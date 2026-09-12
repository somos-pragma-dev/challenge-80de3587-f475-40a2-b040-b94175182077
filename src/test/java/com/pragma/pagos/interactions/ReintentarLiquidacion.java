package com.pragma.pagos.interactions;

import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actors.Actor;
import net.serenitybdd.screenplay.rest.questions.RestQuestion;
import net.serenitybdd.screenplay.rest.questions.CallAnApi;
import com.pragma.pagos.models.Pago;
import com.pragma.pagos.models.Pago.EstadoPago;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ReintentarLiquidacion implements Interaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReintentarLiquidacion.class);
    private static final int MAX_REINTENTOS = 5;
    private static final int ESPERA_BASE_MILLIS = 1000;
    private static final int ESPERA_MAX_MILLIS = 10000;
    private static final double FACTOR_EXPONENCIAL = 2.0;

    private final String pagoId;
    private final String idempotencyKey;
    private final int reintentosMaximos;
    private final boolean usarEstrategiaExponencial;
    private final EstadoPago[] estadosQueRequierenReintento;
    private final boolean verificarConectividadPrevia;

    public ReintentarLiquidacion(String pagoId, String idempotencyKey) {
        this(pagoId, idempotencyKey, MAX_REINTENTOS, true, 
             new EstadoPago[]{EstadoPago.FALLIDO, EstadoPago.TIMEOUT}, true);
    }

    public ReintentarLiquidacion(String pagoId, String idempotencyKey, int reintentosMaximos,
                                  boolean usarEstrategiaExponencial,
                                  EstadoPago[] estadosQueRequierenReintento,
                                  boolean verificarConectividadPrevia) {
        this.pagoId = pagoId;
        this.idempotencyKey = idempotencyKey;
        this.reintentosMaximos = reintentosMaximos;
        this.usarEstrategiaExponencial = usarEstrategiaExponencial;
        this.estadosQueRequierenReintento = estadosQueRequierenReintento;
        this.verificarConectividadPrevia = verificarConectividadPrevia;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        LOGGER.info("Iniciando proceso de reintento de liquidación para pago: {}", pagoId);
        LOGGER.info("Configuración: reintentos={}, estrategia={}", reintentosMaximos, 
                   usarEstrategiaExponencial ? "exponencial" : "lineal");

        int intentos = 0;
        Instant tiempoInicio = Instant.now();
        boolean liquidado = false;
        String ultimoError = null;

        while (intentos < reintentosMaximos && !liquidado) {
            try {
                if (verificarConectividadPrevia && !verificarConectividad(actor)) {
                    LOGGER.warn("Conectividad con sistema de liquidación no disponible. Reintentando...");
                    esperarReintento(intentos);
                    intentos++;
                    continue;
                }

                LOGGER.info("Intento {} de {} - Ejecutando liquidación", intentos + 1, reintentosMaximos);
                
                Pago resultado = ejecutarLiquidacion(actor);
                
                if (resultado != null && resultado.estaLiquidado()) {
                    liquidado = true;
                    LOGGER.info("Liquidación exitosa en el intento {}", intentos + 1);
                } else if (resultado != null && requiereReintento(resultado.getEstado())) {
                    LOGGER.warn("Liquidación retornó estado que requiere reintento: {}", resultado.getEstado());
                    ultimoError = resultado.getMensajeError();
                    esperarReintento(intentos);
                    intentos++;
                } else {
                    LOGGER.error("Estado inesperado: {}", resultado != null ? resultado.getEstado() : "null");
                    ultimoError = "Estado de respuesta no reconocido";
                    intentos++;
                }

            } catch (Exception e) {
                LOGGER.error("Error en intento {}: {}", intentos + 1, e.getMessage());
                ultimoError = e.getMessage();
                
                if (esErrorRecuperable(e)) {
                    LOGGER.info("Error recuperable. Reintentando...");
                    try {
                        esperarReintento(intentos);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Reintento interrumpido", ie);
                    }
                    intentos++;
                } else {
                    LOGGER.error("Error no recuperable. Abortando reintentos.", e);
                    throw new RuntimeException("Error no recuperable en liquidación: " + e.getMessage(), e);
                }
            }
        }

        Duration duracionTotal = Duration.between(tiempoInicio, Instant.now());
        
        if (!liquidado) {
            LOGGER.error("Liquidación fallida después de {} intentos en {} ms", 
                        reintentosMaximos, duracionTotal.toMillis());
            throw new RuntimeException(String.format("No se pudo liquidar el pago después de %d intentos. Último error: %s",
                    reintentosMaximos, ultimoError));
        }

        LOGGER.info("Proceso de reintento completado exitosamente. Duración total: {} ms", duracionTotal.toMillis());
    }

    private boolean verificarConectividad(Actor actor) {
        try {
            String endpoint = "/api/health/liquidacion";
            Map<String, Object> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            
            Object respuesta = RestQuestion.about("Verificación de conectividad con sistema de liquidación")
                    .get(endpoint)
                    .withHeaders(headers)
                    .answeredBy(actor);
            
            return respuesta != null;
        } catch (Exception e) {
            LOGGER.warn("Falló verificación de conectividad: {}", e.getMessage());
            return false;
        }
    }

    private Pago ejecutarLiquidacion(Actor actor) {
        String endpoint = String.format("/api/pagos/%s/liquidar", pagoId);
        
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Idempotency-Key", idempotencyKey + "-liquidacion-" + System.currentTimeMillis());
        headers.put("X-Retry-Count", String.valueOf(reintentosMaximos));
        
        Map<String, Object> body = new HashMap<>();
        body.put("pagoId", pagoId);
        body.put("idempotencyKey", idempotencyKey);
        body.put("timestamp", Instant.now().toString());
        
        return RestQuestion.about("Ejecución de liquidación")
                .post(endpoint)
                .withHeaders(headers)
                .with(body)
                .answeredBy(actor);
    }

    private boolean requiereReintento(EstadoPago estado) {
        for (EstadoPago estadoReintento : estadosQueRequierenReintento) {
            if (estadoReintento.equals(estado)) {
                return true;
            }
        }
        return false;
    }

    private boolean esErrorRecuperable(Exception e) {
        String mensaje = e.getMessage().toLowerCase();
        return mensaje.contains("timeout") || 
               mensaje.contains("connection") || 
               mensaje.contains("refused") || 
               mensaje.contains("unavailable") ||
               mensaje.contains("network");
    }

    private void esperarReintento(int intentoActual) throws InterruptedException {
        int tiempoEspera;
        
        if (usarEstrategiaExponencial) {
            tiempoEspera = (int) Math.min(ESPERA_BASE_MILLIS * Math.pow(FACTOR_EXPONENCIAL, intentoActual), ESPERA_MAX_MILLIS);
        } else {
            tiempoEspera = ESPERA_BASE_MILLIS * (intentoActual + 1);
        }
        
        tiempoEspera += new Random().nextInt(500);
        
        LOGGER.info("Esperando {} ms antes del siguiente reintento", tiempoEspera);
        Thread.sleep(tiempoEspera);
    }

    public static ReintentarLiquidacion paraPago(String pagoId, String idempotencyKey) {
        return new ReintentarLiquidacion(pagoId, idempotencyKey);
    }

    public static ReintentarLiquidacion.Builder configurar() {
        return new Builder();
    }

    public static class Builder {
        private String pagoId;
        private String idempotencyKey;
        private int reintentosMaximos = MAX_REINTENTOS;
        private boolean usarEstrategiaExponencial = true;
        private EstadoPago[] estadosQueRequierenReintento = new EstadoPago[]{EstadoPago.FALLIDO, EstadoPago.TIMEOUT};
        private boolean verificarConectividadPrevia = true;

        public Builder paraPago(String pagoId) {
            this.pagoId = pagoId;
            return this;
        }

        public Builder conIdempotencyKey(String key) {
            this.idempotencyKey = key;
            return this;
        }

        public Builder conMaximosReintentos(int reintentos) {
            this.reintentosMaximos = reintentos;
            return this;
        }

        public Builder conEstrategiaExponencial(boolean usar) {
            this.usarEstrategiaExponencial = usar;
            return this;
        }

        public Builder conEstadosQueRequierenReintento(EstadoPago[] estados) {
            this.estadosQueRequierenReintento = estados;
            return this;
        }

        public Builder conVerificacionDeConectividad(boolean verificar) {
            this.verificarConectividadPrevia = verificar;
            return this;
        }

        public ReintentarLiquidacion build() {
            return new ReintentarLiquidacion(pagoId, idempotencyKey, reintentosMaximos,
                    usarEstrategiaExponencial, estadosQueRequierenReintento, verificarConectividadPrevia);
        }
    }
}