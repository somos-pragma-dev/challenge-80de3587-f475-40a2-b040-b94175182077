# Automatización E2E del Flujo de Pagos

El equipo de pagos de una entidad financiera necesita automatizar el proceso de pagos de extremo a extremo para mejorar la eficiencia y reducir errores manuales. El flujo involucra múltiples sistemas: el sistema de origen de pagos, el procesador de pagos, y el sistema de liquidación. Los pagos deben ser idempotentes, con un umbral de procesamiento de 5000 pagos por hora. El sistema debe manejar errores como timeouts del procesador y fallos de conectividad con el sistema de liquidación.

## Informacion General

| Campo | Valor |
|-------|-------|
| **Tema** | automatizacion e2e del flujo de pagos |
| **Nivel** | senior-l2 |
| **Tipo** | practical |
| **Tiempo estimado** | 4 semanas |

## Fases del Reto

### Fase 0: Configuración del Proyecto

**Objetivo:** Obtener el proyecto base funcional enviando el Código Base a un asistente de IA, que lo analizará, corregirá errores y generará un ZIP listo para usar.

**Tiempo estimado:** 15-30 minutos

**Instrucciones:**

- Asegúrate de tener instalado para ejecutar el proyecto: JDK 17+, Maven 3.9+, IDE con soporte Java.
- Copia todo el contenido del campo **Código Base** de este reto — incluyendo el texto de instrucciones que aparece al inicio.
- Abre un asistente de IA (Claude en claude.ai, ChatGPT o Gemini — se recomienda Claude), pega el contenido copiado en el chat y envíalo.
- El asistente analizará los archivos, corregirá errores y generará un archivo ZIP descargable. Descárgalo y extráelo en la carpeta donde quieras trabajar.
- Ejecuta `mvn compile` en la raíz. Si no hay errores, estás listo.

**Entregable:** El proyecto compila/arranca sin errores.

<details>
<summary>Pistas de conocimiento</summary>

- Copia el Código Base completo incluyendo el texto de instrucciones al inicio — esas instrucciones le indican al asistente exactamente qué hacer con los archivos.
- Si el asistente no genera el ZIP automáticamente al terminar el análisis, escríbele: "genera el ZIP ahora".
- Si el proyecto tiene errores al arrancar, comparte el mensaje de error con el mismo asistente para que lo corrija.

</details>

### Fase 1: Exploración del Flujo Actual

**Objetivo:** Comprender y documentar el flujo de pagos existente.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Identificar los sistemas involucrados en el flujo de pagos.
- Documentar las interacciones entre sistemas y los puntos de falla comunes.
- Evaluar la idempotencia actual y los umbrales de procesamiento.

**Entregable:** Documento descriptivo del flujo de pagos actual.

<details>
<summary>Pistas de conocimiento</summary>

- Mapeo de procesos
- Identificación de puntos críticos

</details>

### Fase 2: Diseño de la Solución Automatizada

**Objetivo:** Diseñar la solución de automatización E2E para el flujo de pagos.

**Tiempo estimado:** 1 semana

**Instrucciones:**

- Proponer un diseño que garantice la idempotencia y maneje los errores identificados.
- Definir los umbrales de procesamiento y las políticas de reintento.
- Identificar las dependencias externas y sus posibles fallos.

**Entregable:** Documento de diseño de la solución automatizada.

<details>
<summary>Pistas de conocimiento</summary>

- Políticas de reintento
- Manejo de errores

</details>

### Fase 3: Implementación y Validación

**Objetivo:** Implementar y validar la solución automatizada.

**Tiempo estimado:** 2 semanas

**Instrucciones:**

- Implementar la solución diseñada, asegurando la idempotencia y el manejo de errores.
- Realizar pruebas de carga para verificar los umbrales de procesamiento.
- Documentar los resultados y cualquier ajuste necesario.

**Entregable:** Solución automatizada implementada y validada, con documentación de resultados.

<details>
<summary>Pistas de conocimiento</summary>

- Pruebas de carga
- Ajustes basados en resultados

</details>

## Dimensiones Evaluadas

- **queEs**: ¿Qué es la automatización E2E del flujo de pagos y por qué es importante?
- **paraQueSirve**: ¿Para qué sirve el diseño propuesto en la solución automatizada?
- **comoSeUsa**: ¿Cómo se usa la solución automatizada para garantizar la idempotencia y manejar errores?
- **erroresComunes**: ¿Cuáles son los errores comunes en el flujo de pagos y cómo los maneja la solución?
- **queDecisionesImplica**: ¿Qué decisiones implica el diseño de la solución automatizada en términos de idempotencia y manejo de errores?

## Criterios de Evaluacion

- Documentación clara del flujo de pagos actual.
- Diseño de solución que garantiza idempotencia y manejo de errores.
- Implementación y validación de la solución con resultados documentados.

## Como trabajar con un asistente de IA

Hay dos caminos, elegi uno:

- **AGENTS.md** (recomendado) — instrucciones nativas del repo. Abri esta carpeta con tu agente local (Claude Code, Cursor, Codex, Copilot, Gemini) y las carga solo. Sabe que archivos faltan y con que comando se verifica, y completa el scaffold escribiendo en disco.
- **PROMPT_MEJORA.md** — para copiar y pegar en un chat (claude.ai, ChatGPT). Devuelve un ZIP con el proyecto. Sirve si no tenes un agente en el IDE.

Ninguno de los dos resuelve las fases del reto: eso es tu trabajo.

## Verificacion

El proyecto esta listo para trabajar cuando este comando corre sin errores:

```bash
mvn clean test-compile
```

---

*Reto generado automaticamente por Challenge Generator - Pragma*
