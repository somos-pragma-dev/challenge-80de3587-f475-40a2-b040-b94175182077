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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

public class ProcesarPago implements Task {

    private static final Logger logger = LoggerFactory.getLogger(ProcesarPago.class);
    private static final String PROCESADOR_ENDPOINT = "/api/v1/procesador/pagar";
    private static final int MAX_REINTENTOS = 3;
    private static final long TIMEOUT_MS = 45000;
    private static final long RETRY_DELAY_MS = 5000;

    private final Pago pago;
    private String procesadorId;
    private int reintentosActual = 0;

    public ProcesarPago(Pago pago) {
        this.pago = pago;
    }

    public static ProcesarPago enElProcesador(Pago pago) {
        return new ProcesarPago(pago);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info("Iniciando procesamiento de pago - ID: {}, Monto: {}", pago.getId(), pago.getMonto());

        if (!pago.requiereProcesamiento()) {
            logger.warn("El pago no requiere procesamiento - Estado actual: {}", pago.getEstado());
            return;
        }

        boolean procesamientoExitoso = intentarProcesamiento(actor);

        if (!procesamientoExitoso && reintentosActual < MAX_REINTENTOS) {
            logger.warn("Procesamiento inicial fallido, iniciando reintentos. Reintentos restantes: {}",
                MAX_REINTENTOS - reintentosActual);
            procesamientoExitoso = ejecutarConReintentos(actor);
        }

        if (!procesamientoExitoso) {
            logger.error("Pago fallido despues de {} intentos", MAX_REINTENTOS);
            pago.marcarComoTimeout();
            throw new RuntimeException("Pago fallido tras reintentos: " + pago.getId());
        }

        verificarEstadoFinal(actor);
    }

    private <T extends Actor> boolean intentarProcesamiento(T actor) {
        try {
            Map<String, Object> payload = construirPayloadProcesamiento();
            logger.debug("Enviando pago al procesador: {}", payload);

            actor.attemptsTo(
                Post.to(PROCESADOR_ENDPOINT)
                    .with(request -> request
                        .header("Content-Type", "application/json")
                        .header("X-Idempotency-Key", pago.getIdempotencyKey())
                        .header("X-Request-Timeout", String.valueOf(TIMEOUT_MS))
                        .body(payload)
                        .relaxedHTTPSValidation()
                        .timeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    )
            );

            Response respuesta = actor.asksFor(LastResponse.received());
            int statusCode = respuesta.getStatusCode();
            String responseBody = respuesta.getBody().asString();
            logger.info("Respuesta del procesador - Status: {}, Body: {}", statusCode, responseBody);

            if (statusCode == 200 || statusCode == 201) {
                String resultado = respuesta.jsonPath().getString("resultado");
                String idProcesador = respuesta.jsonPath().getString("procesadorId");
                String mensaje = respuesta.jsonPath().getString("mensaje");

                if ("APROBADO".equalsIgnoreCase(resultado) || "SUCCESS".equalsIgnoreCase(resultado)) {
                    this.procesadorId = idProcesador;
                    pago.marcarComoAprobado(idProcesador);
                    pago.setFechaProcesamiento(Instant.now());
                    logger.info("Pago aprobado por procesador - ID: {}", idProcesador);
                    return true;
                } else if ("PENDIENTE".equalsIgnoreCase(resultado)) {
                    logger.info("Pago en estado pendiente, esperando procesamiento asincrono");
                    return esperarProcesamientoAsincrono(actor);
                } else {
                    String mensajeRechazo = mensaje != null ? mensaje : "Rechazado por procesador";
                    pago.marcarComoRechazado(mensajeRechazo);
                    logger.error("Pago rechazado por procesador: {}", mensajeRechazo);
                    return false;
                }
            } else if (statusCode == 408 || statusCode == 504) {
                logger.warn("Timeout del procesador - Status: {}", statusCode);
                pago.marcarComoTimeout();
                return false;
            } else if (statusCode >= 500) {
                logger.error("Error del servidor del procesador - Status: {}", statusCode);
                return false;
            }

            return false;
        } catch (Exception e) {
            logger.error("Excepcion durante procesamiento: {}", e.getMessage(), e);
            return false;
        }
    }

    private <T extends Actor> boolean ejecutarConReintentos(T actor) {
        for (int i = reintentosActual; i < MAX_REINTENTOS; i++) {
            reintentosActual = i + 1;
            logger.info("Ejecutando reintento {} de {}", reintentosActual, MAX_REINTENTOS);

            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Reintento interrumpido");
            }

            if (intentarProcesamiento(actor)) {
                return true;
            }

            logger.warn("Reintento {} fallido", reintentosActual);
        }
        return false;
    }

    private <T extends Actor> boolean esperarProcesamientoAsincrono(T actor) {
        logger.info("Esperando procesamiento asincrono del pago");
        long tiempoInicio = System.currentTimeMillis();
        long tiempoMaximo = TIMEOUT_MS;

        while (System.currentTimeMillis() - tiempoInicio < tiempoMaximo) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            Boolean estadoActualizado = actor.asksFor(VerificarEstadoPago.delPago(pago.getId()));
            if (estadoActualizado != null && estadoActualizado) {
                logger.info("Pago procesado asincronamente de forma exitosa");
                return true;
            }
        }

        logger.warn("Timeout esperando procesamiento asincrono");
        return false;
    }

    private <T extends Actor> void verificarEstadoFinal(T actor) {
        actor.should(
            seeThat("El pago debe estar aprobado",
                actor1 -> pago.getEstado() == EstadoPago.APROBADO,
                is(true)
            )
        );
        logger.info("Verificacion de estado final completada - Estado: {}", pago.getEstado());
    }

    private Map<String, Object> construirPayloadProcesamiento() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("pagoId", pago.getId());
        payload.put("idempotencyKey", pago.getIdempotencyKey());
        payload.put("monto", pago.getMonto());
        payload.put("moneda", pago.getMoneda());
        payload.put("cuentaOrigen", pago.getCuentaOrigen());
        payload.put("cuentaDestino", pago.getCuentaDestino());
        payload.put("concepto", pago.getConcepto());
        payload.put("referenciaExternal", pago.getReferenciaExternal());
        payload.put("sistemaOrigen", pago.getSistemaOrigen());
        payload.put("timestamp", Instant.now().toString());
        return payload;
    }

    public String getProcesadorId() {
        return procesadorId;
    }

    public int getReintentosActual() {
        return reintentosActual;
    }
}