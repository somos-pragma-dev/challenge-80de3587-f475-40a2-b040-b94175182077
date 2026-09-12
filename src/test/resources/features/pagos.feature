Feature: Automatizacion E2E del Flujo de Pagos

  Como analista de calidad de software
  Necesito automatizar el flujo completo de pagos
  Para garantizar que el sistema procesa correctamente los pagos, maneja errores y mantiene la idempotencia

  Background:
    Given que el sistema de origen esta disponible
    And que el procesador de pagos esta operativo
    And que el sistema de liquidacion esta accesible

  @smoke @idempotencia
  Scenario: Pago exitoso con clave de idempotencia unica
    Given un pago con monto de "5000.00" pesos, origen "CUENTA_001" y destino "CUENTA_002"
    And con concepto "Pago de servicio"
    And genero una clave de idempotencia unica
    When ejecuto el flujo de pago completo
    Then el pago debe ser creado con estado "PENDIENTE"
    And debe ser procesado exitosamente
    And debe ser liquidado correctamente
    And debe mantener su clave de idempotencia

  @smoke @fallback
  Scenario: Reintento de pago por timeout del procesador
    Given un pago con monto de "3000.00" pesos
    And el procesador de pagos responde con timeout
    When reintento el procesamiento del pago
    Then el sistema debe reintentar automaticamente
    And el pago debe completar su procesamiento
    And debe registrar el numero de reintentos

  @regresion @concurrencia
  Scenario: Multiple pagos simultaneos con misma clave de idempotencia
    Given dos pagos concurrentes con la misma clave de idempotencia
    And monto de "1000.00" pesos para ambos
    When ambos pagos se ejecutan al mismo tiempo
    Then solo un pago debe ser procesado
    And el segundo debe ser rechazado por duplicado
    And el mensaje de error debe indicar duplicacion

  @regresion @conectividad
  Scenario: Fallo de conectividad con sistema de liquidacion
    Given un pago procesado exitosamente
    And el sistema de liquidacion no esta disponible
    When intento liquidar el pago
    Then el sistema debe reintentar la liquidacion
    And debe esperar entre reintentos segun la politica configurada
    And debe marcar el pago como pendiente de liquidacion

  @rendimiento @carga
  Scenario: Procesamiento de alto volumen - 5000 pagos por hora
    Given necesito procesar "5000" pagos en una hora
    And cada pago tiene un monto aleatorio entre "100" y "10000" pesos
    When ejecuto el procesamiento en paralelo
    Then todos los pagos deben ser procesados
    And el tiempo total no debe exceder "3600" segundos
    And la tasa efectiva debe ser mayor a "5000" pagos por hora

  @regresion @estados
  Scenario: Verificacion de transiciones de estado validas
    Given un pago en estado "PENDIENTE"
    When proceso el pago exitosamente
    Then el estado debe cambiar a "PROCESADO"
    When liquido el pago
    Then el estado debe cambiar a "LIQUIDADO"
    And la fecha de liquidacion debe ser registrada

  @regresion @errores
  Scenario: Pago rechazado por monto invalido
    Given un pago con monto negativo "-500.00"
    When intento iniciar el pago
    Then el sistema debe rechazar el pago
    And debe devolver un mensaje de error de validacion
    And el pago no debe ser creado

  @smoke @recuperacion
  Scenario: Recuperacion de pago tras fallo temporal
    Given un pago que fallo por error temporal del procesador
    And el pago tiene "2" reintentos disponibles
    When reintento el procesamiento
    Then el pago debe procesarse exitosamente
    And debe actualizar su estado a "PROCESADO"
    And debe registrar los reintentos realizados

  @integration @e2e
  Scenario: Flujo completo de extremo a extremo
    Given un cliente con cuenta origen "CUENTA_100"
    And deseo pagar "15000.00" pesos a beneficiario "CUENTA_200"
    And el concepto es "Pago de租赁"
    When ejecuto el flujo completo de pago
    Then debo recibir una confirmacion de procesamiento
    And debo recibir una confirmacion de liquidacion
    And debo poder consultar el estado final del pago
    And el tiempo de respuesta debe ser menor a "5" segundos