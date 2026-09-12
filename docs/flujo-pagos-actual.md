# Flujo de Pagos Actual - Documentación Técnica

## 1. Descripción General del Proceso

El flujo de pagos actual opera como un sistema distribuido que involucra tres componentes principales: el **Sistema de Origen de Pagos** (SOP), el **Procesador de Pagos** (PP), y el **Sistema de Liquidación** (SL). El proceso completo desde la initiate hasta la liquidación de un pago puede variar entre 2 y 15 minutos dependiendo del Processor y las condiciones de red.

El SOP recibe las solicitudes de pago desde múltiples canales (API REST, batch, portal web), valida los datos básicos del pago y genera un identificador único mediante una clave de idempotencia basada en la cuenta origen, monto y timestamp. Una vez validado, el SOP envía la solicitud al PP mediante un llamado síncrono que puede resultar en aprobación, rechazo o timeout.

Cuando el PP responde con aprobación, el SOP registra el pago como "procesado" y deriva la información al SL para su liquidación. El SL procesa la instrucción de liquidación asynchronously y confirma el resultado al SOP mediante callbacks o polling. El flujo completo requiere que cada pago mantenga trazabilidad desde su creación hasta su estado final.

## 2. Sistemas Involucrados y sus Responsabilidades

### 2.1 Sistema de Origen de Pagos (SOP)

El SOP actúa como orquestador central del flujo. Sus responsabilidades incluyen: validación de datos del pago antes de enviar al Processor, generación de claves de idempotencia para evitar duplicados, gestión del ciclo de vida del pago (creado, procesando, aprobado, rechazado, liquidado, fallido, timeout), almacenamiento del estado en base de datos transaccional, y comunicación con el Processor y el SL.

El SOP expone una API REST que acepta pagos con los siguientes datos: identificador único del pago, monto en formato decimal con hasta 2 decimales, código de moneda (COP, USD, EUR), cuenta origen y cuenta destino en formato CLABE o número de cuenta bancaria, concepto o descripción del pago, tipo de pago (ordinario, diferido, recurrente), y canal de origen (web, móvil, batch, API).

### 2.2 Procesador de Pagos (PP)

El Processor es un servicio externo que validates y ejecuta el movimiento de fondos entre cuentas. Su interfaz es síncrona: el SOP envía la solicitud y espera una respuesta inmediata. Sin embargo, el PP puede retornar tres tipos de respuesta: aprobación inmediata con ID de transacción, rechazo con código de error descriptivo, o timeout después de 30 segundos sin respuesta.

El PP no garantiza exactamente-once delivery, por lo que el SOP debe implementar idempotencia para evitar cargos duplicados. Los códigos de rechazo incluyen: fondos insuficientes, cuenta bloqueada, límite excedido, validación de identidad fallida, y error interno del Processor. El PP tiene un límite de procesamiento de aproximadamente 200 transacciones por segundo por cuenta origen.

### 2.3 Sistema de Liquidación (SL)

El SL es responsable de mueve los fondos de la cuenta puente del Processor a la cuenta destino final. Opera de manera asíncrona: el SOP envía la instrucción de liquidación y el SL la procesa en un job programado. El tiempo de liquidación puede variar desde segundos hasta varias horas dependiendo del monto, tipo de pago y horario de operación del SL.

El SL expone una API para consultar el estado de liquidación y recibe callbacks cuando el proceso completa. Los estados de liquidación incluyen: pendiente, en proceso, liquidado, fallido. Cuando la liquidación falla, el SL intenta reintentar automáticamente hasta 3 veces antes de marcar el pago como fallido y notificar al equipo de operaciones.

## 3. Puntos de Falla Identificados

### 3.1 Timeouts del Procesador de Pagos

El punto de falla más crítico ocurre cuando el SOP envía un pago al PP y no recibe respuesta dentro del timeout configurado de 30 segundos. En este escenario, el SOP no puede determinar si el pago fue procesado o no. La implementación actual registra el pago como "timeout" pero no realiza ninguna acción de verificación o recuperación.

Este escenario genera dos problemas graves: posibles duplicados si el pago fue efectivamente procesado pero la respuesta se perdió, y pagos huérfanos que quedan en estado "timeout" sin resolución automática. El equipo de operaciones debe revisar manualmente estos casos, lo cual toma aproximadamente 15 minutos por incidente y genera atrasos en el cierre contable.

### 3.2 Fallos de Conectividad con el Sistema de Liquidación

La comunicación entre el SOP y el SL es menos robusta que con el PP. El SL puede rechazar instrucciones de liquidación por datos inválidos, puede experimentar caídas parciales que impiden el procesamiento de lotes, o puede tener latency variable que afecta los tiempos de liquidación.

Actualmente, si el SL rechaza una instrucción de liquidación, el SOP registra el error pero no reintenta automáticamente. El pago queda en estado "aprobado" pero no liquidado, creando inconsistencias contables. El equipo de operaciones debe intervenir manualmente para reenviar la instrucción o investigar el motivo del rechazo.

### 3.3 Duplicados por Falta de Idempotencia

Aunque el SOP implementa claves de idempotencia, la verificación se realiza solo al momento de crear el pago. Si el usuario intenta pagar nuevamente antes de recibir respuesta del PP, puede generar pagos duplicados. Adicionalmente, si el pago queda en timeout, el usuario puede intentar pagar nuevamente, creando múltiples pagos con el mismo monto y cuenta origen.

La falta de validación rigurosa de idempotencia en el flujo completo (no solo en la creación) ha generado aproximadamente 120 incidentes de duplicados en los últimos 6 meses, con un impacto financiero estimado de $2.5M en ajustes contables.

### 3.4 Limitaciones de Rendimiento

El flujo actual tiene un throughput máximo de aproximadamente 800 pagos por hora, muy por debajo del umbral requerido de 5000 pagos por hora. El bottleneck principal está en la comunicación síncrona con el PP: cada pago requiere un llamado HTTP que puede tomar entre 500ms y 30 segundos. En horas pico, la cola de procesamiento puede acumular miles de pagos pendientes.

Además, la validación de idempotencia en la base de datos genera contención cuando múltiples pagos se procesan concurrentemente. Los locks de base de datos para verificar claves de idempotencia duplicadas aumentan la latencia promedio de 200ms a 1.5 segundos durante picos de tráfico.

## 4. Métricas Actuales del Sistema

| Métrica | Valor Actual | Valor Objetivo | Gap |
|---------|--------------|----------------|-----|
| Throughput máximo | 800 pagos/hora | 5000 pagos/hora | -84% |
| Tiempo promedio de procesamiento | 4.5 segundos | < 2 segundos | +125% |
| Tasa de timeouts | 3.2% | < 0.5% | +540% |
| Tasa de duplicados | 0.8% | < 0.01% | +7900% |
| Tiempo de resolución de incidentes | 45 minutos | < 5 minutos | +800% |
| Disponibilidad del sistema | 97.5% | 99.9% | -2.4% |

## 5. Recomendaciones de Automatización

Para abordar los puntos de falla identificados, la solución de automatización debe implementar: verificación de estado de pagos en timeout mediante consulta al PP antes de marcar como fallido, políticas de reintento exponencial para fallos de comunicación con el SL, validación de idempotencia en todo el flujo incluyendo el paso de procesamiento, monitoreo en tiempo real del throughput y latencia con alertas automáticas, y recuperación automática de pagos en estado inconsistente mediante jobs programados.

La automatización del flujo de pagos no solo reducirá los errores manuales sino que permitirá escalar hasta el umbral de 5000 pagos por hora con monitoreo proactivo de anomalías.