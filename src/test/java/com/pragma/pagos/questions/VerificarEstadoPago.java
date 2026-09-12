package com.pragma.pagos.questions;

import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.questions.RestQuestion;
import net.serenitybdd.screenplay.rest.questions.CallAnApi;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import com.pragma.pagos.models.Pago;
import com.pragma.pagos.models.Pago.EstadoPago;
import com.pragma.pagos.utils.IdempotenciaHelper;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class VerificarEstadoPago implements Question<Pago> {

    private final String pagoId;
    private final String idempotencyKey;
    private final EstadoPago estadoEsperado;
    private final boolean verificarIdempotencia;
    private final int tiempoMaximoEsperaSegundos;

    public VerificarEstadoPago(String pagoId, String idempotencyKey, EstadoPago estadoEsperado) {
        this(pagoId, idempotencyKey, estadoEsperado, true, 30);
    }

    public VerificarEstadoPago(String pagoId, String idempotencyKey, EstadoPago estadoEsperado, 
                                boolean verificarIdempotencia, int tiempoMaximoEsperaSegundos) {
        this.pagoId = pagoId;
        this.idempotencyKey = idempotencyKey;
        this.estadoEsperado = estadoEsperado;
        this.verificarIdempotencia = verificarIdempotencia;
        this.tiempoMaximoEsperaSegundos = tiempoMaximoEsperaSegundos;
    }

    @Override
    public Pago answeredBy(net.serenitybdd.screenplay.actors.Actor actor) {
        String endpoint = String.format("/api/pagos/%s", pagoId);
        
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Idempotency-Key", idempotencyKey);
        headers.put("X-Request-Timeout", String.valueOf(tiempoMaximoEsperaSegundos));
        
        Pago pago = RestQuestion.about("Consulta de estado de pago")
                .get(endpoint)
                .withHeaders(headers)
                .answeredBy(actor);
        
        if (pago == null) {
            throw new AssertionError("No se recibió respuesta del servicio de pagos para el ID: " + pagoId);
        }
        
        validarEstado(pago);
        
        if (verificarIdempotencia) {
            validarIdempotencia(pago);
        }
        
        return pago;
    }

    private void validarEstado(Pago pago) {
        EstadoPago estadoActual = pago.getEstado();
        MatcherAssert.assertThat(
                "El estado del pago debe ser: " + estadoEsperado + ", pero era: " + estadoActual,
                estadoActual,
                Matchers.is(estadoEsperado)
        );
    }

    private void validarIdempotencia(Pago pago) {
        String idempotencyKeyRecibida = pago.getIdempotencyKey();
        MatcherAssert.assertThat(
                "La clave de idempotencia debe mantenerse consistente",
                idempotencyKeyRecibida,
                Matchers.is(idempotencyKey)
        );
        
        boolean esIdempotente = pago.esIdempotente();
        MatcherAssert.assertThat(
                "El pago debe ser idempotente según su configuración",
                esIdempotente,
                Matchers.is(true)
        );
        
        if (IdempotenciaHelper.tieneDuplicados(pagoId, idempotencyKey)) {
            throw new AssertionError("Se detectaron duplicados para la clave de idempotencia: " + idempotencyKey);
        }
    }

    public static VerificarEstadoPago conEstado(String pagoId, String idempotencyKey, EstadoPago estado) {
        return new VerificarEstadoPago(pagoId, idempotencyKey, estado);
    }

    public static VerificarEstadoPago conEstadoYVerificacionIdempotencia(String pagoId, String idempotencyKey, EstadoPago estado) {
        return new VerificarEstadoPago(pagoId, idempotencyKey, estado, true, 30);
    }

    public static VerificarEstadoPago conTiempoEsperaPersonalizado(String pagoId, String idempotencyKey, 
                                                                     EstadoPago estado, int segundos) {
        return new VerificarEstadoPago(pagoId, idempotencyKey, estado, true, segundos);
    }

    public VerificarEstadoPago sinVerificarIdempotencia() {
        return new VerificarEstadoPago(pagoId, idempotencyKey, estadoEsperado, false, tiempoMaximoEsperaSegundos);
    }
}