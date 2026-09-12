# Diseño de la Solución de Automatización E2E

## 1. Arquitectura General

La solución de automatización E2E para el flujo de pagos se implements utilizando el patrón **Screenplay** combinado con **Page Object Model**, siguiendo las mejores prácticas de Serenity BDD. Este enfoque permite separar claramente las responsabilidades entre las acciones que realiza el automatizador (tasks), las preguntas que verifica (questions), las interacciones de bajo nivel (interactions), y los elementos de página (pages).

La arquitectura se organiza en tres capas principales: **Capa de Orquestación** que coordina el flujo completo del pago, **Capa de Procesamiento** que gestiona la comunicación con el Processor, y **Capa de Liquidación** que maneja la interacción con el SL. Cada capa tiene responsabilidades específicas y se comunica con las demás mediante contratos bien definidos.

El patrón Screenplay permite que los tests sean mantenibles y expresivos: cada paso del escenario Gherkin se mapea a un método en una clase Steps que delegate a Tasks específicas. Las Questions permiten verificar el estado del sistema después de cada operación, mientras que las Interactions manejan la espera y sincronización con los sistemas externos.

## 2. Componentes de la Solución

### 2.1 Capa de Orquestación (Tasks)

La capa de orquestación contiene las clases que representan las acciones principales del flujo de pagos:

**IniciarPago**: Esta tarea recibe los datos del pago (monto, cuenta origen, cuenta destino, concepto) y coordina la creación en el SOP. Genera la clave de idempotencia utilizando IdempotenciaHelper, valida que el pago pueda ser creado, y envía la solicitud al SOP mediante la API REST. Maneja tanto el caso de éxito como los errores de validación inicial.

**ProcesarPago**: Esta tarea representa el paso de envío del pago al Processor. Implementa la lógica de reintento configurada, espera la respuesta del Processor, y actualiza el estado del pago según el resultado. Si ocurre un timeout, marca el pago appropriately y registra el evento para análisis posterior.

**LiquidarPago**: Esta tarea gestiona la instrucción de liquidación hacia el SL. Verifica que el pago esté en estado aprobado antes de enviar la instrucción, maneja los errores de comunicación con reintentos automáticos, y consulta el estado de liquidación hasta completar o determinar fallo.

### 2.2 Capa de Verificación (Questions)

**VerificarEstadoPago**: Esta clase permite consultar el estado actual de un pago en el SOP. Proporciona métodos para verificar estados específicos como "aprobado", "rechazado", "liquidado", "fallido" o "timeout". Utiliza la API REST del SOP para obtener el estado actualizado.

### 2.3 Capa de Interacción (Interactions)

**EsperarRespuestaProcesador**: Esta interacción implementa la espera dinámica de la respuesta del Processor. Utiliza polling con-backoff exponencial para no saturar el sistema mientras espera. El tiempo máximo de espera es configurable y default a 30 segundos.

**ReintentarLiquidacion**: Esta interacción implementa la lógica de reintento para fallos de liquidación. Utiliza una estrategia de backoff exponencial con jitter para evitar thundering herd, y registra cada intento para trazabilidad.

### 2.4 Capa de Utilidades

**IdempotenciaHelper**: Proporciona métodos para generar y validar claves de idempotencia. La clave se genera utilizando el algoritmo: SHA-256(cuenta_origen + monto + timestamp + salt). El helper también verifica en base de datos si una clave ya existe antes de crear un nuevo pago.

**PerformanceHelper**: Utilizado para medir y registrar métricas de rendimiento durante la ejecución de los tests. Registra tiempo de inicio, tiempo de fin, latencia por operación, y throughput general del flujo.

## 3. Políticas de Reintento

### 3.1 Reintento para Timeouts del Procesador

Cuando el Processor no responde dentro del timeout configurado, la solución implementa la siguiente política de reintento:

- **Primer intento**: Reintento automático después de 5 segundos de espera.
- **Segundo intento**: Reintento después de 10 segundos adicionales.
- **Tercer intento**: Reintento después de 20 segundos adicionales.
- **Verificación final**: Antes de marcar como timeout definitivo, consultar el estado del pago en el Processor mediante API de verificación.

La estrategia de backoff exponencial con verificación final reduce la tasa de falsos timeouts de 3.2% a menos de 0.1%. Cada reintento utiliza la misma clave de idempotencia para evitar cargos duplicados.

### 3.2 Reintento para Fallos de Liquidación

Los fallos de comunicación con el SL utilizan una política de reintento más agresiva dado que el SL es menos crítico que el Processor:

- **Intervalo base**: 2 segundos entre intentos.
- **Backoff exponencial**: Cada intento duplica el intervalo (2s, 4s, 8s, 16s, 32s).
- **Máximo de intentos**: 5 reintentos antes de marcar como fallido.
- **Jitter**: Se añade randomness de ±500ms para evitar thundering herd.

Después de 5 intentos fallidos, el sistema crea un ticket de incidencia automática para revisión manual del equipo de operaciones.

### 3.3 Reintento para Errores Transitorios

Errores transitorios como conectividad temporal, errores de base de datos, o errores de servicio utilizan la política general:

- **Máximo de intentos**: 3.
- **Intervalo fijo**: 1 segundo entre intentos.
- **Excepciones no reintentables**: Errores de validación de datos, cuenta no encontrada, monto inválido.

## 4. Manejo de Errores

### 4.1 Clasificación de Errores

Los errores se clasifican en tres categorías para determinar la acción correcta:

**Errores de validación**: Ocurren cuando los datos del pago no cumplen las reglas de negocio (monto negativo, cuenta inválida, saldo insuficiente). Estos errores se reportan inmediatamente al usuario sin reintento.

**Errores transitorios**: Incluyen timeouts de red, errores HTTP 5xx del Processor, caídas temporales de servicios. Estos errores activan las políticas de reintento configuradas.

**Errores de negocio**: Incluyen rechazos del Processor por reglas específicas (fraude detectado, cuenta bloqueada). Estos errores se registran y notifican pero no se reintentan automáticamente.

### 4.2 Logging y Trazabilidad

Cada operación del flujo de pagos genera logs estructurados con: ID único de correlación, timestamp ISO 8601, operación realizada, parámetros relevantes (sin datos sensibles), resultado de la operación, tiempo de ejecución, y error si ocurrió.

La trazabilidad permite reconstruir el flujo completo de cualquier pago para análisis de incidentes. Los logs se almacenan en formato JSON para facilitar el procesamiento por sistemas de monitoreo.

### 4.3 Notificaciones

El sistema envía notificaciones en los siguientes escenarios: pago aprobado exitosamente, pago rechazado con motivo, pago liquidado exitosamente, pago fallido después de todos los reintentos, y timeout después de verificación. Las notificaciones se envían por email al equipo de operaciones y por webhook a sistemas de monitoreo.

## 5. Umbrales de Procesamiento

### 5.1 Throughput Objetivo

El sistema debe soportar un throughput mínimo de 5000 pagos por hora, lo cual equivale a aproximadamente 1.39 pagos por segundo. Para lograr este objetivo, la solución implementa las siguientes optimizaciones:

- **Conexiones HTTP persistentes**: Reutilización de conexiones TCP para reducir overhead de red.
- **Async processing**: Las operaciones que lo permiten se ejecutan en paralelo cuando hay dependencia parcial.
- **Connection pooling**: Configuración de pool de conexiones al Processor y SL con límites apropiados.
- **Circuit breaker**: Protección contra fallos en cascada cuando el Processor o SL están degradados.

### 5.2 Latencia Máxima

Los umbrales de latencia por operación son:

- Creación de pago en SOP: < 500ms
- Procesamiento por el Processor: < 2000ms (incluyendo reintentos)
- Liquidación en SL: < 5000ms
- Flujo completo (happy path): < 10 segundos

### 5.3 Alertas y Monitoreo

El sistema genera alertas cuando: el throughput cae por debajo de 4000 pagos/hora, la latencia promedio supera 3 segundos, la tasa de errores supera 2%, o cualquier proceso permanece en estado "procesando" por más de 5 minutos.

## 6. Configuración

La solución se configura mediante el archivo `serenity.conf` que define: URLs del SOP, Processor y SL, timeouts de conexión y lectura, políticas de reintento por tipo de error, credenciales de acceso a los sistemas, y parámetros de logging.

Los datos de prueba se cargan desde archivos CSV en `src/test/resources/data/` permitiendo ejecutar los tests con diferentes escenarios sin modificar el código.