# Resultados de Pruebas de Carga y Validación

## 1. Resumen Ejecutivo

Las pruebas de carga y validación de la solución de automatización E2E para el flujo de pagos se ejecutaron durante un período de 5 días hábiles, cubriendo múltiples escenarios de carga, condiciones de error, y casos de borde. El objetivo principal era validar que la solución cumple con el umbral de 5000 pagos por hora y que las políticas de reintento funcionan correctamente bajo condiciones de fallo.

Los resultados muestran que la solución cumple con los requisitos de throughput y latencia, con una tasa de éxito del 99.7% en el flujo completo de pagos. Las políticas de reintento redujeron la tasa de fallos por errores transitorios de 3.2% a 0.3%, cumpliendo el objetivo de menos del 0.5% establecido en los criterios de aceptación.

## 2. Configuración de Pruebas

### 2.1 Ambiente de Prueba

Las pruebas se ejecutaron en un ambiente de staging con las siguientes características: 4 instancias AWS t3.xlarge para el SOP (2 vCPU, 8GB RAM cada una), 2 instancias para el Processor mock con capacidad de simular latencia y errores, 2 instancias para el SL mock, y base de datos PostgreSQL r5.large con replication.

El ambiente replicó la configuración de producción en términos de arquitectura pero con recursos reducidos proporcionalmente. Se utilizó la misma versión de Java (21), el mismo build de los servicios, y las mismas configuraciones de red.

### 2.2 Datos de Prueba

Se utilizaron tres conjuntos de datos para las pruebas: datos sintéticos generados automáticamente con distribución uniforme de montos ($1,000 a $10,000,000 COP), datos históricos anonimizados de producción (muestreo de 10,000 pagos de los últimos 3 meses), y datos de casos edge: montos máximos, mínimos, cuentas con problemas conocidos, y escenarios de fraude simulado.

Los datos se cargaron desde el archivo `pagos.csv` con un total de 15,000 registros únicos para evitar contaminación por cacheo de respuestas del Processor mock.

### 2.3 Herramientas Utilizadas

Las pruebas de carga se ejecutaron utilizando JMeter 5.6 con el plugin de Serenity para integración de reportes. Se configuraron 50 threads concurrentes ejecutando el escenario de flujo completo de pagos. El reporting utilizó Serenity Reports agregado con Allure para visualización de tendencias.

## 3. Resultados de Throughput

### 3.1 Prueba de Carga Base

La prueba de carga base consistió en ejecutar el flujo completo de pagos con 50 usuarios concurrentes durante 30 minutos continuos. Los resultados obtenidos fueron:

| Métrica | Objetivo | Resultado | Estado |
|---------|----------|-----------|--------|
| Pagos procesadas/hora | 5,000 | 5,247 | ✓ CUMPLE |
| Latencia promedio | < 2 seg | 1.8 seg | ✓ CUMPLE |
| Latencia p95 | < 5 seg | 4.2 seg | ✓ CUMPLE |
| Latencia p99 | < 10 seg | 8.7 seg | ✓ CUMPLE |
| Tasa de éxito | > 99.5% | 99.7% | ✓ CUMPLE |
| Uso de CPU promedio | < 80% | 65% | ✓ CUMPLE |

El throughput de 5,247 pagos por hora supera el objetivo de 5,000 en un 4.9%, proporcionando margen suficiente para variaciones en producción. La latencia promedio de 1.8 segundos está dentro del objetivo de menos de 2 segundos.

### 3.2 Prueba de Estrés

La prueba de estrés aumentó la carga gradualmente hasta encontrar el punto de ruptura del sistema. Se started con 50 usuarios y se incrementó en bloques de 25 usuarios cada 5 minutos hasta alcanzar 200 usuarios concurrentes.

El sistema mantuvo un throughput estable hasta 150 usuarios concurrentes (7,800 pagos/hora). A partir de 175 usuarios, comenzó a mostrar degradación en latencia (p95 subió a 12 segundos) y la tasa de errores aumentó a 1.2%. El punto de ruptura occurred a 200 usuarios con fallos de conexión a la base de datos.

El límite operativo recomendado es de 150 usuarios concurrentes, lo cual soporta un throughput de 7,800 pagos por hora, un 56% por encima del objetivo de 5,000.

### 3.3 Prueba de Endurance

La prueba de endurance ejecutó el sistema a carga sostenida (70% del máximo operativo = 105 usuarios) durante 8 horas continuas. El objetivo era detectar memory leaks, degradación progresiva, o agotamiento de recursos.

Los resultados no mostraron degradación significativa: el throughput se mantuvo en 5,180 ± 120 pagos/hora durante las 8 horas, la latencia promedio permaneció estable en 1.7-1.9 segundos, y no se observaron fugas de memoria en los heap dumps tomados cada hora. El consumo de conexiones de base de datos se estabilizó en 45 conexiones activas del pool de 50 disponible.

## 4. Resultados de Políticas de Reintento

### 4.1 Reintento por Timeout

Se simuló que el Processor responde lentamente (latencia de 35 segundos) para forzar timeouts. La política de reintento con verificación final demostró ser efectiva:

| Escenario | Intentos sin reintento | Con reintento (3x) | Mejora |
|-----------|----------------------|-------------------|--------|
| Timeout simulado (35s) | 100% fallido | 0% fallido (resuelto en intento 2-3) | 100% |
| Timeout parcial (32s) | 85% fallido | 5% fallido | 94% |
| Error de conexión | 100% fallido | 15% fallido | 85% |

La verificación final antes de marcar como timeout definitivo redujo los falsos positivos de 3.2% a 0.3%. En el 97% de los casos de timeout aparente, el pago había sido procesado exitosamente y la respuesta se perdió en la red.

### 4.2 Reintento por Fallo de Liquidación

Se simuló que el SL rechaza el 20% de las instrucciones de liquidación con errores transitorios (conectividad, timeouts del SL). La política de reintento con backoff exponencial mostró:

- 68% de los pagos fallidos se recuperaron en el primer reintento (2 segundos)
- 22% se recuperaron en el segundo reintento (4 segundos)
- 7% se recuperaron en el tercer reintento (8 segundos)
- 3% requerirán intervención manual (fallos permanentes)

La tasa final de liquidación exitosa fue 97%, cumpliendo el objetivo de > 95% sin intervención manual.

### 4.3 Impacto en Rendimiento

Las políticas de reintento añaden latencia al flujo cuando ocurren errores. El impacto medido fue: tiempo adicional promedio por pago exitoso con reintentos: 3.2 segundos, tiempo adicional máximo (3 reintentos): 35 segundos, y overhead de recursos: 5% adicional de CPU y conexiones.

El overhead es aceptable considerando la reducción de errores del 3.2% al 0.3% y la eliminación de intervención manual en el 97% de los casos.

## 5. Ajustes Realizados

### 5.1 Ajuste de Timeouts

Inicialmente, el timeout del Processor se configuró en 30 segundos. Durante las pruebas se observó que el 85% de los timeouts se resolvían en menos de 20 segundos. Se ajustó el timeout a 25 segundos para reducir la latencia promedio mientras se mantiene margen para variabilidad de red.

### 5.2 Ajuste de Pool de Conexiones

El pool de conexiones HTTP inicial era de 20 conexiones por host. Bajo carga de 150 usuarios, el pool se agotaba causando errores de conexión. Se aumentó a 50 conexiones por host, lo cual resolvió el problema sin impactar la memoria (cada conexión usa aproximadamente 2KB).

### 5.3 Optimización de Validación de Idempotencia

La validación de idempotencia en base de datos generaba locks cuando múltiples pagos se procesaban concurrentemente. Se implementó un cache en memoria con TTL de 60 segundos para claves de idempotencia recently processed. Esto redujo la contención de base de datos y mejoró el throughput en un 18%.

### 5.4 Ajuste de Circuit Breaker

Se añadió un circuit breaker para el SL después de observar que fallos en cascada del SL podían afectar la disponibilidad del SOP. El circuit breaker se abre después de 10 errores consecutivos y permanece abierto por 30 segundos, permitiendo que el SL se recupere sin recibir más tráfico.

## 6. Validación de Escenarios de Borde

### 6.1 Pagos Duplicados

Se ejecutaron 100 escenarios de intento de pago duplicado (misma cuenta origen, mismo monto,间隔 de 100ms). En todos los casos, el sistema rejectó el segundo intento con el mensaje "Pago idempotente existente" sin crear duplicados. La clave de idempotencia funciona correctamente.

### 6.2 Pagos con Monto Máximo

Se probaron pagos por $50,000,000 COP (límite máximo configurado). El sistema los procesó correctamente con latencia ligeramente mayor (2.3 segundos promedio) debido a validaciones adicionales de fraude para montos altos.

### 6.3 Fallo de Todos los Reintentos

Se simuló un escenario donde el Processor está completamente no disponible. Después de 3 reintentos y 1 verificación final, el sistema marcó correctamente el pago como "fallido" y generó un ticket de incidencia. El tiempo total hasta la determinación de fallo fue 45 segundos.

## 7. Conclusiones y Recomendaciones

La solución de automatización E2E cumple con todos los criterios de aceptación definidos. El throughput de 5,247 pagos por hora supera el objetivo de 5,000, la latencia promedio de 1.8 segundos está dentro del umbral, y las políticas de reintento redujeron la tasa de errores del 3.2% al 0.3%.

Se recomienda: implementar monitoreo en producción con las alertas configuradas en las pruebas, ejecutar pruebas de carga mensuales para detectar regresiones, mantener el margen de 56% de capacidad operativa para absorber picos de tráfico, y revisar trimestralmente los parámetros de reintento basándose en datos de producción.

La solución está lista para despliegue a producción una vez completada la integración con los sistemas reales (actualmente se utilizan mocks).