package com.pragma.pagos.tasks;

import com.pragma.pagos.models.Pago;
import com.pragma.pagos.models.Pago.EstadoPago;
import com.pragma.pagos.questions.VerificarEstadoPago;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

public class LiquidarPago implements Task {

    private static final Logger logger = LoggerFactory.getLogger(LiquidarPago.class);
    private static final String LIQUIDACION_ENDPOINT = "/api/v1/liquidacion/registrar";
    private static final int MAX_REINTENTOS_CONECTIVIDAD = 5;
    private static final long TIMEOUT_MS = 30000;
    private static final long RETRY_DELAY_MS = 3000;
    private static final long MAX_ESPERA_CONECTIVIDAD_MS = 60000;

    private final Pago pago;
    private String referenciaLiquidacion;
    private int intentosConectividad = 0;

    public LiquidarPago(Pago pago) {
        this.pago = pago;
    }

    public static LiquidarPago enElSistemaDeLiquidacion(Pago pago) {
        return new LiquidarPago(pago);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info("Iniciando liquidacion de pago - ID: {}, Estado actual: {}", pago.getId(), pago.getEstado());

        if (!pago.estaLiquidado()) {
            logger.warn("El pago no esta en estado de liquidacion - Estado: {}", pago.getEstado());
            if (pago.getEstado() != EstadoPago.APROBADO) {
                throw new IllegalStateException("No se puede liquidar un pago que no esta aprobado. Estado: " + pago.getEstado());
            }
        }

        boolean liquidado = false;
        Exception ultimaExcepcion = null;

        for (int intento = 1; intento <= MAX_REINTENTOS_CONECTIVIDAD; intento++) {
            try {
                liquidado = intentarLiquidacion(actor);
                if (liquidado) {
                    break;
                }
            } catch (Exception e) {
                ultimaExcepcion = e;
                logger.error("Error de conectividad en intento {}: {}", intento, e.getMessage());
                intentosConectividad = intento;

                if (intento < MAX_REINTENTOS_CONECTIVIDAD) {
                    logger.info("Esperando antes del siguiente intento de conectividad...");
                    esperarReintentoConectividad();
                }
            }
        }

        if (!liquidado && ultimaExcepcion != null) {
            logger.error("Liquidacion fallida despues de {} intentos por problemas de conectividad", MAX_REINTENTOS_CONECTIVIDAD);
            pago.marcarComoFallido("Fallos de conectividad con sistema de liquidacion: " + ultimaExcepcion.getMessage());
            throw new RuntimeException("No se pudo liquidar el pago por problemas de conectividad", ultimaExcepcion);
        }

        verificarLiquidacionExitosa(actor);
    }

    private <T extends Actor> boolean intentarLiquidacion(T actor) {
        logger.debug("Intentando liquidacion del pago {}", pago.getId());

        Map<String, Object> payload = construirPayloadLiquidacion();
        logger.debug("Payload de liquidacion: {}", payload);

        try {
            actor.attemptsTo(
                Post.to(LIQUIDACION_ENDPOINT)
                    .with(request -> request
                        .header("Content-Type", "application/json")
                        .header("X-Idempotency-Key", generarClaveLiquidacion())
                        .header("X-Request-Timeout", String.valueOf(TIMEOUT_MS))
                        .body(payload)
                        .relaxedHTTPSValidation()
                        .timeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    )
            );

            Response respuesta = actor.asksFor(LastResponse.received());
            int statusCode = respuesta.getStatusCode();
            String responseBody = respuesta.getBody().asString();
            logger.info("Respuesta del sistema de liquidacion - Status: {}, Body: {}", statusCode, responseBody);

            if (statusCode == 200 || statusCode == 201) {
                String estadoLiquidacion = respuesta.jsonPath().getString("estado");
                this.referenciaLiquidacion = respuesta.jsonPath().getString("referenciaLiquidacion");

                if ("LIQUIDADO".equalsIgnoreCase(estadoLiquidacion) || "CONFIRMADO".equalsIgnoreCase(estadoLiquidacion)) {
                    pago.marcarComoLiquidado();
                    pago.setFechaLiquidacion(Instant.now());
                    logger.info("Pago liquidado exitosamente - Referencia: {}", referenciaLiquidacion);
                    return true;
                } else if ("PENDIENTE".equalsIgnoreCase(estadoLiquidacion)) {
                    logger.info("Liquidacion en proceso asincrono, esperando confirmacion");
                    return esperarConfirmacionLiquidacion(actor);
                }
            } else if (statusCode >= 500) {
                logger.error("Error del servidor de liquidacion - Status: {}", statusCode);
                throw new RuntimeException("Error de conectividad con sistema de liquidacion: HTTP " + statusCode);
            } else if (statusCode == 409 || statusCode == 422) {
                String mensaje = respuesta.jsonPath().getString("mensaje");
                if (mensaje != null && mensaje.toLowerCase().contains("ya liquidado")) {
                    logger.info("El pago ya fue liquidado anteriormente");
                    pago.marcarComoLiquidado();
                    return true;
                }
            }

            return false;
        } catch (net.serenitybdd.screenplay.rest.exceptions.PotentialAuthenticationFailureException e) {
            logger.error("Error de autenticacion con sistema de liquidacion: {}", e.getMessage());
            throw new RuntimeException("Error de autenticacion con sistema de liquidacion", e);
        } catch (Exception e) {
            if (e.getMessage() != null && (e.getMessage().contains("Connection") || 
                e.getMessage().contains("timeout") || e.getMessage().contains("ConnectionRefused"))) {
                logger.error("Fallo de conectividad detectado: {}", e.getMessage());
                throw new RuntimeException("Fallo de conectividad con sistema de liquidacion", e);
            }
            throw e;
        }
    }

    private <T extends Actor> boolean esperarConfirmacionLiquidacion(T actor) {
        logger.info("Esperando confirmacion de liquidacion asincrona");
        long tiempoInicio = System.currentTimeMillis();
        long tiempoMaximo = MAX_ESPERA_CONECTIVIDAD_MS;

        while (System.currentTimeMillis() - tiempoInicio < tiempoMaximo) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            Boolean estadoActualizado = actor.asksFor(VerificarEstadoPago.delPago(pago.getId()));
            if (estadoActualizado != null && estadoActualizado && pago.estaLiquidado()) {
                logger.info("Liquidacion confirmada asincronamente");
                return true;
            }
        }

        logger.warn("Timeout esperando confirmacion de liquidacion");
        return false;
    }

    private void esperarReintentoConectividad() {
        try {
            logger.debug("Esperando {} ms antes de reintentar", RETRY_DELAY_MS);
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Espera de reintento interrumpida");
        }
    }

    private <T extends Actor> void verificarLiquidacionExitosa(T actor) {
        actor.should(
            seeThat("El pago debe estar liquidado",
                actor1 -> pago.estaLiquidado(),
                is(true)
            )
        );
        logger.info("Verificacion de liquidacion completada - Fecha: {}", pago.getFechaLiquidacion());
    }

    private Map<String, Object> construirPayloadLiquidacion() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("pagoId", pago.getId());
        payload.put("procesadorPagoId", pago.getProcesadorPagoId());
        payload.put("monto", pago.getMonto());
        payload.put("moneda", pago.getMoneda());
        payload.put("cuentaOrigen", pago.getCuentaOrigen());
        payload.put("cuentaDestino", pago.getCuentaDestino());
        payload.put("referenciaExternal", pago.getReferenciaExternal());
        payload.put("fechaProcesamiento", pago.getFechaProcesamiento() != null ? 
            pago.getFechaProcesamiento().toString() : Instant.now().toString());
        payload.put("sistemaOrigen", pago.getSistemaOrigen());
        payload.put("timestamp", Instant.now().toString());
        return payload;
    }

    private String generarClaveLiquidacion() {
        return pago.getIdempotencyKey() + "_LIQ_" + System.currentTimeMillis();
    }

    public String getReferenciaLiquidacion() {
        return referenciaLiquidacion;
    }

    public int getIntentosConectividad() {
        return intentosConectividad;
    }
}