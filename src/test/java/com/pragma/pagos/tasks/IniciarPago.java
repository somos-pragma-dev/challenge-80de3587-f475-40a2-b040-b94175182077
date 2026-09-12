package com.pragma.pagos.tasks;

import com.pragma.pagos.models.Pago;
import com.pragma.pagos.models.Pago.EstadoPago;
import com.pragma.pagos.utils.IdempotenciaHelper;
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

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

public class IniciarPago implements Task {

    private static final Logger logger = LoggerFactory.getLogger(IniciarPago.class);
    private static final String SISTEMA_ORIGEN_ENDPOINT = "/api/v1/pagos/iniciar";
    private static final int TIMEOUT_SECONDS = 30;

    private final BigDecimal monto;
    private final String cuentaOrigen;
    private final String cuentaDestino;
    private final String concepto;
    private final String canal;
    private final String usuario;

    private Pago pagoCreado;

    public IniciarPago(BigDecimal monto, String cuentaOrigen, String cuentaDestino, String concepto) {
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.concepto = concepto;
        this.canal = "AUTOMATIZADO";
        this.usuario = "QA_AUTOMATION";
    }

    public static IniciarPago conDatos(BigDecimal monto, String cuentaOrigen, String cuentaDestino, String concepto) {
        return new IniciarPago(monto, cuentaOrigen, cuentaDestino, concepto);
    }

    public IniciarPago conCanal(String canal) {
        this.canal = canal;
        return this;
    }

    public IniciarPago conUsuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info("Iniciando proceso de pago - Monto: {}, Origen: {}, Destino: {}", monto, cuentaOrigen, cuentaDestino);

        String idempotencyKey = IdempotenciaHelper.generarClaveIdempotencia(cuentaOrigen, monto);
        logger.debug("Clave de idempotencia generada: {}", idempotencyKey);

        Map<String, Object> payload = construirPayloadPago(idempotencyKey);
        logger.debug("Payload construido para iniciar pago: {}", payload);

        actor.attemptsTo(
            Post.to(SISTEMA_ORIGEN_ENDPOINT)
                .with(request -> request
                    .header("Content-Type", "application/json")
                    .header("X-Idempotency-Key", idempotencyKey)
                    .header("X-Request-Timeout", String.valueOf(TIMEOUT_SECONDS))
                    .body(payload)
                    .relaxedHTTPSValidation()
                )
        );

        Response respuesta = actor.asksFor(LastResponse.received());
        int statusCode = respuesta.getStatusCode();
        logger.info("Respuesta del sistema de origen - Status: {}, Body: {}", statusCode, respuesta.getBody().asString());

        actor.should(
            seeThat("Codigo de estado de respuesta",
                response -> statusCode,
                anyOf(is(200), is(201), is(202))
            )
        );

        Pago pago = mapearRespuestaAPago(respuesta, idempotencyKey);
        this.pagoCreado = pago;

        logger.info("Pago iniciado exitosamente - ID: {}, Estado: {}", pago.getId(), pago.getEstado());
        actor.remember("pagoActual", pago);
        actor.remember("idempotencyKey", idempotencyKey);
    }

    private Map<String, Object> construirPayloadPago(String idempotencyKey) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("idempotencyKey", idempotencyKey);
        payload.put("monto", monto);
        payload.put("moneda", "COP");
        payload.put("cuentaOrigen", cuentaOrigen);
        payload.put("cuentaDestino", cuentaDestino);
        payload.put("concepto", concepto);
        payload.put("canal", canal);
        payload.put("usuario", usuario);
        payload.put("ipOrigen", "192.168.1.100");
        payload.put("tipoPago", "TRANSFERENCIA");
        return payload;
    }

    private Pago mapearRespuestaAPago(Response respuesta, String idempotencyKey) {
        Pago pago = Pago.crearNuevo(monto, cuentaOrigen, cuentaDestino, concepto);
        pago.setIdempotencyKey(idempotencyKey);
        pago.setCanal(canal);
        pago.setUsuario(usuario);
        pago.setIpOrigen("192.168.1.100");
        pago.setFechaCreacion(Instant.now());

        if (respuesta.getStatusCode() == 200 || respuesta.getStatusCode() == 201) {
            String idPago = respuesta.jsonPath().getString("id");
            String estado = respuesta.jsonPath().getString("estado");
            String referencia = respuesta.jsonPath().getString("referenciaExternal");

            pago.setId(idPago);
            pago.setEstado(EstadoPago.valueOf(estado.toUpperCase()));
            pago.setReferenciaExternal(referencia);
            pago.setSistemaOrigen("SISTEMA_ORIGEN");

            if (estado.equalsIgnoreCase("PROCESANDO")) {
                pago.marcarComoProcesando();
            } else if (estado.equalsIgnoreCase("APROBADO")) {
                pago.marcarComoAprobado("PROCESADOR_DEFAULT");
            }
        } else if (respuesta.getStatusCode() == 202) {
            String idPago = respuesta.jsonPath().getString("id");
            pago.setId(idPago);
            pago.setEstado(EstadoPago.PENDIENTE);
            pago.setSistemaOrigen("SISTEMA_ORIGEN");
        }

        return pago;
    }

    public Pago getPagoCreado() {
        return pagoCreado;
    }
}