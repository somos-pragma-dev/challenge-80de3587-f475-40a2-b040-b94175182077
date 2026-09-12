package com.pragma.pagos.interactions;

import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.questions.RestQuestion;
import net.serenitybdd.screenplay.Functions;
import net.serenitybdd.screenplay.Task;
import com.pragma.pagos.models.Pago;
import com.pragma.pagos.models.Pago.EstadoPago;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EsperarRespuestaProcesador implements Interaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsperarRespuestaProcesador.class);

    private final String pagoId;
    private final String idempotencyKey;
    private final int timeoutTotalSegundos;
    private final int intervaloPoolSegundos;
    private final int maximosReintentos;
    private final EstadoPago[] estadosExito;
    private final EstadoPago[] estadosFallido;

    public EsperarRespuestaProcesador(String pagoId, String idempotencyKey) {
        this(pagoId, idempotencyKey, 60, 5, 3, 
             new EstadoPago[]{EstadoPago.APROBADO, EstadoPago.LIQUIDADO},
             new EstadoPago[]{EstadoPago.RECHAZADO, EstadoPago.FALLIDO});
    }

    public EsperarRespuestaProcesador(String pagoId, String idempotencyKey, int timeoutTotalSegundos,
                                       int intervaloPoolSegundos, int maximosReintentos,
                                       EstadoPago[] estadosExito, EstadoPago[] estadosFallido) {
        this.pagoId = pagoId;
        this.idempotencyKey = idempotencyKey;
        this.timeoutTotalSegundos = timeoutTotalSegundos;
        this.intervaloPoolSegundos = intervaloPoolSegundos;
        this.maximosReintentos = maximosReintentos;
        this.estadosExito = estadosExito;
        this.estadosFallido = estadosFallido;
    }

    @Override
    public <T extends net.serenitybdd.screenplay.actors.Actor> void performAs(T actor) {
        LOGGER.info("Iniciando espera de respuesta del procesador para pago: {}", pagoId);
        
        Instant tiempoInicio = Instant.now();
        int intentos = 0;
        boolean respuestaRecibida = false;
        EstadoPago estadoFinal = null;
        
        while (!respuestaRecibida && intentos < maximosReintentos) {
            try {
                Pago pago = consultarEstadoPago(actor);
                estadoFinal = pago.getEstado();
                
                LOGGER.info("Intento {} - Estado actual del pago: {}", intentos + 1, estadoFinal);
                
                if (esEstadoTerminal(estadoFinal)) {
                    respuestaRecibida = true;
                    LOGGER.info("Respuesta recibida. Estado final: {}", estadoFinal);
                    break;
                }
                
                if (haExcedidoTimeout(tiempoInicio)) {
                    LOGGER.warn("Timeout excedido después de {} segundos", timeoutTotalSegundos);
                    throw new TimeoutException("El procesador excedió el tiempo máximo de respuesta");
                }
                
                intentos++;
                LOGGER.info("Esperando {} segundos antes del siguiente intento", intervaloPoolSegundos);
                Thread.sleep(intervaloPoolSegundos * 1000L);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Hilo interrumpido durante la espera del procesador", e);
                throw new RuntimeException("Operación interrumpida", e);
            } catch (TimeoutException e) {
                LOGGER.error("Timeout en la comunicación con el procesador de pagos", e);
                throw new RuntimeException(e);
            }
        }
        
        if (!respuestaRecibida) {
            LOGGER.error("No se recibió respuesta después de {} intentos", maximosReintentos);
            throw new RuntimeException("No se recibió respuesta del procesador después de " + maximosReintentos + " intentos");
        }
    }

    private Pago consultarEstadoPago(net.serenitybdd.screenplay.actors.Actor actor) {
        String endpoint = String.format("/api/pagos/%s", pagoId);
        
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Idempotency-Key", idempotencyKey);
        headers.put("X-Request-Timeout", String.valueOf(intervaloPoolSegundos));
        
        return RestQuestion.about("Consulta de estado al procesador")
                .get(endpoint)
                .withHeaders(headers)
                .answeredBy(actor);
    }

    private boolean esEstadoTerminal(EstadoPago estado) {
        for (EstadoPago exito : estadosExito) {
            if (exito.equals(estado)) return true;
        }
        for (EstadoPago fallido : estadosFallido) {
            if (fallido.equals(estado)) return true;
        }
        return false;
    }

    private boolean haExcedidoTimeout(Instant tiempoInicio) {
        Duration transcurrido = Duration.between(tiempoInicio, Instant.now());
        return transcurrido.getSeconds() >= timeoutTotalSegundos;
    }

    public static EsperarRespuestaProcesador conTimeout(String pagoId, String idempotencyKey) {
        return new EsperarRespuestaProcesador(pagoId, idempotencyKey);
    }

    public static Builder conConfiguracion() {
        return new Builder();
    }

    public static class Builder {
        private String pagoId;
        private String idempotencyKey;
        private int timeoutTotalSegundos = 60;
        private int intervaloPoolSegundos = 5;
        private int maximosReintentos = 3;
        private EstadoPago[] estadosExito = new EstadoPago[]{EstadoPago.APROBADO, EstadoPago.LIQUIDADO};
        private EstadoPago[] estadosFallido = new EstadoPago[]{EstadoPago.RECHAZADO, EstadoPago.FALLIDO};

        public Builder paraPago(String pagoId) {
            this.pagoId = pagoId;
            return this;
        }

        public Builder conIdempotencyKey(String key) {
            this.idempotencyKey = key;
            return this;
        }

        public Builder conTimeout(int segundos) {
            this.timeoutTotalSegundos = segundos;
            return this;
        }

        public Builder conIntervaloPool(int segundos) {
            this.intervaloPoolSegundos = segundos;
            return this;
        }

        public Builder conMaximosReintentos(int reintentos) {
            this.maximosReintentos = reintentos;
            return this;
        }

        public EsperarRespuestaProcesador build() {
            return new EsperarRespuestaProcesador(pagoId, idempotencyKey, timeoutTotalSegundos,
                    intervaloPoolSegundos, maximosReintentos, estadosExito, estadosFallido);
        }
    }
}