# AGENTS.md

Instrucciones para el agente de IA que abra este repositorio (Claude Code, Cursor, Codex, Copilot, Gemini). Se cargan solas: no hay que pegar nada en ningun chat.

## Que es este repositorio

Es el codigo base de un reto de aprendizaje de Pragma: **Automatización E2E del Flujo de Pagos**.

| | |
|---|---|
| Tema | automatizacion e2e del flujo de pagos |
| Nivel | senior-l2 |
| Chapter | Calidad de Software |
| Especialidad | Automatizador |
| Stack | Java 21 / Serenity BDD 4.1.0 + Cucumber 7.15.0 + JUnit 5.10.2 |
| Patron arquitectonico | Screenplay + Page Object Model + Capas de servicios (orquestación, procesamiento, liquidación) |
| Tiempo estimado | 4 semanas |

## Tu tarea

Dejar este proyecto en estado **verificable**: que el comando de verificacion corra sin errores. Escribi los archivos en disco, en este repositorio. No generes ZIPs ni archivos adjuntos.

En orden:

1. Corre `mvn clean test-compile` y mira que falla.
2. Completa lo que falte de la lista de abajo: manifiesto de dependencias, punto de entrada, capa de interfaz y las capas del patron declarado.
3. Arregla SOLO los errores que impiden compilar o arrancar.
4. Volve a correr `mvn clean test-compile` hasta que pase.
5. Pará ahí.

## Regla dura: las fases son trabajo del humano

**PROHIBIDO implementar los entregables de las fases.** El valor del reto esta en que la persona los resuelva. Tu trabajo es que tenga un proyecto que arranca; el hueco pedagogico se queda como esta.

No resuelvas nada de esto:

- **Fase 1 — Exploración del Flujo Actual**: Documento descriptivo del flujo de pagos actual.
- **Fase 2 — Diseño de la Solución Automatizada**: Documento de diseño de la solución automatizada.
- **Fase 3 — Implementación y Validación**: Solución automatizada implementada y validada, con documentación de resultados.

Distincion operativa:

- **Arreglar** (si): import faltante, tipo que no existe, dependencia sin declarar, error de sintaxis, archivo referenciado que no existe.
- **No tocar** (no): logica de negocio incompleta, validaciones ausentes, secretos hardcodeados, APIs deprecadas que funcionan, concurrencia insegura, patrones mejorables. Eso es lo que la persona tiene que encontrar.

## Lo que falta y tenes que completar

### 1. Boilerplate del stack (2)

Sin esto el proyecto no compila ni arranca. **Es tu trabajo crearlo**, y no toca nada de lo pedagogico: es andamiaje del stack.

- [ ] **Clase runner (ej. RunCucumberTest.java)** — Sin la clase runner anotada, JUnit no tiene punto de entrada para ejecutar los features.
- [ ] **Step definitions (ej. XSteps.java)** — Sin los step definitions, los pasos Gherkin no tienen implementacion y Cucumber falla con "step undefined".

### 2. Archivos que la arquitectura declara (2 de 18)

La propuesta arquitectonica del reto los lista y no llegaron al repo. Crealos con implementacion real, respetando la capa en la que viven:

- [ ] `src/test/java/com/pragma/pagos/runners/RunPagosTest.java`
- [ ] `src/test/java/com/pragma/pagos/steps/PagosSteps.java`

### 3. Referencias colgando (4)

Salieron de un analisis estatico del codigo que SI esta en el repo. Cada una rompe la compilacion:

- [ ] `src/test/java/com/pragma/pagos/tasks/IniciarPago.java` — `org.hamcrest.CoreMatchers`
      El import org.hamcrest.CoreMatchers pertenece a org.hamcrest.CoreMatchers, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/pragma/pagos/tasks/ProcesarPago.java` — `org.hamcrest.CoreMatchers`
      El import org.hamcrest.CoreMatchers pertenece a org.hamcrest.CoreMatchers, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/pragma/pagos/tasks/LiquidarPago.java` — `org.hamcrest.CoreMatchers`
      El import org.hamcrest.CoreMatchers pertenece a org.hamcrest.CoreMatchers, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- [ ] `src/test/java/com/pragma/pagos/questions/VerificarEstadoPago.java` — `org.hamcrest`
      El import org.hamcrest.MatcherAssert pertenece a org.hamcrest, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).

### Presentes (16)

- `pom.xml`
- `src/test/java/com/pragma/pagos/models/Pago.java`
- `src/test/resources/features/pagos.feature`
- `src/test/java/com/pragma/pagos/tasks/IniciarPago.java`
- `src/test/java/com/pragma/pagos/tasks/ProcesarPago.java`
- `src/test/java/com/pragma/pagos/tasks/LiquidarPago.java`
- `src/test/java/com/pragma/pagos/questions/VerificarEstadoPago.java`
- `src/test/java/com/pragma/pagos/interactions/EsperarRespuestaProcesador.java`
- `src/test/java/com/pragma/pagos/interactions/ReintentarLiquidacion.java`
- `src/test/resources/config/serenity.conf`
- `src/test/resources/data/pagos.csv`
- `src/test/java/com/pragma/pagos/utils/IdempotenciaHelper.java`
- `src/test/java/com/pragma/pagos/utils/PerformanceHelper.java`
- `docs/flujo-pagos-actual.md`
- `docs/diseno-solucion-automatizada.md`
- `docs/resultados-pruebas.md`

### Capas del patron declarado

Cada una tiene que existir como directorio real con al menos un archivo. Codigo plano en la raiz no satisface el patron.

- `src/test/java/com/pragma/pagos`
- `src/test/java/com/pragma/pagos/tasks`
- `src/test/java/com/pragma/pagos/questions`
- `src/test/java/com/pragma/pagos/interactions`
- `src/test/java/com/pragma/pagos/models`
- `src/test/java/com/pragma/pagos/runners`
- `src/test/java/com/pragma/pagos/steps`
- `src/test/resources/features`
- `src/test/resources/data`
- `src/test/resources/config`
- `target/site/serenity`

## Verificacion

```bash
mvn clean test-compile
```

Ese comando pasando es la definicion de "terminado" para vos.

## Convenciones que tenes que respetar

- Un solo ecosistema: no declares librerias de otro lenguaje ni mezcles gestores de paquetes.
- Toda libreria que uses tiene que estar declarada en el manifiesto de dependencias.
- Todo import declarado tiene que usarse; todo tipo usado tiene que existir o venir de una dependencia declarada.
- El patron es **Screenplay + Page Object Model + Capas de servicios (orquestación, procesamiento, liquidación)**: los contratos (interfaces, puertos) los define la capa interna y los implementa la externa, nunca al revés.
- Los archivos que crees llevan implementacion real, no stubs: sin `TODO`, sin cuerpos vacios, sin `// getters y setters`.

## Contexto del candidato

Sirve para calibrar el nivel del codigo, no para resolver las fases.

- Perfil: Chapter Calidad de Software, Especialidad Automatizador, Tecnología Automatizador, Senior
- Brecha que el reto ataca: Necesita fortalecer la practica de Automatizador
- Mision: Liderar la iniciativa de automatizacion e2e del flujo de pagos

---

*Generado por Challenge Generator — Pragma. `README.md` tiene el enunciado completo del reto para la persona. `PROMPT_MEJORA.md` es la variante para pegar en un chat, si se prefiere ese flujo.*
