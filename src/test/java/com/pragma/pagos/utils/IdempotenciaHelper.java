package com.pragma.pagos.utils;

import com.pragma.pagos.models.Pago;
import net.serenitybdd.core.Serenity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IdempotenciaHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdempotenciaHelper.class);
    private static final String KEY_PREFIX = "IDEM";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Map<String, Pago> REGISTRO_IDEMPOTENCIA = new ConcurrentHashMap<>();
    private static final Map<String, Instant> CACHE_EXPIRACION = new ConcurrentHashMap<>();
    private static final long TTL_MINUTOS = 60;

    public static String generarClaveIdempotencia(String cuentaOrigen, BigDecimal monto) {
        String timestamp = LocalDateTime.now(ZoneId.of("America/Bogota")).format(TIMESTAMP_FORMAT);
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String clave = String.format("%s-%s-%s-%s", KEY_PREFIX, timestamp, cuentaOrigen.substring(cuentaOrigen.length() - 6), uniqueId);
        LOGGER.info("Clave idempotencia generada: {} para cuenta {} con monto {}", clave, cuentaOrigen, monto);
        return clave;
    }

    public static String generarClaveIdempotencia(String cuentaOrigen, BigDecimal monto, String concepto) {
        String timestamp = LocalDateTime.now(ZoneId.of("America/Bogota")).format(TIMESTAMP_FORMAT);
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String hashConcepto = String.valueOf(concepto.hashCode()).replace("-", "").substring(0, 4).toUpperCase();
        String clave = String.format("%s-%s-%s-%s-%s", KEY_PREFIX, timestamp, cuentaOrigen.substring(cuentaOrigen.length() - 6), hashConcepto, uniqueId);
        LOGGER.info("Clave idempotencia generada con concepto: {} para cuenta {} con monto {} y concepto {}", clave, cuentaOrigen, monto, concepto);
        return clave;
    }

    public static boolean esClaveIdempotenciaValida(String claveIdempotencia) {
        if (claveIdempotencia == null || claveIdempotencia.isBlank()) {
            LOGGER.warn("Clave idempotencia nula o vacía");
            return false;
        }
        if (!claveIdempotencia.startsWith(KEY_PREFIX)) {
            LOGGER.warn("Clave idempotencia no tiene el prefijo esperado: {}", claveIdempotencia);
            return false;
        }
        String[] partes = claveIdempotencia.split("-");
        if (partes.length < 3) {
            LOGGER.warn("Clave idempotencia no tiene el formato esperado: {}", claveIdempotencia);
            return false;
        }
        return true;
    }

    public static boolean verificarDuplicado(String claveIdempotencia) {
        if (!esClaveIdempotenciaValida(claveIdempotencia)) {
            return false;
        }
        boolean existe = REGISTRO_IDEMPOTENCIA.containsKey(claveIdempotencia);
        if (existe) {
            Pago pagoExistente = REGISTRO_IDEMPOTENCIA.get(claveIdempotencia);
            LOGGER.info("Pago duplicado detectado para clave: {}. Estado actual: {}", claveIdempotencia, pagoExistente.getEstado());
            Serenity.getCurrentSession().put("pago_existente", pagoExistente);
        } else {
            LOGGER.info("Nueva clave idempotencia: {} - no existe en registros", claveIdempotencia);
        }
        return existe;
    }

    public static void registrarPago(Pago pago) {
        if (pago == null || pago.getIdempotencyKey() == null) {
            LOGGER.error("No se puede registrar un pago nulo o sin clave de idempotencia");
            return;
        }
        REGISTRO_IDEMPOTENCIA.put(pago.getIdempotencyKey(), pago);
        Instant expiracion = Instant.now().plusSeconds(TTL_MINUTOS * 60);
        CACHE_EXPIRACION.put(pago.getIdempotencyKey(), expiracion);
        LOGGER.info("Pago registrado con clave idempotencia: {}. Expira en {}", pago.getIdempotencyKey(), expiracion);
    }

    public static Pago obtenerPagoPorClave(String claveIdempotencia) {
        if (!esClaveIdempotenciaValida(claveIdempotencia)) {
            return null;
        }
        Pago pago = REGISTRO_IDEMPOTENCIA.get(claveIdempotencia);
        if (pago != null) {
            Instant expiracion = CACHE_EXPIRACION.get(claveIdempotencia);
            if (expiracion != null && Instant.now().isAfter(expiracion)) {
                LOGGER.warn("Clave idempotencia expirada: {}", claveIdempotencia);
                REGISTRO_IDEMPOTENCIA.remove(claveIdempotencia);
                CACHE_EXPIRACION.remove(claveIdempotencia);
                return null;
            }
        }
        return pago;
    }

    public static boolean verificarYRegistrar(String claveIdempotencia, Pago nuevoPago) {
        if (verificarDuplicado(claveIdempotencia)) {
            LOGGER.info("Duplicado detectado - no se registra el nuevo pago");
            return false;
        }
        registrarPago(nuevoPago);
        return true;
    }

    public static void limpiarRegistrosExpirados() {
        Instant ahora = Instant.now();
        CACHE_EXPIRACION.entrySet().removeIf(entry -> ahora.isAfter(entry.getValue()));
        LOGGER.info("Registros de idempotencia expirados limpiados. Registros activos: {}", REGISTRO_IDEMPOTENCIA.size());
    }

    public static Map<String, Object> obtenerMetricasIdempotencia() {
        Map<String, Object> metricas = new HashMap<>();
        metricas.put("total_registros", REGISTRO_IDEMPOTENCIA.size());
        metricas.put("cache_expirados", CACHE_EXPIRACION.size());
        metricas.put("ttl_minutos", TTL_MINUTOS);
        metricas.put("timestamp_consulta", Instant.now().toString());
        return metricas;
    }

    public static void resetearContadorPruebas() {
        REGISTRO_IDEMPOTENCIA.clear();
        CACHE_EXPIRACION.clear();
        LOGGER.info("Contador de idempotencia reseteado para nuevas pruebas");
    }

    public static boolean sonMismosDatos(Pago pago1, Pago pago2) {
        if (pago1 == null || pago2 == null) {
            return false;
        }
        boolean mismaCuentaOrigen = pago1.getCuentaOrigen().equals(pago2.getCuentaOrigen());
        boolean mismaCuentaDestino = pago1.getCuentaDestino().equals(pago2.getCuentaDestino());
        boolean mismoMonto = pago1.getMonto().compareTo(pago2.getMonto()) == 0;
        boolean mismoConcepto = pago1.getConcepto() != null && pago1.getConcepto().equals(pago2.getConcepto());
        LOGGER.debug("Comparación de pagos - cuenta origen: {}, cuenta destino: {}, monto: {}, concepto: {}",
                mismaCuentaOrigen, mismaCuentaDestino, mismoMonto, mismoConcepto);
        return mismaCuentaOrigen && mismaCuentaDestino && mismoMonto && mismoConcepto;
    }
}