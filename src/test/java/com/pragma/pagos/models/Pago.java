package com.pragma.pagos.models;

import java.math.BigDecimal;
import java.time.Instant;

public class Pago {
    private String id;
    private String idempotencyKey;
    private BigDecimal monto;
    private String moneda;
    private String cuentaOrigen;
    private String cuentaDestino;
    private String concepto;
    private EstadoPago estado;
    private Instant fechaCreacion;
    private Instant fechaActualizacion;
    private Instant fechaProcesamiento;
    private Instant fechaLiquidacion;
    private String referenciaExternal;
    private String procesadorPagoId;
    private String sistemaOrigen;
    private String canal;
    private String usuario;
    private String ipOrigen;
    private String mensajeError;
    private Integer reintentos;
    private TipoPago tipoPago;

    public enum EstadoPago {
        PENDIENTE, PROCESANDO, APROBADO, RECHAZADO, LIQUIDADO, FALLIDO, TIMEOUT
    }

    public enum TipoPago {
        TRANSFERENCIA, PAGO_INMEDIATO, PAGO_DIFERIDO
    }

    public Pago() {
        this.reintentos = 0;
        this.moneda = "USD";
    }

    public static Pago crearNuevo(BigDecimal monto, String cuentaOrigen, String cuentaDestino, String concepto) {
        Pago pago = new Pago();
        pago.monto = monto;
        pago.cuentaOrigen = cuentaOrigen;
        pago.cuentaDestino = cuentaDestino;
        pago.concepto = concepto;
        pago.estado = EstadoPago.PENDIENTE;
        pago.fechaCreacion = Instant.now();
        pago.idempotencyKey = generarIdempotencyKey(cuentaOrigen, monto);
        return pago;
    }

    public static String generarIdempotencyKey(String cuentaOrigen, BigDecimal monto) {
        return String.format("idem-%s-%s-%d", cuentaOrigen, monto.toPlainString(), System.currentTimeMillis());
    }

    public boolean esIdempotente() {
        return idempotencyKey != null && !idempotencyKey.isEmpty();
    }

    public boolean puedeReintentarse() {
        return reintentos != null && reintentos < 3;
    }

    public boolean estaLiquidado() {
        return estado == EstadoPago.LIQUIDADO;
    }

    public boolean requiereProcesamiento() {
        return estado == EstadoPago.PENDIENTE || estado == EstadoPago.PROCESANDO;
    }

    public void marcarComoProcesando() {
        this.estado = EstadoPago.PROCESANDO;
        this.fechaProcesamiento = Instant.now();
        this.fechaActualizacion = Instant.now();
    }

    public void marcarComoAprobado(String procesadorId) {
        this.estado = EstadoPago.APROBADO;
        this.procesadorPagoId = procesadorId;
        this.fechaActualizacion = Instant.now();
    }

    public void marcarComoRechazado(String mensaje) {
        this.estado = EstadoPago.RECHAZADO;
        this.mensajeError = mensaje;
        this.fechaActualizacion = Instant.now();
    }

    public void marcarComoLiquidado() {
        this.estado = EstadoPago.LIQUIDADO;
        this.fechaLiquidacion = Instant.now();
        this.fechaActualizacion = Instant.now();
    }

    public void marcarComoFallido(String mensaje) {
        this.estado = EstadoPago.FALLIDO;
        this.mensajeError = mensaje;
        this.fechaActualizacion = Instant.now();
        this.reintentos = (this.reintentos == null) ? 1 : this.reintentos + 1;
    }

    public void marcarComoTimeout() {
        this.estado = EstadoPago.TIMEOUT;
        this.mensajeError = "Timeout en procesamiento";
        this.fechaActualizacion = Instant.now();
    }

    public boolean excedeMaximoReintentos(int maximo) {
        return reintentos != null && reintentos >= maximo;
    }

    public String obtenerResumen() {
        return String.format("Pago[id=%s, monto=%s %s, estado=%s]", id, monto, moneda, estado);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(String cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Instant getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Instant fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Instant getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    public void setFechaProcesamiento(Instant fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }

    public Instant getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(Instant fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public String getReferenciaExternal() {
        return referenciaExternal;
    }

    public void setReferenciaExternal(String referenciaExternal) {
        this.referenciaExternal = referenciaExternal;
    }

    public String getProcesadorPagoId() {
        return procesadorPagoId;
    }

    public void setProcesadorPagoId(String procesadorPagoId) {
        this.procesadorPagoId = procesadorPagoId;
    }

    public String getSistemaOrigen() {
        return sistemaOrigen;
    }

    public void setSistemaOrigen(String sistemaOrigen) {
        this.sistemaOrigen = sistemaOrigen;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIpOrigen() {
        return ipOrigen;
    }

    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public Integer getReintentos() {
        return reintentos;
    }

    public void setReintentos(Integer reintentos) {
        this.reintentos = reintentos;
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }
}