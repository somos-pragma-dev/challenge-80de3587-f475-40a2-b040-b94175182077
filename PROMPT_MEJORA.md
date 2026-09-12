# Prompt para Mejorar el Codigo Base

Copia y pega el contenido del bloque de abajo en un asistente de IA (Claude, ChatGPT)
para obtener un ZIP con el proyecto completo y arrancable.

Si preferis trabajar en tu editor con un agente local (Claude Code, Cursor, Copilot), usa `AGENTS.md` en vez de este archivo: dice lo mismo pero para que escriba los archivos en disco.

## Las dos reglas que no se negocian

1. **Completa el boilerplate.** Todo lo que el proyecto necesita para compilar y arrancar: manifiesto de dependencias, punto de entrada, configuracion, capa de interfaz, y las capas del patron arquitectonico declarado. Eso es andamiaje y es tu trabajo.
2. **NO resuelvas el reto.** Los entregables de las fases son el trabajo de la persona. El hueco pedagogico se deja como esta: el proyecto arranca, pero lo que el reto pide implementar NO esta implementado.

Dicho de otra forma: si algo impide compilar, arreglalo. Si algo es logica de negocio incompleta, validaciones ausentes, un secreto hardcodeado o un patron mejorable, dejalo exactamente como esta — es lo que la persona tiene que encontrar.

## Lo que le falta a este proyecto

Esto NO lo tenes que adivinar: salio de comparar el proyecto contra la arquitectura declarada del reto y de un analisis estatico del codigo. Completalo TODO.

### Boilerplate del stack que falta

Sin esto no compila ni arranca. Es andamiaje, no toca nada de lo pedagogico:

- **Clase runner (ej. RunCucumberTest.java)** — Sin la clase runner anotada, JUnit no tiene punto de entrada para ejecutar los features.
- **Step definitions (ej. XSteps.java)** — Sin los step definitions, los pasos Gherkin no tienen implementacion y Cucumber falla con "step undefined".

### Archivos que la arquitectura del reto declara y no estan

Creálos con implementacion real, en la capa que les corresponde:

- `src/test/java/com/pragma/pagos/runners/RunPagosTest.java`
- `src/test/java/com/pragma/pagos/steps/PagosSteps.java`

### Referencias colgando en el codigo que si esta

Cada una rompe la compilacion:

- `src/test/java/com/pragma/pagos/tasks/IniciarPago.java` — `org.hamcrest.CoreMatchers`: El import org.hamcrest.CoreMatchers pertenece a org.hamcrest.CoreMatchers, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/pragma/pagos/tasks/ProcesarPago.java` — `org.hamcrest.CoreMatchers`: El import org.hamcrest.CoreMatchers pertenece a org.hamcrest.CoreMatchers, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/pragma/pagos/tasks/LiquidarPago.java` — `org.hamcrest.CoreMatchers`: El import org.hamcrest.CoreMatchers pertenece a org.hamcrest.CoreMatchers, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).
- `src/test/java/com/pragma/pagos/questions/VerificarEstadoPago.java` — `org.hamcrest`: El import org.hamcrest.MatcherAssert pertenece a org.hamcrest, pero ninguna dependencia declarada en el pom.xml cubre ese paquete. Falta agregar la dependencia o el import esta mal (libreria equivocada).

## Como saber que terminaste

```bash
mvn clean test-compile
```

Ese comando corriendo sin errores es la definicion de "listo".

---

```
## Briefing del reto (autoridad)
Este bloque manda sobre los archivos adjuntos. El stack y el rol salen de AQUÍ, no de un topic genérico ni de markdown placeholder.

### Perfil
Chapter Calidad de Software, Especialidad Automatizador, Tecnología Automatizador, Senior

### Brecha de conocimiento
Necesita fortalecer la practica de Automatizador

### Misión / candidato
Liderar la iniciativa de automatizacion e2e del flujo de pagos

### Reto
- Tema: automatizacion e2e del flujo de pagos
- Seniority: senior-l2
- Tipo: practical
- Título: Automatización E2E del Flujo de Pagos
- Tiempo estimado: 4 semanas

### Fases (trabajo del HUMANO — PROHIBIDO completarlas)
No implementes estos entregables. Dejalos como hueco pedagógico. El asistente solo materializa el proyecto arrancable para que el participante pueda trabajar.
- Fase 1: Exploración del Flujo Actual — objetivo: Comprender y documentar el flujo de pagos existente. — entregable (NO resolver): Documento descriptivo del flujo de pagos actual.
- Fase 2: Diseño de la Solución Automatizada — objetivo: Diseñar la solución de automatización E2E para el flujo de pagos. — entregable (NO resolver): Documento de diseño de la solución automatizada.
- Fase 3: Implementación y Validación — objetivo: Implementar y validar la solución automatizada. — entregable (NO resolver): Solución automatizada implementada y validada, con documentación de resultados.

Eres un asistente experto en análisis, corrección y generación de archivos de cualquier tipo:
código fuente, documentación, hojas de cálculo, documentos Word, configuraciones, entre otros.
Voy a enviarte una cadena de texto que contiene uno o más archivos. Cada archivo está delimitado por un marcador con el siguiente formato:
// === ARCHIVO: ruta/del/archivo.extension ===
o también puede aparecer como:
## === ARCHIVO: ruta/del/archivo.extension ===
Lo que sigue al marcador puede ser:

El contenido real del archivo (código, texto, YAML, etc.)
Una descripción en lenguaje natural de lo que debe contener el archivo


TU TAREA
PASO 0 — ¿Esto es un proyecto o una carcasa?
Antes de extraer archivos, leé el Briefing (si está) y diagnosticá el adjunto.

Es CARCASA si ocurre CUALQUIERA de estas:
- No hay manifiesto de dependencias del stack del briefing (manifest.json de VTEX IO / package.json / pom.xml / build.gradle / requirements.txt / go.mod / *.tf / *.csproj, según corresponda)
- Hay un "binario" que en realidad es un comentario ("no puede ser mostrado como texto plano", placeholder .fig/.docx vacío)
- Los markdowns ya completan entregables de fases posteriores ("se implementó fade-in", lista de áreas ya resuelta)

Si es CARCASA:
- MATERIALIZÁ un proyecto que arranca en el stack del briefing (VTEX IO Store Framework, Angular, Terraform, pytest, Nest, etc.). Incluí manifiesto, punto de entrada y capa de interfaz reales.
- NO copies los markdowns de "solución" como si fueran el producto. Son ruido de generación.
- NO resuelvas las fases del briefing (están marcadas PROHIBIDO). Dejá el hueco pedagógico: el flujo existe, las microinteracciones/calidad/infra que el reto pide NO están hechas.
- Después seguí al PASO 5 (ZIP).

Si es un proyecto REAL (manifiesto + código que compila o arranca):
- Seguí PASO 1 en adelante. 🔴 compilación sí. 🟡 pedagógico no.

PASO 1 — Detección y extracción
Identifica todos los archivos presentes en la cadena. Para cada archivo extrae:

Su ruta completa (ej: src/main/java/com/pragma/Service.java)
Su contenido o descripción

PASO 2 — Clasificación por tipo
Clasifica cada archivo en una de estas categorías:
A) Código fuente (Java, Python, TypeScript, JavaScript, Kotlin, etc.)
B) Configuración / documentación (YAML, properties, Markdown, JSON, txt, etc.)
C) Excel (.xlsx, .xls, .csv)
D) Word (.docx, .doc)
E) Otro tipo de archivo binario o especial
PASO 3 — Clasificación de errores en código fuente

Objetivo prioritario: que el proyecto compile. No corrijas flujo de negocio ni lógica funcional.

Antes de modificar cualquier archivo de código fuente, clasifica cada problema encontrado en una de estas dos categorías:
🔴 ERROR DE COMPILACIÓN — corregir siempre
Son errores que impiden que el proyecto arranque, sin valor pedagógico:

Import faltante o incorrecto
Clase, método o variable referenciada que no existe en ningún archivo del proyecto
Error de sintaxis
Anotación con atributos inválidos
Dependencia ausente en pom.xml, package.json, etc.
Archivo referenciado que no existe y debe ser creado con implementación mínima

→ CORREGIR estos errores.
🟡 PROBLEMA FUNCIONAL O DE CALIDAD — preservar siempre
Son problemas que no impiden compilar. Pueden ser intencionales para el aprendizaje:

Clave secreta hardcodeada ("secret", "password123")
API deprecada que funciona pero tiene reemplazo moderno
Lógica de negocio incorrecta o incompleta
Código redundante o de baja legibilidad
Falta de validaciones en flujo de negocio
Patrones de diseño incorrectos pero funcionales
Concurrencia no segura
Configuración funcional pero no óptima

→ PRESERVAR tal cual. No corregir, no mejorar, no comentar.
PASO 4 — Procesamiento según tipo de archivo
Tipo A — Código fuente
Aplica únicamente las correcciones clasificadas como 🔴 ERROR DE COMPILACIÓN.
No alteres ningún elemento clasificado como 🟡 PROBLEMA FUNCIONAL O DE CALIDAD.
Si falta un archivo referenciado, créalo con la implementación mínima necesaria para compilar.
Tipo B — Configuración / documentación
Extrae el contenido tal cual, sin modificaciones salvo errores evidentes de sintaxis
(ej: YAML mal indentado).
Tipo C — Excel (.xlsx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un archivo Excel funcional con:

Fila de encabezados en negrita con color de fondo distintivo
Columnas con ancho ajustado al contenido
Tipos de dato correctos por columna
Validaciones si la descripción lo indica
Hojas nombradas descriptivamente si hay más de una
Filas de ejemplo si no hay datos reales

Tipo D — Word (.docx)
Si viene con contenido real, genera el archivo respetando ese contenido.
Si viene con descripción en lenguaje natural, genera un documento Word funcional con:

Estilos de título (Título 1, Título 2) para jerarquía de secciones
Fuente legible (Calibri o equivalente), tamaño 11-12pt para cuerpo
Márgenes estándar
Tabla de contenido si tiene múltiples secciones
Tablas con encabezados en negrita si aplica

Tipo E — Otro
Genera el archivo con el contenido o estructura más apropiada según la descripción.
PASO 5 — Exportación en ZIP
Empaqueta todos los archivos en un único archivo ZIP descargable respetando exactamente
la estructura de rutas indicada por los marcadores.
El ZIP debe incluir:

Archivos de código con únicamente los errores de compilación corregidos
Archivos de configuración y documentación sin cambios
Archivos nuevos creados para resolver dependencias de compilación faltantes
Archivos Excel y Word generados desde descripción

IMPORTANTE: El ZIP debe estar listo para descargar al finalizar. No preguntes si el usuario
quiere generarlo. Simplemente genera el archivo y proporciona el enlace de descarga; No debes desplegar en el chat el resumen de lo que arreglaste al Zip, solo entregalo.

REGLAS IMPORTANTES

No omitas ningún archivo aunque no tenga errores ni modificaciones
Respeta los nombres y rutas exactas indicadas por los marcadores
Si un archivo no tiene marcador claro, infiere el nombre desde su contenido
Si la cadena contiene solo documentación, placeholders o binarios fake, NO la reproduzcas:
aplicá PASO 0 (materializar el proyecto del briefing). Reproducir la carcasa es un fallo.
No agregues texto después del enlace de descarga del ZIP
No preguntes si el usuario quiere el ZIP: simplemente generalo siempre
Si detectas que falta un archivo de configuración necesario para compilar
(pom.xml, package.json, requirements.txt, build.gradle, etc.), créalo e inclúyelo
inferiendo su contenido desde los imports y frameworks detectados en el código
Nunca corrijas problemas 🟡 aunque parezcan obvios o fáciles de mejorar.
El participante que recibirá este proyecto los debe encontrar y resolver él mismo.


INPUT
Aquí está la cadena con los archivos:

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.pragma</groupId>
    <artifactId>pagos-e2e-automation</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>Automatización E2E Flujo de Pagos</name>
    <description>Suite de pruebas automatizadas para el flujo de pagos de extremo a extremo</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <serenity.version>4.1.0</serenity.version>
        <cucumber.version>7.15.0</cucumber.version>
        <junit.version>5.10.2</junit.version>
        <selenium.version>4.22.0</selenium.version>
        <rest-assured.version>5.4.0</rest-assured.version>
        <lombok.version>1.18.30</lombok.version>
    </properties>

    <parent>
        <groupId>net.serenity-bdd</groupId>
        <artifactId>serenity-parent</artifactId>
        <version>4.1.0</version>
        <relativePath/>
    </parent>

    <dependencies>
        <dependency>
            <groupId>net.serenity-bdd</groupId>
            <artifactId>serenity-core</artifactId>
            <version>${serenity.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>net.serenity-bdd</groupId>
            <artifactId>serenity-cucumber</artifactId>
            <version>${serenity.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit</artifactId>
            <version>${cucumber.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>${rest-assured.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.12.1</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${lombok.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <includes>
                        <include>**/*Test.java</include>
                        <include>**/*Runner.java</include>
                    </includes>
                    <parallel>classes</parallel>
                    <threadCount>4</threadCount>
                    <perCoreThreadCount>true</perCoreThreadCount>
                    <useUnlimitedThreads>false</useUnlimitedThreads>
                </configuration>
            </plugin>
            <plugin>
                <groupId>net.serenity-bdd</groupId>
                <artifactId>serenity-maven-plugin</artifactId>
                <version>${serenity.version}</version>
                <executions>
                    <execution>
                        <id>serenity-reports</id>
                        <phase>post-integration-test</phase>
                        <goals>
                            <goal>aggregate</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <reportsDirectory>target/site/serenity</reportsDirectory>
                    <outputDirectory>target/site/serenity</outputDirectory>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <includes>
                        <include>**/*Test.java</include>
                        <include>**/*Runner.java</include>
                    </includes>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>integration-test</goal>
                            <goal>verify</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/test/java/com/pragma/pagos/models/Pago.java ===
package com.pragma.pagos.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        PENDIENTE("PENDIENTE"),
        INICIADO("INICIADO"),
        PROCESANDO("PROCESANDO"),
        APROBADO("APROBADO"),
        RECHAZADO("RECHAZADO"),
        LIQUIDADO("LIQUIDADO"),
        FALLIDO("FALLIDO"),
        TIMEOUT("TIMEOUT"),
        CANCELADO("CANCELADO");

        private final String valor;

        EstadoPago(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }

    public enum TipoPago {
        TRANSFERENCIA,
        PAGO_FACTURA,
        RECARGA,
        DISPERSION,
        DEVOLUCION
    }

    public static Pago crearNuevo(BigDecimal monto, String cuentaOrigen, String cuentaDestino, String concepto) {
        return Pago.builder()
                .id(UUID.randomUUID().toString())
                .idempotencyKey(generarIdempotencyKey(cuentaOrigen, monto))
                .monto(monto)
                .moneda("COP")
                .cuentaOrigen(cuentaOrigen)
                .cuentaDestino(cuentaDestino)
                .concepto(concepto)
                .estado(EstadoPago.PENDIENTE)
                .fechaCreacion(Instant.now())
                .reintentos(0)
                .tipoPago(TipoPago.TRANSFERENCIA)
                .build();
    }

    public static String generarIdempotencyKey(String cuentaOrigen, BigDecimal monto) {
        return UUID.nameUUIDFromBytes((cuentaOrigen + monto.toString() + System.currentTimeMillis()).getBytes()).toString();
    }

    public boolean esIdempotente() {
        return idempotencyKey != null && !idempotencyKey.isBlank();
    }

    public boolean puedeReintentarse() {
        return estado == EstadoPago.FALLIDO || 
               estado == EstadoPago.TIMEOUT || 
               estado == EstadoPago.PENDIENTE;
    }

    public boolean estaLiquidado() {
        return estado == EstadoPago.LIQUIDADO;
    }

    public boolean requiereProcesamiento() {
        return estado == EstadoPago.PENDIENTE || estado == EstadoPago.INICIADO;
    }

    public void marcarComoProcesando() {
        this.estado = EstadoPago.PROCESANDO;
        this.fechaActualizacion = Instant.now();
    }

    public void marcarComoAprobado(String procesadorId) {
        this.estado = EstadoPago.APROBADO;
        this.procesadorPagoId = procesadorId;
        this.fechaProcesamiento = Instant.now();
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
        this.reintentos = (this.reintentos == null ? 0 : this.reintentos) + 1;
        this.fechaActualizacion = Instant.now();
    }

    public void marcarComoTimeout() {
        this.estado = EstadoPago.TIMEOUT;
        this.mensajeError = "Timeout del procesador de pagos";
        this.reintentos = (this.reintentos == null ? 0 : this.reintentos) + 1;
        this.fechaActualizacion = Instant.now();
    }

    public boolean excedeMaximoReintentos(int maximo) {
        return this.reintentos != null && this.reintentos >= maximo;
    }

    public String obtenerResumen() {
        return String.format("Pago[id=%s, monto=%s %s, estado=%s, origen=%s, destino=%s]",
                id, monto, moneda, estado, cuentaOrigen, cuentaDestino);
    }
}

// === ARCHIVO: src/test/resources/features/pagos.feature ===
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

// === ARCHIVO: src/test/java/com/pragma/pagos/tasks/IniciarPago.java ===
package com.pragma.pagos.tasks;

import com.pragma.pagos.models.Pago;
import com.pragma.pagos.utils.IdempotenciaHelper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

public class IniciarPago implements Task {

    private static final Logger logger = LoggerFactory.getLogger(IniciarPago.class);
    private static final String SISTEMA_ORIGEN_ENDPOINT = "/api/v1/pagos/iniciar";
    private static final int TIMEOUT_SECONDS = 30;

    private final BigDecimal monto;
    private final String cuentaOrigen;
    private final String cuentaDestino;
    private final String concepto;
    private final String canal;
    private final String usuario;

    private Pago pagoCreado;

    public IniciarPago(BigDecimal monto, String cuentaOrigen, String cuentaDestino, String concepto) {
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.concepto = concepto;
        this.canal = "AUTOMATIZADO";
        this.usuario = "QA_AUTOMATION";
    }

    public static IniciarPago conDatos(BigDecimal monto, String cuentaOrigen, String cuentaDestino, String concepto) {
        return new IniciarPago(monto, cuentaOrigen, cuentaDestino, concepto);
    }

    public IniciarPago conCanal(String canal) {
        this.canal = canal;
        return this;
    }

    public IniciarPago conUsuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info("Iniciando proceso de pago - Monto: {}, Origen: {}, Destino: {}", monto, cuentaOrigen, cuentaDestino);

        String idempotencyKey = IdempotenciaHelper.generarIdempotencyKey(cuentaOrigen, monto);
        logger.debug("Clave de idempotencia generada: {}", idempotencyKey);

        Map<String, Object> payload = construirPayloadPago(idempotencyKey);
        logger.debug("Payload construido para iniciar pago: {}", payload);

        actor.attemptsTo(
            Post.to(SISTEMA_ORIGEN_ENDPOINT)
                .with(request -> request
                    .header("Content-Type", "application/json")
                    .header("X-Idempotency-Key", idempotencyKey)
                    .header("X-Request-Timeout", String.valueOf(TIMEOUT_SECONDS))
                    .body(payload)
                    .relaxedHTTPSValidation()
                )
        );

        Response respuesta = actor.asksFor(LastResponse.received());
        int statusCode = respuesta.getStatusCode();
        logger.info("Respuesta del sistema de origen - Status: {}, Body: {}", statusCode, respuesta.getBody().asString());

        actor.should(
            seeThat("Código de estado de respuesta",
                response -> statusCode,
                anyOf(is(200), is(201), is(202))
            )
        );

        Pago pago = mapearRespuestaAPago(respuesta, idempotencyKey);
        this.pagoCreado = pago;

        logger.info("Pago iniciado exitosamente - ID: {}, Estado: {}", pago.getId(), pago.getEstado());
        actor.remember("pagoActual", pago);
        actor.remember("idempotencyKey", idempotencyKey);
    }

    private Map<String, Object> construirPayloadPago(String idempotencyKey) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("idempotencyKey", idempotencyKey);
        payload.put("monto", monto);
        payload.put("moneda", "COP");
        payload.put("cuentaOrigen", cuentaOrigen);
        payload.put("cuentaDestino", cuentaDestino);
        payload.put("concepto", concepto);
        payload.put("canal", canal);
        payload.put("usuario", usuario);
        payload.put("ipOrigen", "192.168.1.100");
        payload.put("tipoPago", "TRANSFERENCIA");
        return payload;
    }

    private Pago mapearRespuestaAPago(Response respuesta, String idempotencyKey) {
        Pago pago = Pago.crearNuevo(monto, cuentaOrigen, cuentaDestino, concepto);
        pago.setIdempotencyKey(idempotencyKey);
        pago.setCanal(canal);
        pago.setUsuario(usuario);
        pago.setIpOrigen("192.168.1.100");
        pago.setFechaCreacion(Instant.now());

        if (respuesta.getStatusCode() == 200 || respuesta.getStatusCode() == 201) {
            String idPago = respuesta.jsonPath().getString("id");
            String estado = respuesta.jsonPath().getString("estado");
            String referencia = respuesta.jsonPath().getString("referenciaExternal");

            pago.setId(idPago);
            pago.setEstado(Pago.EstadoPago.valueOf(estado.toUpperCase()));
            pago.setReferenciaExternal(referencia);
            pago.setSistemaOrigen("SISTEMA_ORIGEN");

            if (estado.equalsIgnoreCase("PROCESANDO")) {
                pago.marcarComoProcesando();
            } else if (estado.equalsIgnoreCase("APROBADO")) {
                pago.marcarComoAprobado("PROCESADOR_DEFAULT");
            }
        } else if (respuesta.getStatusCode() == 202) {
            String idPago = respuesta.jsonPath().getString("id");
            pago.setId(idPago);
            pago.setEstado(Pago.EstadoPago.PENDIENTE);
            pago.setSistemaOrigen("SISTEMA_ORIGEN");
        }

        return pago;
    }

    public Pago getPagoCreado() {
        return pagoCreado;
    }
}

// === ARCHIVO: src/test/java/com/pragma/pagos/tasks/ProcesarPago.java ===
package com.pragma.pagos.tasks;

import com.pragma.pagos.models.Pago;
import com.pragma.pagos.questions.VerificarEstadoPago;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

public class ProcesarPago implements Task {

    private static final Logger logger = LoggerFactory.getLogger(ProcesarPago.class);
    private static final String PROCESADOR_ENDPOINT = "/api/v1/procesador/pagar";
    private static final int MAX_REINTENTOS = 3;
    private static final long TIMEOUT_MS = 45000;
    private static final long RETRY_DELAY_MS = 5000;

    private final Pago pago;
    private String procesadorId;
    private int reintentosActual = 0;

    public ProcesarPago(Pago pago) {
        this.pago = pago;
    }

    public static ProcesarPago enElProcesador(Pago pago) {
        return new ProcesarPago(pago);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info("Iniciando procesamiento de pago - ID: {}, Monto: {}", pago.getId(), pago.getMonto());

        if (!pago.requiereProcesamiento()) {
            logger.warn("El pago no requiere procesamiento - Estado actual: {}", pago.getEstado());
            return;
        }

        boolean procesamientoExitoso = intentarProcesamiento(actor);

        if (!procesamientoExitoso && reintentosActual < MAX_REINTENTOS) {
            logger.warn("Procesamiento inicial fallido, iniciando reintentos. Reintentos restantes: {}",
                MAX_REINTENTOS - reintentosActual);
            procesamientoExitoso = ejecutarConReintentos(actor);
        }

        if (!procesamientoExitoso) {
            logger.error("Pago fallido después de {} intentos", MAX_REINTENTOS);
            pago.marcarComoTimeout();
            throw new RuntimeException("Pago fallido tras reintentos: " + pago.getId());
        }

        verificarEstadoFinal(actor);
    }

    private <T extends Actor> boolean intentarProcesamiento(T actor) {
        try {
            Map<String, Object> payload = construirPayloadProcesamiento();
            logger.debug("Enviando pago al procesador: {}", payload);

            actor.attemptsTo(
                Post.to(PROCESADOR_ENDPOINT)
                    .with(request -> request
                        .header("Content-Type", "application/json")
                        .header("X-Idempotency-Key", pago.getIdempotencyKey())
                        .header("X-Request-Timeout", String.valueOf(TIMEOUT_MS))
                        .body(payload)
                        .relaxedHTTPSValidation()
                        .timeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    )
            );

            Response respuesta = actor.asksFor(LastResponse.received());
            int statusCode = respuesta.getStatusCode();
            String responseBody = respuesta.getBody().asString();
            logger.info("Respuesta del procesador - Status: {}, Body: {}", statusCode, responseBody);

            if (statusCode == 200 || statusCode == 201) {
                String resultado = respuesta.jsonPath().getString("resultado");
                String idProcesador = respuesta.jsonPath().getString("procesadorId");
                String mensaje = respuesta.jsonPath().getString("mensaje");

                if ("APROBADO".equalsIgnoreCase(resultado) || "SUCCESS".equalsIgnoreCase(resultado)) {
                    this.procesadorId = idProcesador;
                    pago.marcarComoAprobado(idProcesador);
                    pago.setFechaProcesamiento(Instant.now());
                    logger.info("Pago aprobado por procesador - ID: {}", idProcesador);
                    return true;
                } else if ("PENDIENTE".equalsIgnoreCase(resultado)) {
                    logger.info("Pago en estado pendiente, esperando procesamiento asíncrono");
                    return esperarProcesamientoAsincrono(actor);
                } else {
                    String mensajeRechazo = mensaje != null ? mensaje : "Rechazado por procesador";
                    pago.marcarComoRechazado(mensajeRechazo);
                    logger.error("Pago rechazado por procesador: {}", mensajeRechazo);
                    return false;
                }
            } else if (statusCode == 408 || statusCode == 504) {
                logger.warn("Timeout del procesador - Status: {}", statusCode);
                pago.marcarComoTimeout();
                return false;
            } else if (statusCode >= 500) {
                logger.error("Error del servidor del procesador - Status: {}", statusCode);
                return false;
            }

            return false;
        } catch (Exception e) {
            logger.error("Excepción durante procesamiento: {}", e.getMessage(), e);
            return false;
        }
    }

    private <T extends Actor> boolean ejecutarConReintentos(T actor) {
        for (int i = reintentosActual; i < MAX_REINTENTOS; i++) {
            reintentosActual = i + 1;
            logger.info("Ejecutando reintento {} de {}", reintentosActual, MAX_REINTENTOS);

            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Reintento interrumpido");
            }

            if (intentarProcesamiento(actor)) {
                return true;
            }

            logger.warn("Reintento {} fallido", reintentosActual);
        }
        return false;
    }

    private <T extends Actor> boolean esperarProcesamientoAsincrono(T actor) {
        logger.info("Esperando procesamiento asíncrono del pago");
        long tiempoInicio = System.currentTimeMillis();
        long tiempoMaximo = TIMEOUT_MS;

        while (System.currentTimeMillis() - tiempoInicio < tiempoMaximo) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            Boolean estadoActualizado = actor.asksFor(VerificarEstadoPago.delPago(pago.getId()));
            if (estadoActualizado != null && estadoActualizado) {
                logger.info("Pago procesado asíncronamente de forma exitosa");
                return true;
            }
        }

        logger.warn("Timeout esperando procesamiento asíncrono");
        return false;
    }

    private <T extends Actor> void verificarEstadoFinal(T actor) {
        actor.should(
            seeThat("El pago debe estar aprobado",
                actor1 -> pago.getEstado() == Pago.EstadoPago.APROBADO,
                is(true)
            )
        );
        logger.info("Verificación de estado final completada - Estado: {}", pago.getEstado());
    }

    private Map<String, Object> construirPayloadProcesamiento() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("pagoId", pago.getId());
        payload.put("idempotencyKey", pago.getIdempotencyKey());
        payload.put("monto", pago.getMonto());
        payload.put("moneda", pago.getMoneda());
        payload.put("cuentaOrigen", pago.getCuentaOrigen());
        payload.put("cuentaDestino", pago.getCuentaDestino());
        payload.put("concepto", pago.getConcepto());
        payload.put("referenciaExternal", pago.getReferenciaExternal());
        payload.put("sistemaOrigen", pago.getSistemaOrigen());
        payload.put("timestamp", Instant.now().toString());
        return payload;
    }

    public String getProcesadorId() {
        return procesadorId;
    }

    public int getReintentosActual() {
        return reintentosActual;
    }
}

// === ARCHIVO: src/test/java/com/pragma/pagos/tasks/LiquidarPago.java ===
package com.pragma.pagos.tasks;

import com.pragma.pagos.models.Pago;
import com.pragma.pagos.questions.VerificarEstadoPago;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

public class LiquidarPago implements Task {

    private static final Logger logger = LoggerFactory.getLogger(LiquidarPago.class);
    private static final String LIQUIDACION_ENDPOINT = "/api/v1/liquidacion/registrar";
    private static final int MAX_REINTENTOS_CONECTIVIDAD = 5;
    private static final long TIMEOUT_MS = 30000;
    private static final long RETRY_DELAY_MS = 3000;
    private static final long MAX_ESPERA_CONECTIVIDAD_MS = 60000;

    private final Pago pago;
    private String referenciaLiquidacion;
    private int intentosConectividad = 0;

    public LiquidarPago(Pago pago) {
        this.pago = pago;
    }

    public static LiquidarPago enElSistemaDeLiquidacion(Pago pago) {
        return new LiquidarPago(pago);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info("Iniciando liquidación de pago - ID: {}, Estado actual: {}", pago.getId(), pago.getEstado());

        if (!pago.estaLiquidado()) {
            logger.warn("El pago no está en estado de liquidación - Estado: {}", pago.getEstado());
            if (pago.getEstado() != Pago.EstadoPago.APROBADO) {
                throw new IllegalStateException("No se puede liquidar un pago que no está aprobado. Estado: " + pago.getEstado());
            }
        }

        boolean liquidado = false;
        Exception ultimaExcepcion = null;

        for (int intento = 1; intento <= MAX_REINTENTOS_CONECTIVIDAD; intento++) {
            try {
                liquidado = intentarLiquidacion(actor);
                if (liquidado) {
                    break;
                }
            } catch (Exception e) {
                ultimaExcepcion = e;
                logger.error("Error de conectividad en intento {}: {}", intento, e.getMessage());
                intentosConectividad = intento;

                if (intento < MAX_REINTENTOS_CONECTIVIDAD) {
                    logger.info("Esperando antes del siguiente intento de conectividad...");
                    esperarReintentoConectividad();
                }
            }
        }

        if (!liquidado && ultimaExcepcion != null) {
            logger.error("Liquidación fallida después de {} intentos por problemas de conectividad", MAX_REINTENTOS_CONECTIVIDAD);
            pago.marcarComoFallido("Fallos de conectividad con sistema de liquidación: " + ultimaExcepcion.getMessage());
            throw new RuntimeException("No se pudo liquidar el pago por problemas de conectividad", ultimaExcepcion);
        }

        verificarLiquidacionExitosa(actor);
    }

    private <T extends Actor> boolean intentarLiquidacion(T actor) {
        logger.debug("Intentando liquidación del pago {}", pago.getId());

        Map<String, Object> payload = construirPayloadLiquidacion();
        logger.debug("Payload de liquidación: {}", payload);

        try {
            actor.attemptsTo(
                Post.to(LIQUIDACION_ENDPOINT)
                    .with(request -> request
                        .header("Content-Type", "application/json")
                        .header("X-Idempotency-Key", generarClaveLiquidacion())
                        .header("X-Request-Timeout", String.valueOf(TIMEOUT_MS))
                        .body(payload)
                        .relaxedHTTPSValidation()
                        .timeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    )
            );

            Response respuesta = actor.asksFor(LastResponse.received());
            int statusCode = respuesta.getStatusCode();
            String responseBody = respuesta.getBody().asString();
            logger.info("Respuesta del sistema de liquidación - Status: {}, Body: {}", statusCode, responseBody);

            if (statusCode == 200 || statusCode == 201) {
                String estadoLiquidacion = respuesta.jsonPath().getString("estado");
                this.referenciaLiquidacion = respuesta.jsonPath().getString("referenciaLiquidacion");

                if ("LIQUIDADO".equalsIgnoreCase(estadoLiquidacion) || "CONFIRMADO".equalsIgnoreCase(estadoLiquidacion)) {
                    pago.marcarComoLiquidado();
                    pago.setFechaLiquidacion(Instant.now());
                    logger.info("Pago liquidado exitosamente - Referencia: {}", referenciaLiquidacion);
                    return true;
                } else if ("PENDIENTE".equalsIgnoreCase(estadoLiquidacion)) {
                    logger.info("Liquidación en proceso asíncrono, esperando confirmación");
                    return esperarConfirmacionLiquidacion(actor);
                }
            } else if (statusCode >= 500) {
                logger.error("Error del servidor de liquidación - Status: {}", statusCode);
                throw new RuntimeException("Error de conectividad con sistema de liquidación: HTTP " + statusCode);
            } else if (statusCode == 409 || statusCode == 422) {
                String mensaje = respuesta.jsonPath().getString("mensaje");
                if (mensaje != null && mensaje.toLowerCase().contains("ya liquidado")) {
                    logger.info("El pago ya fue liquidado anteriormente");
                    pago.marcarComoLiquidado();
                    return true;
                }
            }

            return false;
        } catch (net.serenitybdd.screenplay.rest.exceptions.PotentialAuthenticationFailureException e) {
            logger.error("Error de autenticación con sistema de liquidación: {}", e.getMessage());
            throw new RuntimeException("Error de autenticación con sistema de liquidación", e);
        } catch (Exception e) {
            if (e.getMessage() != null && (e.getMessage().contains("Connection") || 
                e.getMessage().contains("timeout") || e.getMessage().contains("ConnectionRefused"))) {
                logger.error("Fallo de conectividad detectado: {}", e.getMessage());
                throw new RuntimeException("Fallo de conectividad con sistema de liquidación", e);
            }
            throw e;
        }
    }

    private <T extends Actor> boolean esperarConfirmacionLiquidacion(T actor) {
        logger.info("Esperando confirmación de liquidación asíncrona");
        long tiempoInicio = System.currentTimeMillis();
        long tiempoMaximo = MAX_ESPERA_CONECTIVIDAD_MS;

        while (System.currentTimeMillis() - tiempoInicio < tiempoMaximo) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            Boolean estadoActualizado = actor.asksFor(VerificarEstadoPago.delPago(pago.getId()));
            if (estadoActualizado != null && estadoActualizado && pago.estaLiquidado()) {
                logger.info("Liquidación confirmada asíncronamente");
                return true;
            }
        }

        logger.warn("Timeout esperando confirmación de liquidación");
        return false;
    }

    private void esperarReintentoConectividad() {
        try {
            logger.debug("Esperando {} ms antes de reintentar", RETRY_DELAY_MS);
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Espera de reintento interrumpida");
        }
    }

    private <T extends Actor> void verificarLiquidacionExitosa(T actor) {
        actor.should(
            seeThat("El pago debe estar liquidado",
                actor1 -> pago.estaLiquidado(),
                is(true)
            )
        );
        logger.info("Verificación de liquidación completada - Fecha: {}", pago.getFechaLiquidacion());
    }

    private Map<String, Object> construirPayloadLiquidacion() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("pagoId", pago.getId());
        payload.put("procesadorPagoId", pago.getProcesadorPagoId());
        payload.put("monto", pago.getMonto());
        payload.put("moneda", pago.getMoneda());
        payload.put("cuentaOrigen", pago.getCuentaOrigen());
        payload.put("cuentaDestino", pago.getCuentaDestino());
        payload.put("referenciaExternal", pago.getReferenciaExternal());
        payload.put("fechaProcesamiento", pago.getFechaProcesamiento() != null ? 
            pago.getFechaProcesamiento().toString() : Instant.now().toString());
        payload.put("sistemaOrigen", pago.getSistemaOrigen());
        payload.put("timestamp", Instant.now().toString());
        return payload;
    }

    private String generarClaveLiquidacion() {
        return pago.getIdempotencyKey() + "_LIQ_" + System.currentTimeMillis();
    }

    public String getReferenciaLiquidacion() {
        return referenciaLiquidacion;
    }

    public int getIntentosConectividad() {
        return intentosConectividad;
    }
}

// === ARCHIVO: src/test/java/com/pragma/pagos/questions/VerificarEstadoPago.java ===
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

// === ARCHIVO: src/test/java/com/pragma/pagos/interactions/EsperarRespuestaProcesador.java ===
package com.pragma.pagos.interactions;

import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.questions.RestQuestion;
import net.serenitybdd.screenplay.Functions;
import net.serenitybdd.screenplay.Task;
import com.pragma.pagos.models.Pago;
import com.pragma.pagos.models.Pago.EstadoPago;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EsperarRespuestaProcesador implements Interaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsperarRespuestaProcesador.class);

    private final String pagoId;
    private final String idempotencyKey;
    private final int timeoutTotalSegundos;
    private final int intervaloPoolSegundos;
    private final int maximosReintentos;
    private final EstadoPago[] estadosExito;
    private final EstadoPago[] estadosFallido;

    public EsperarRespuestaProcesador(String pagoId, String idempotencyKey) {
        this(pagoId, idempotencyKey, 60, 5, 3, 
             new EstadoPago[]{EstadoPago.APROBADO, EstadoPago.LIQUIDADO},
             new EstadoPago[]{EstadoPago.RECHAZADO, EstadoPago.FALLIDO});
    }

    public EsperarRespuestaProcesador(String pagoId, String idempotencyKey, int timeoutTotalSegundos,
                                       int intervaloPoolSegundos, int maximosReintentos,
                                       EstadoPago[] estadosExito, EstadoPago[] estadosFallido) {
        this.pagoId = pagoId;
        this.idempotencyKey = idempotencyKey;
        this.timeoutTotalSegundos = timeoutTotalSegundos;
        this.intervaloPoolSegundos = intervaloPoolSegundos;
        this.maximosReintentos = maximosReintentos;
        this.estadosExito = estadosExito;
        this.estadosFallido = estadosFallido;
    }

    @Override
    public <T extends net.serenitybdd.screenplay.actors.Actor> void performAs(T actor) {
        LOGGER.info("Iniciando espera de respuesta del procesador para pago: {}", pagoId);
        
        Instant tiempoInicio = Instant.now();
        int intentos = 0;
        boolean respuestaRecibida = false;
        EstadoPago estadoFinal = null;
        
        while (!respuestaRecibida && intentos < maximosReintentos) {
            try {
                Pago pago = consultarEstadoPago(actor);
                estadoFinal = pago.getEstado();
                
                LOGGER.info("Intento {} - Estado actual del pago: {}", intentos + 1, estadoFinal);
                
                if (esEstadoTerminal(estadoFinal)) {
                    respuestaRecibida = true;
                    LOGGER.info("Respuesta recibida. Estado final: {}", estadoFinal);
                    break;
                }
                
                if (haExcedidoTimeout(tiempoInicio)) {
                    LOGGER.warn("Timeout excedido después de {} segundos", timeoutTotalSegundos);
                    throw new TimeoutException("El procesador excedió el tiempo máximo de respuesta");
                }
                
                intentos++;
                LOGGER.info("Esperando {} segundos antes del siguiente intento", intervaloPoolSegundos);
                Thread.sleep(intervaloPoolSegundos * 1000L);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Hilo interrumpido durante la espera del procesador", e);
                throw new RuntimeException("Operación interrumpida", e);
            } catch (TimeoutException e) {
                LOGGER.error("Timeout en la comunicación con el procesador de pagos", e);
                throw new RuntimeException(e);
            }
        }
        
        if (!respuestaRecibida) {
            LOGGER.error("No se recibió respuesta después de {} intentos", maximosReintentos);
            throw new RuntimeException("No se recibió respuesta del procesador después de " + maximosReintentos + " intentos");
        }
    }

    private Pago consultarEstadoPago(net.serenitybdd.screenplay.actors.Actor actor) {
        String endpoint = String.format("/api/pagos/%s", pagoId);
        
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Idempotency-Key", idempotencyKey);
        headers.put("X-Request-Timeout", String.valueOf(intervaloPoolSegundos));
        
        return RestQuestion.about("Consulta de estado al procesador")
                .get(endpoint)
                .withHeaders(headers)
                .answeredBy(actor);
    }

    private boolean esEstadoTerminal(EstadoPago estado) {
        for (EstadoPago exito : estadosExito) {
            if (exito.equals(estado)) return true;
        }
        for (EstadoPago fallido : estadosFallido) {
            if (fallido.equals(estado)) return true;
        }
        return false;
    }

    private boolean haExcedidoTimeout(Instant tiempoInicio) {
        Duration transcurrido = Duration.between(tiempoInicio, Instant.now());
        return transcurrido.getSeconds() >= timeoutTotalSegundos;
    }

    public static EsperarRespuestaProcesador conTimeout(String pagoId, String idempotencyKey) {
        return new EsperarRespuestaProcesador(pagoId, idempotencyKey);
    }

    public static Builder conConfiguracion() {
        return new Builder();
    }

    public static class Builder {
        private String pagoId;
        private String idempotencyKey;
        private int timeoutTotalSegundos = 60;
        private int intervaloPoolSegundos = 5;
        private int maximosReintentos = 3;
        private EstadoPago[] estadosExito = new EstadoPago[]{EstadoPago.APROBADO, EstadoPago.LIQUIDADO};
        private EstadoPago[] estadosFallido = new EstadoPago[]{EstadoPago.RECHAZADO, EstadoPago.FALLIDO};

        public Builder paraPago(String pagoId) {
            this.pagoId = pagoId;
            return this;
        }

        public Builder conIdempotencyKey(String key) {
            this.idempotencyKey = key;
            return this;
        }

        public Builder conTimeout(int segundos) {
            this.timeoutTotalSegundos = segundos;
            return this;
        }

        public Builder conIntervaloPool(int segundos) {
            this.intervaloPoolSegundos = segundos;
            return this;
        }

        public Builder conMaximosReintentos(int reintentos) {
            this.maximosReintentos = reintentos;
            return this;
        }

        public EsperarRespuestaProcesador build() {
            return new EsperarRespuestaProcesador(pagoId, idempotencyKey, timeoutTotalSegundos,
                    intervaloPoolSegundos, maximosReintentos, estadosExito, estadosFallido);
        }
    }
}

// === ARCHIVO: src/test/java/com/pragma/pagos/interactions/ReintentarLiquidacion.java ===
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


// === ARCHIVO: src/test/resources/config/serenity.conf ===
webdriver {
  driver = chrome
  autodownload = true
  timeouts {
    implicit = 10000
    script = 30000
    page.load = 60000
  }
  capabilities {
    browserName = "chrome"
    acceptInsecureCerts = true
    "goog:chromeOptions" {
      args = [
        "--start-maximized",
        "--disable-infobars",
        "--disable-notifications",
        "--disable-popup-blocking",
        "--no-sandbox",
        "--disable-dev-shm-usage",
        "--disable-gpu",
        "--window-size=1920,1080"
      ]
      prefs = {
        "profile.default_content_setting_values.notifications" = 2
        "credentials_enable_service" = false
        "profile.password_manager_enabled" = false
      }
    }
  }
}

environments {
  default = "qa"
  
  dev {
    base.url = "https://pagos-dev.pragma.com.co"
    api.base.url = "https://api-pagos-dev.pragma.com.co"
    procesador.url = "https://procesador-dev.pragma.com.co"
    liquidacion.url = "https://liquidacion-dev.pragma.com.co"
    timeout = 30000
    retry.attempts = 3
    retry.delay = 2000
  }
  
  qa {
    base.url = "https://pagos-qa.pragma.com.co"
    api.base.url = "https://api-pagos-qa.pragma.com.co"
    procesador.url = "https://procesador-qa.pragma.com.co"
    liquidacion.url = "https://liquidacion-qa.pragma.com.co"
    timeout = 45000
    retry.attempts = 5
    retry.delay = 3000
  }
  
  staging {
    base.url = "https://pagos-staging.pragma.com.co"
    api.base.url = "https://api-pagos-staging.pragma.com.co"
    procesador.url = "https://procesador-staging.pragma.com.co"
    liquidacion.url = "https://liquidacion-staging.pragma.com.co"
    timeout = 60000
    retry.attempts = 3
    retry.delay = 5000
  }
  
  prod {
    base.url = "https://pagos.pragma.com.co"
    api.base.url = "https://api-pagos.pragma.com.co"
    procesador.url = "https://procesador.pragma.com.co"
    liquidacion.url = "https://liquidacion.pragma.com.co"
    timeout = 90000
    retry.attempts = 2
    retry.delay = 10000
  }
}

serenity {
  take.screenshots = AFTER_EACH_STEP
  screenshot.format = png
  report.show.related.tests = true
  test.root = "com.pragma.pagos"
  
  restapi {
    timeout = 30000
    connect.timeout = 15000
    socket.timeout = 30000
    retries {
      enabled = true
      max.retries = 3
      retry.interval = 1000
    }
  }
  
  performance {
    threshold.responses.per.hour = 5000
    max.response.time.ms = 2000
    alert.on.threshold.breach = true
  }
  
  idempotency {
    key.prefix = "IDEM"
    key.timestamp.format = "yyyyMMddHHmmss"
    key.separator = "-"
    cache.enabled = true
    cache.ttl.minutes = 60
  }
}

logging {
  level = INFO
  pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file {
    enabled = true
    path = "target/serenity.log"
    max.size = 10MB
    max.history = 5
  }
  console {
    enabled = true
    color.enabled = true
  }
}

reports {
  standalone {
    enabled = true
    outputDirectory = "target/site/serenity"
    historyDir = "target/site/serenity/history"
  }
  
  json {
    enabled = true
    outputDirectory = "target/site/serenity/json"
  }
  
  html {
    enabled = true
    outputDirectory = "target/site/serenity"
    compress = true
  }
}

// === ARCHIVO: src/test/resources/data/pagos.csv ===
id_pago;idempotency_key;monto;moneda;cuenta_origen;cuenta_destino;concepto;estado_esperado;tipo_pago;reintentos_max;timeout_seg;espera_liquidacion;scenario_descripcion
PAG-001;IDEM-20240615-100000-001;50000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;APROBADO;TRANSFERENCIA;3;30;true;Pago exitoso con idempotencia nueva
PAG-002;IDEM-20240615-100001-002;125000.50;COP;CTA-123456789;CTA-987654321;Compra producto;APROBADO;TRANSFERENCIA;3;30;true,Pago exitoso monto con decimales
PAG-003;IDEM-20240615-100002-003;75000.00;USD;CTA-123456789;CTA-555555555;Pago internacional;APROBADO;INTERNACIONAL;5;60;true,Pago internacional en dólares
PAG-004;IDEM-20240615-100003-004;50000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;APROBADO;TRANSFERENCIA;3;30;true,Reenvío con misma clave idempotencia - debe retornar duplicado
PAG-005;IDEM-20240615-100004-005;50000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;RECHAZADO;TRANSFERENCIA;3;30;false,Monto menor al mínimo permitido
PAG-006;IDEM-20240615-100005-006;50000000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;RECHAZADO;TRANSFERENCIA;3;30;false,Monto mayor al máximo permitido
PAG-007;IDEM-20240615-100006-007;100000.00;COP;CTA-000000000;CTA-987654321;Pago servicios;RECHAZADO;TRANSFERENCIA;3;30;false,Cuenta origen inválida
PAG-008;IDEM-20240615-100007-008;100000.00;COP;CTA-123456789;CTA-000000000;Pago servicios;RECHAZADO;TRANSFERENCIA;3;30;false,Cuenta destino inválida
PAG-009;IDEM-20240615-100008-009;25000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;TIMEOUT;TRANSFERENCIA;5;10;true,Timeout del procesador - reintento automático
PAG-010;IDEM-20240615-100009-010;25000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;ERROR_CONECTIVIDAD;TRANSFERENCIA;5;10;false,Error de conectividad con procesador
PAG-011;IDEM-20240615-100010-011;25000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;FALLIDO;TRANSFERENCIA;3;30;false,Fallo en procesamiento - sin reintentos disponibles
PAG-012;IDEM-20240615-100011-012;50000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;LIQUIDADO;TRANSFERENCIA;3;30;true,Pago aprobado y liquidado exitosamente
PAG-013;IDEM-20240615-100012-013;80000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;RECHAZADO;TRANSFERENCIA;3;30;false,Cuenta destino bloqueada por fraude
PAG-014;IDEM-20240615-100013-014;95000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;ERROR_LIQUIDACION;TRANSFERENCIA;3;30;false,Error en liquidación - requiere intervención manual
PAG-015;IDEM-20240615-100014-015;150000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;APROBADO;TRANSFERENCIA;3;30;true,Pago exitoso cerca del límite superior
PAG-016;IDEM-20240615-100015-016;100.00;COP;CTA-123456789;CTA-987654321;Pago servicios;APROBADO;TRANSFERENCIA;3;30;true,Pago exitoso cerca del límite inferior
PAG-017;IDEM-20240615-100016-017;50000.00;COP;CTA-999999999;CTA-987654321;Pago servicios;RECHAZADO;TRANSFERENCIA;3;30;false,Cuenta origen sin saldo suficiente
PAG-018;IDEM-20240615-100017-018;50000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;APROBADO;TRANSFERENCIA;3;30;true,Caso base para pruebas de rendimiento
PAG-019;IDEM-20240615-100018-019;50000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;APROBADO;TRANSFERENCIA;3;30;true,Segundo pago con clave diferente - no es duplicado
PAG-020;IDEM-20240615-100019-020;50000.00;COP;CTA-123456789;CTA-987654321;Pago servicios;APROBADO;TRANSFERENCIA;3;30;true,Tercer pago con clave diferente - no es duplicado

// === ARCHIVO: src/test/java/com/pragma/pagos/utils/IdempotenciaHelper.java ===
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
        String clave = String.format("%s-%s-%s-%s", KEY_PREFIX, timestamp, cuentaOrigen.substring(ctaOrigen.length() - 6), uniqueId);
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
        CACHE_EXPIRacion.entrySet().removeIf(entry -> ahora.isAfter(entry.getValue()));
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


// === ARCHIVO: src/test/java/com/pragma/pagos/utils/PerformanceHelper.java ===
package com.pragma.pagos.utils;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class PerformanceHelper {
    private final AtomicInteger processedCount = new AtomicInteger(0);
    private final AtomicInteger failedCount = new AtomicInteger(0);
    private final List<Long> executionTimes = Collections.synchronizedList(new ArrayList<>());
    private final Instant startTime;
    private volatile Instant endTime;
    private final int targetThroughputPerHour;
    private final Map<String, AtomicInteger> statusBreakdown = new ConcurrentHashMap<>();

    public PerformanceHelper(int targetThroughputPerHour) {
        this.targetThroughputPerHour = targetThroughputPerHour;
        this.startTime = Instant.now();
    }

    public PerformanceHelper() {
        this(5000);
    }

    public void recordProcessing(long executionTimeMs, String status) {
        executionTimes.add(executionTimeMs);
        processedCount.incrementAndGet();
        statusBreakdown.computeIfAbsent(status, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void recordFailure(String errorType) {
        failedCount.incrementAndGet();
        statusBreakdown.computeIfAbsent("FAILED_" + errorType, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void finish() {
        this.endTime = Instant.now();
    }

    public double calculateThroughputPerHour() {
        if (endTime == null) {
            endTime = Instant.now();
        }
        long durationSeconds = Duration.between(startTime, endTime).getSeconds();
        if (durationSeconds == 0) {
            return 0.0;
        }
        return (processedCount.get() * 3600.0) / durationSeconds;
    }

    public double calculateAverageExecutionTimeMs() {
        if (executionTimes.isEmpty()) {
            return 0.0;
        }
        return executionTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }

    public long calculatePercentile(long percentile) {
        if (executionTimes.isEmpty()) {
            return 0;
        }
        List<Long> sorted = new ArrayList<>(executionTimes);
        Collections.sort(sorted);
        int index = (int) Math.ceil((percentile / 100.0) * sorted.size()) - 1;
        return sorted.get(Math.max(0, index));
    }

    public boolean meetsThroughputThreshold() {
        double currentThroughput = calculateThroughputPerHour();
        return currentThroughput >= targetThroughputPerHour;
    }

    public double getThroughputPercentage() {
        double current = calculateThroughputPerHour();
        return (current / targetThroughputPerHour) * 100.0;
    }

    public int getTotalProcessed() {
        return processedCount.get();
    }

    public int getTotalFailed() {
        return failedCount.get();
    }

    public long getDurationSeconds() {
        Instant referenceEnd = (endTime != null) ? endTime : Instant.now();
        return Duration.between(startTime, referenceEnd).getSeconds();
    }

    public Map<String, Integer> getStatusBreakdown() {
        Map<String, Integer> result = new HashMap<>();
        statusBreakdown.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }

    public long getP50() {
        return calculatePercentile(50);
    }

    public long getP95() {
        return calculatePercentile(95);
    }

    public long getP99() {
        return calculatePercentile(99);
    }

    public long getMaxExecutionTime() {
        return executionTimes.stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);
    }

    public long getMinExecutionTime() {
        return executionTimes.stream()
                .mapToLong(Long::longValue)
                .min()
                .orElse(0);
    }

    public double getSuccessRate() {
        int total = processedCount.get() + failedCount.get();
        if (total == 0) {
            return 0.0;
        }
        return (processedCount.get() * 100.0) / total;
    }

    public String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== REPORTE DE RENDIMIENTO ===\n");
        report.append("Umbral objetivo: ").append(targetThroughputPerHour).append(" pagos/hora\n");
        report.append("Pagos procesados: ").append(getTotalProcessed()).append("\n");
        report.append("Pagos fallidos: ").append(getTotalFailed()).append("\n");
        report.append("Duracion: ").append(getDurationSeconds()).append(" segundos\n");
        report.append("Throughput actual: ").append(String.format("%.2f", calculateThroughputPerHour()))
              .append(" pagos/hora\n");
        report.append("Porcentaje del umbral: ").append(String.format("%.2f%%", getThroughputPercentage())).append("\n");
        report.append("Cumple umbral: ").append(meetsThroughputThreshold() ? "SI" : "NO").append("\n");
        report.append("Tasa de exito: ").append(String.format("%.2f%%", getSuccessRate())).append("\n");
        report.append("Tiempo promedio: ").append(String.format("%.2f", calculateAverageExecutionTimeMs())).append(" ms\n");
        report.append("Tiempo minimo: ").append(getMinExecutionTime()).append(" ms\n");
        report.append("Tiempo maximo: ").append(getMaxExecutionTime()).append(" ms\n");
        report.append("P50: ").append(getP50()).append(" ms\n");
        report.append("P95: ").append(getP95()).append(" ms\n");
        report.append("P99: ").append(getP99()).append(" ms\n");
        report.append("=== Desglose por estado ===\n");
        getStatusBreakdown().forEach((status, count) -> 
            report.append(status).append(": ").append(count).append("\n")
        );
        return report.toString();
    }

    public PerformanceMetrics getMetrics() {
        return new PerformanceMetrics(
            getTotalProcessed(),
            getTotalFailed(),
            getDurationSeconds(),
            calculateThroughputPerHour(),
            calculateAverageExecutionTimeMs(),
            getP50(),
            getP95(),
            getP99(),
            getSuccessRate(),
            meetsThroughputThreshold()
        );
    }

    public record PerformanceMetrics(
        int totalProcessed,
        int totalFailed,
        long durationSeconds,
        double throughputPerHour,
        double averageExecutionTimeMs,
        long p50,
        long p95,
        long p99,
        double successRate,
        boolean meetsThreshold
    ) {}

    public static class ThroughputValidator {
        private final int expectedPayments;
        private final Duration maxDuration;

        public ThroughputValidator(int expectedPayments, Duration maxDuration) {
            this.expectedPayments = expectedPayments;
            this.maxDuration = maxDuration;
        }

        public boolean validate(PerformanceHelper helper) {
            if (helper.getTotalProcessed() < expectedPayments) {
                return false;
            }
            Duration actualDuration = Duration.between(helper.startTime, 
                helper.endTime != null ? helper.endTime : Instant.now());
            return actualDuration.compareTo(maxDuration) <= 0;
        }

        public String getValidationMessage(PerformanceHelper helper) {
            Duration actualDuration = Duration.between(helper.startTime, 
                helper.endTime != null ? helper.endTime : Instant.now());
            return String.format(
                "Validacion: %d pagos en %d segundos (maximo: %d segundos). %s",
                helper.getTotalProcessed(),
                actualDuration.getSeconds(),
                maxDuration.getSeconds(),
                validate(helper) ? "CUMPLE" : "NO CUMPLE"
            );
        }
    }
}


// === ARCHIVO: docs/flujo-pagos-actual.md ===
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

// === ARCHIVO: docs/diseno-solucion-automatizada.md ===
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

// === ARCHIVO: docs/resultados-pruebas.md ===
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


=== ARCHIVO: pom.xml ===
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.pragma</groupId>
    <artifactId>pagos-test-automation</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>Pagos Test Automation</name>
    <description>Automatizacion de pruebas para el sistema de pagos</description>

    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <serenity.version>4.1.0</serenity.version>
        <cucumber.version>7.15.0</cucumber.version>
        <junit.version>5.10.2</junit.version>
        <selenium.version>4.22.0</selenium.version>
        <rest-assured.version>5.4.0</rest-assured.version>
        <lombok.version>1.18.30</lombok.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>net.serenity-bdd</groupId>
            <artifactId>serenity-core</artifactId>
            <version>${serenity.version}</version>
        </dependency>
        <dependency>
            <groupId>net.serenity-bdd</groupId>
            <artifactId>serenity-cucumber</artifactId>
            <version>${serenity.version}</version>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit</artifactId>
            <version>${cucumber.version}</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
        </dependency>
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>${rest-assured.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>
        <dependency>
            <groupId>org.hamcrest</groupId>
            <artifactId>hamcrest</artifactId>
            <version>2.2</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
            <plugin>
                <groupId>net.serenity-bdd</groupId>
                <artifactId>serenity-maven-plugin</artifactId>
                <version>${serenity.version}</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/test/java/com/pragma/pagos/models/Pago.java ===
package com.pragma.pagos.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

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
        PENDIENTE,
        PROCESANDO,
        APROBADO,
        RECHAZADO,
        LIQUIDADO,
        FALLIDO,
        TIMEOUT
    }

    public enum TipoPago {
        TRANSFERENCIA,
        PAGO_CON_TARJETA,
        DEBITO,
        CREDITO
    }

    public Pago() {
        this.reintentos = 0;
        this.estado = EstadoPago.PENDIENTE;
    }

    public static Pago crearNuevo(BigDecimal monto, String cuentaOrigen, String cuentaDestino, String concepto) {
        Pago pago = new Pago();
        pago.setMonto(monto);
        pago.setCuentaOrigen(cuentaOrigen);
        pago.setCuentaDestino(cuentaDestino);
        pago.setConcepto(concepto);
        pago.setFechaCreacion(Instant.now());
        pago.setEstado(EstadoPago.PENDIENTE);
        return pago;
    }

    public static String generarIdempotencyKey(String cuentaOrigen, BigDecimal monto) {
        return UUID.randomUUID().toString() + "-" + cuentaOrigen + "-" + monto.toPlainString();
    }

    public boolean esIdempotente() {
        return idempotencyKey != null && !idempotencyKey.isEmpty();
    }

    public boolean puedeReintentarse() {
        return reintentos != null && reintentos < 3 && (estado == EstadoPago.FALLIDO || estado == EstadoPago.TIMEOUT);
    }

    public boolean estaLiquidado() {
        return estado == EstadoPago.LIQUIDADO;
    }

    public boolean requiereProcesamiento() {
        return estado == EstadoPago.PENDIENTE || estado == EstadoPago.PROCESANDO;
    }

    public void marcarComoProcesando() {
        this.estado = EstadoPago.PROCESANDO;
        this.fechaActualizacion = Instant.now();
    }

    public void marcarComoAprobado(String procesadorId) {
        this.estado = EstadoPago.APROBADO;
        this.procesadorPagoId = procesadorId;
        this.fechaProcesamiento = Instant.now();
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

// === ARCHIVO: src/test/java/com/pragma/pagos/tasks/IniciarPago.java ===
package com.pragma.pagos.tasks;

import com.pragma.pagos.models.Pago;
import com.pragma.pagos.models.Pago.EstadoPago;
import com.pragma.pagos.utils.IdempotenciaHelper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

public class IniciarPago implements Task {

    private static final Logger logger = LoggerFactory.getLogger(IniciarPago.class);
    private static final String SISTEMA_ORIGEN_ENDPOINT = "/api/v1/pagos/iniciar";
    private static final int TIMEOUT_SECONDS = 30;

    private final BigDecimal monto;
    private final String cuentaOrigen;
    private final String cuentaDestino;
    private final String concepto;
    private final String canal;
    private final String usuario;

    private Pago pagoCreado;

    public IniciarPago(BigDecimal monto, String cuentaOrigen, String cuentaDestino, String concepto) {
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.concepto = concepto;
        this.canal = "AUTOMATIZADO";
        this.usuario = "QA_AUTOMATION";
    }

    public static IniciarPago conDatos(BigDecimal monto, String cuentaOrigen, String cuentaDestino, String concepto) {
        return new IniciarPago(monto, cuentaOrigen, cuentaDestino, concepto);
    }

    public IniciarPago conCanal(String canal) {
        this.canal = canal;
        return this;
    }

    public IniciarPago conUsuario(String usuario) {
        this.usuario = usuario;
        return this;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info("Iniciando proceso de pago - Monto: {}, Origen: {}, Destino: {}", monto, cuentaOrigen, cuentaDestino);

        String idempotencyKey = IdempotenciaHelper.generarClaveIdempotencia(cuentaOrigen, monto);
        logger.debug("Clave de idempotencia generada: {}", idempotencyKey);

        Map<String, Object> payload = construirPayloadPago(idempotencyKey);
        logger.debug("Payload construido para iniciar pago: {}", payload);

        actor.attemptsTo(
            Post.to(SISTEMA_ORIGEN_ENDPOINT)
                .with(request -> request
                    .header("Content-Type", "application/json")
                    .header("X-Idempotency-Key", idempotencyKey)
                    .header("X-Request-Timeout", String.valueOf(TIMEOUT_SECONDS))
                    .body(payload)
                    .relaxedHTTPSValidation()
                )
        );

        Response respuesta = actor.asksFor(LastResponse.received());
        int statusCode = respuesta.getStatusCode();
        logger.info("Respuesta del sistema de origen - Status: {}, Body: {}", statusCode, respuesta.getBody().asString());

        actor.should(
            seeThat("Codigo de estado de respuesta",
                response -> statusCode,
                anyOf(is(200), is(201), is(202))
            )
        );

        Pago pago = mapearRespuestaAPago(respuesta, idempotencyKey);
        this.pagoCreado = pago;

        logger.info("Pago iniciado exitosamente - ID: {}, Estado: {}", pago.getId(), pago.getEstado());
        actor.remember("pagoActual", pago);
        actor.remember("idempotencyKey", idempotencyKey);
    }

    private Map<String, Object> construirPayloadPago(String idempotencyKey) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("idempotencyKey", idempotencyKey);
        payload.put("monto", monto);
        payload.put("moneda", "COP");
        payload.put("cuentaOrigen", cuentaOrigen);
        payload.put("cuentaDestino", cuentaDestino);
        payload.put("concepto", concepto);
        payload.put("canal", canal);
        payload.put("usuario", usuario);
        payload.put("ipOrigen", "192.168.1.100");
        payload.put("tipoPago", "TRANSFERENCIA");
        return payload;
    }

    private Pago mapearRespuestaAPago(Response respuesta, String idempotencyKey) {
        Pago pago = Pago.crearNuevo(monto, cuentaOrigen, cuentaDestino, concepto);
        pago.setIdempotencyKey(idempotencyKey);
        pago.setCanal(canal);
        pago.setUsuario(usuario);
        pago.setIpOrigen("192.168.1.100");
        pago.setFechaCreacion(Instant.now());

        if (respuesta.getStatusCode() == 200 || respuesta.getStatusCode() == 201) {
            String idPago = respuesta.jsonPath().getString("id");
            String estado = respuesta.jsonPath().getString("estado");
            String referencia = respuesta.jsonPath().getString("referenciaExternal");

            pago.setId(idPago);
            pago.setEstado(EstadoPago.valueOf(estado.toUpperCase()));
            pago.setReferenciaExternal(referencia);
            pago.setSistemaOrigen("SISTEMA_ORIGEN");

            if (estado.equalsIgnoreCase("PROCESANDO")) {
                pago.marcarComoProcesando();
            } else if (estado.equalsIgnoreCase("APROBADO")) {
                pago.marcarComoAprobado("PROCESADOR_DEFAULT");
            }
        } else if (respuesta.getStatusCode() == 202) {
            String idPago = respuesta.jsonPath().getString("id");
            pago.setId(idPago);
            pago.setEstado(EstadoPago.PENDIENTE);
            pago.setSistemaOrigen("SISTEMA_ORIGEN");
        }

        return pago;
    }

    public Pago getPagoCreado() {
        return pagoCreado;
    }
}

// === ARCHIVO: src/test/java/com/pragma/pagos/tasks/ProcesarPago.java ===
package com.pragma.pagos.tasks;

import com.pragma.pagos.models.Pago;
import com.pragma.pagos.models.Pago.EstadoPago;
import com.pragma.pagos.questions.VerificarEstadoPago;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

public class ProcesarPago implements Task {

    private static final Logger logger = LoggerFactory.getLogger(ProcesarPago.class);
    private static final String PROCESADOR_ENDPOINT = "/api/v1/procesador/pagar";
    private static final int MAX_REINTENTOS = 3;
    private static final long TIMEOUT_MS = 45000;
    private static final long RETRY_DELAY_MS = 5000;

    private final Pago pago;
    private String procesadorId;
    private int reintentosActual = 0;

    public ProcesarPago(Pago pago) {
        this.pago = pago;
    }

    public static ProcesarPago enElProcesador(Pago pago) {
        return new ProcesarPago(pago);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info("Iniciando procesamiento de pago - ID: {}, Monto: {}", pago.getId(), pago.getMonto());

        if (!pago.requiereProcesamiento()) {
            logger.warn("El pago no requiere procesamiento - Estado actual: {}", pago.getEstado());
            return;
        }

        boolean procesamientoExitoso = intentarProcesamiento(actor);

        if (!procesamientoExitoso && reintentosActual < MAX_REINTENTOS) {
            logger.warn("Procesamiento inicial fallido, iniciando reintentos. Reintentos restantes: {}",
                MAX_REINTENTOS - reintentosActual);
            procesamientoExitoso = ejecutarConReintentos(actor);
        }

        if (!procesamientoExitoso) {
            logger.error("Pago fallido despues de {} intentos", MAX_REINTENTOS);
            pago.marcarComoTimeout();
            throw new RuntimeException("Pago fallido tras reintentos: " + pago.getId());
        }

        verificarEstadoFinal(actor);
    }

    private <T extends Actor> boolean intentarProcesamiento(T actor) {
        try {
            Map<String, Object> payload = construirPayloadProcesamiento();
            logger.debug("Enviando pago al procesador: {}", payload);

            actor.attemptsTo(
                Post.to(PROCESADOR_ENDPOINT)
                    .with(request -> request
                        .header("Content-Type", "application/json")
                        .header("X-Idempotency-Key", pago.getIdempotencyKey())
                        .header("X-Request-Timeout", String.valueOf(TIMEOUT_MS))
                        .body(payload)
                        .relaxedHTTPSValidation()
                        .timeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    )
            );

            Response respuesta = actor.asksFor(LastResponse.received());
            int statusCode = respuesta.getStatusCode();
            String responseBody = respuesta.getBody().asString();
            logger.info("Respuesta del procesador - Status: {}, Body: {}", statusCode, responseBody);

            if (statusCode == 200 || statusCode == 201) {
                String resultado = respuesta.jsonPath().getString("resultado");
                String idProcesador = respuesta.jsonPath().getString("procesadorId");
                String mensaje = respuesta.jsonPath().getString("mensaje");

                if ("APROBADO".equalsIgnoreCase(resultado) || "SUCCESS".equalsIgnoreCase(resultado)) {
                    this.procesadorId = idProcesador;
                    pago.marcarComoAprobado(idProcesador);
                    pago.setFechaProcesamiento(Instant.now());
                    logger.info("Pago aprobado por procesador - ID: {}", idProcesador);
                    return true;
                } else if ("PENDIENTE".equalsIgnoreCase(resultado)) {
                    logger.info("Pago en estado pendiente, esperando procesamiento asincrono");
                    return esperarProcesamientoAsincrono(actor);
                } else {
                    String mensajeRechazo = mensaje != null ? mensaje : "Rechazado por procesador";
                    pago.marcarComoRechazado(mensajeRechazo);
                    logger.error("Pago rechazado por procesador: {}", mensajeRechazo);
                    return false;
                }
            } else if (statusCode == 408 || statusCode == 504) {
                logger.warn("Timeout del procesador - Status: {}", statusCode);
                pago.marcarComoTimeout();
                return false;
            } else if (statusCode >= 500) {
                logger.error("Error del servidor del procesador - Status: {}", statusCode);
                return false;
            }

            return false;
        } catch (Exception e) {
            logger.error("Excepcion durante procesamiento: {}", e.getMessage(), e);
            return false;
        }
    }

    private <T extends Actor> boolean ejecutarConReintentos(T actor) {
        for (int i = reintentosActual; i < MAX_REINTENTOS; i++) {
            reintentosActual = i + 1;
            logger.info("Ejecutando reintento {} de {}", reintentosActual, MAX_REINTENTOS);

            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Reintento interrumpido");
            }

            if (intentarProcesamiento(actor)) {
                return true;
            }

            logger.warn("Reintento {} fallido", reintentosActual);
        }
        return false;
    }

    private <T extends Actor> boolean esperarProcesamientoAsincrono(T actor) {
        logger.info("Esperando procesamiento asincrono del pago");
        long tiempoInicio = System.currentTimeMillis();
        long tiempoMaximo = TIMEOUT_MS;

        while (System.currentTimeMillis() - tiempoInicio < tiempoMaximo) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            Boolean estadoActualizado = actor.asksFor(VerificarEstadoPago.delPago(pago.getId()));
            if (estadoActualizado != null && estadoActualizado) {
                logger.info("Pago procesado asincronamente de forma exitosa");
                return true;
            }
        }

        logger.warn("Timeout esperando procesamiento asincrono");
        return false;
    }

    private <T extends Actor> void verificarEstadoFinal(T actor) {
        actor.should(
            seeThat("El pago debe estar aprobado",
                actor1 -> pago.getEstado() == EstadoPago.APROBADO,
                is(true)
            )
        );
        logger.info("Verificacion de estado final completada - Estado: {}", pago.getEstado());
    }

    private Map<String, Object> construirPayloadProcesamiento() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("pagoId", pago.getId());
        payload.put("idempotencyKey", pago.getIdempotencyKey());
        payload.put("monto", pago.getMonto());
        payload.put("moneda", pago.getMoneda());
        payload.put("cuentaOrigen", pago.getCuentaOrigen());
        payload.put("cuentaDestino", pago.getCuentaDestino());
        payload.put("concepto", pago.getConcepto());
        payload.put("referenciaExternal", pago.getReferenciaExternal());
        payload.put("sistemaOrigen", pago.getSistemaOrigen());
        payload.put("timestamp", Instant.now().toString());
        return payload;
    }

    public String getProcesadorId() {
        return procesadorId;
    }

    public int getReintentosActual() {
        return reintentosActual;
    }
}

// === ARCHIVO: src/test/java/com/pragma/pagos/tasks/LiquidarPago.java ===
package com.pragma.pagos.tasks;

import com.pragma.pagos.models.Pago;
import com.pragma.pagos.models.Pago.EstadoPago;
import com.pragma.pagos.questions.VerificarEstadoPago;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.LastResponse;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.CoreMatchers.*;

public class LiquidarPago implements Task {

    private static final Logger logger = LoggerFactory.getLogger(LiquidarPago.class);
    private static final String LIQUIDACION_ENDPOINT = "/api/v1/liquidacion/registrar";
    private static final int MAX_REINTENTOS_CONECTIVIDAD = 5;
    private static final long TIMEOUT_MS = 30000;
    private static final long RETRY_DELAY_MS = 3000;
    private static final long MAX_ESPERA_CONECTIVIDAD_MS = 60000;

    private final Pago pago;
    private String referenciaLiquidacion;
    private int intentosConectividad = 0;

    public LiquidarPago(Pago pago) {
        this.pago = pago;
    }

    public static LiquidarPago enElSistemaDeLiquidacion(Pago pago) {
        return new LiquidarPago(pago);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        logger.info("Iniciando liquidacion de pago - ID: {}, Estado actual: {}", pago.getId(), pago.getEstado());

        if (!pago.estaLiquidado()) {
            logger.warn("El pago no esta en estado de liquidacion - Estado: {}", pago.getEstado());
            if (pago.getEstado() != EstadoPago.APROBADO) {
                throw new IllegalStateException("No se puede liquidar un pago que no esta aprobado. Estado: " + pago.getEstado());
            }
        }

        boolean liquidado = false;
        Exception ultimaExcepcion = null;

        for (int intento = 1; intento <= MAX_REINTENTOS_CONECTIVIDAD; intento++) {
            try {
                liquidado = intentarLiquidacion(actor);
                if (liquidado) {
                    break;
                }
            } catch (Exception e) {
                ultimaExcepcion = e;
                logger.error("Error de conectividad en intento {}: {}", intento, e.getMessage());
                intentosConectividad = intento;

                if (intento < MAX_REINTENTOS_CONECTIVIDAD) {
                    logger.info("Esperando antes del siguiente intento de conectividad...");
                    esperarReintentoConectividad();
                }
            }
        }

        if (!liquidado && ultimaExcepcion != null) {
            logger.error("Liquidacion fallida despues de {} intentos por problemas de conectividad", MAX_REINTENTOS_CONECTIVIDAD);
            pago.marcarComoFallido("Fallos de conectividad con sistema de liquidacion: " + ultimaExcepcion.getMessage());
            throw new RuntimeException("No se pudo liquidar el pago por problemas de conectividad", ultimaExcepcion);
        }

        verificarLiquidacionExitosa(actor);
    }

    private <T extends Actor> boolean intentarLiquidacion(T actor) {
        logger.debug("Intentando liquidacion del pago {}", pago.getId());

        Map<String, Object> payload = construirPayloadLiquidacion();
        logger.debug("Payload de liquidacion: {}", payload);

        try {
            actor.attemptsTo(
                Post.to(LIQUIDACION_ENDPOINT)
                    .with(request -> request
                        .header("Content-Type", "application/json")
                        .header("X-Idempotency-Key", generarClaveLiquidacion())
                        .header("X-Request-Timeout", String.valueOf(TIMEOUT_MS))
                        .body(payload)
                        .relaxedHTTPSValidation()
                        .timeout(TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    )
            );

            Response respuesta = actor.asksFor(LastResponse.received());
            int statusCode = respuesta.getStatusCode();
            String responseBody = respuesta.getBody().asString();
            logger.info("Respuesta del sistema de liquidacion - Status: {}, Body: {}", statusCode, responseBody);

            if (statusCode == 200 || statusCode == 201) {
                String estadoLiquidacion = respuesta.jsonPath().getString("estado");
                this.referenciaLiquidacion = respuesta.jsonPath().getString("referenciaLiquidacion");

                if ("LIQUIDADO".equalsIgnoreCase(estadoLiquidacion) || "CONFIRMADO".equalsIgnoreCase(estadoLiquidacion)) {
                    pago.marcarComoLiquidado();
                    pago.setFechaLiquidacion(Instant.now());
                    logger.info("Pago liquidado exitosamente - Referencia: {}", referenciaLiquidacion);
                    return true;
                } else if ("PENDIENTE".equalsIgnoreCase(estadoLiquidacion)) {
                    logger.info("Liquidacion en proceso asincrono, esperando confirmacion");
                    return esperarConfirmacionLiquidacion(actor);
                }
            } else if (statusCode >= 500) {
                logger.error("Error del servidor de liquidacion - Status: {}", statusCode);
                throw new RuntimeException("Error de conectividad con sistema de liquidacion: HTTP " + statusCode);
            } else if (statusCode == 409 || statusCode == 422) {
                String mensaje = respuesta.jsonPath().getString("mensaje");
                if (mensaje != null && mensaje.toLowerCase().contains("ya liquidado")) {
                    logger.info("El pago ya fue liquidado anteriormente");
                    pago.marcarComoLiquidado();
                    return true;
                }
            }

            return false;
        } catch (net.serenitybdd.screenplay.rest.exceptions.PotentialAuthenticationFailureException e) {
            logger.error("Error de autenticacion con sistema de liquidacion: {}", e.getMessage());
            throw new RuntimeException("Error de autenticacion con sistema de liquidacion", e);
        } catch (Exception e) {
            if (e.getMessage() != null && (e.getMessage().contains("Connection") || 
                e.getMessage().contains("timeout") || e.getMessage().contains("ConnectionRefused"))) {
                logger.error("Fallo de conectividad detectado: {}", e.getMessage());
                throw new RuntimeException("Fallo de conectividad con sistema de liquidacion", e);
            }
            throw e;
        }
    }

    private <T extends Actor> boolean esperarConfirmacionLiquidacion(T actor) {
        logger.info("Esperando confirmacion de liquidacion asincrona");
        long tiempoInicio = System.currentTimeMillis();
        long tiempoMaximo = MAX_ESPERA_CONECTIVIDAD_MS;

        while (System.currentTimeMillis() - tiempoInicio < tiempoMaximo) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }

            Boolean estadoActualizado = actor.asksFor(VerificarEstadoPago.delPago(pago.getId()));
            if (estadoActualizado != null && estadoActualizado && pago.estaLiquidado()) {
                logger.info("Liquidacion confirmada asincronamente");
                return true;
            }
        }

        logger.warn("Timeout esperando confirmacion de liquidacion");
        return false;
    }

    private void esperarReintentoConectividad() {
        try {
            logger.debug("Esperando {} ms antes de reintentar", RETRY_DELAY_MS);
            Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Espera de reintento interrumpida");
        }
    }

    private <T extends Actor> void verificarLiquidacionExitosa(T actor) {
        actor.should(
            seeThat("El pago debe estar liquidado",
                actor1 -> pago.estaLiquidado(),
                is(true)
            )
        );
        logger.info("Verificacion de liquidacion completada - Fecha: {}", pago.getFechaLiquidacion());
    }

    private Map<String, Object> construirPayloadLiquidacion() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("pagoId", pago.getId());
        payload.put("procesadorPagoId", pago.getProcesadorPagoId());
        payload.put("monto", pago.getMonto());
        payload.put("moneda", pago.getMoneda());
        payload.put("cuentaOrigen", pago.getCuentaOrigen());
        payload.put("cuentaDestino", pago.getCuentaDestino());
        payload.put("referenciaExternal", pago.getReferenciaExternal());
        payload.put("fechaProcesamiento", pago.getFechaProcesamiento() != null ? 
            pago.getFechaProcesamiento().toString() : Instant.now().toString());
        payload.put("sistemaOrigen", pago.getSistemaOrigen());
        payload.put("timestamp", Instant.now().toString());
        return payload;
    }

    private String generarClaveLiquidacion() {
        return pago.getIdempotencyKey() + "_LIQ_" + System.currentTimeMillis();
    }

    public String getReferenciaLiquidacion() {
        return referenciaLiquidacion;
    }

    public int getIntentosConectividad() {
        return intentosConectividad;
    }
}


=== ARCHIVO: pom.xml ===
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.pragma</groupId>
    <artifactId>pagos-serenity-tests</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Pagos Serenity Tests</name>
    <description>Pruebas automatizadas del sistema de pagos con Serenity BDD</description>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <serenity.version>4.1.0</serenity.version>
        <cucumber.version>7.15.0</cucumber.version>
        <junit.version>5.10.2</junit.version>
        <selenium.version>4.22.0</selenium.version>
        <rest-assured.version>5.4.0</rest-assured.version>
        <lombok.version>1.18.30</lombok.version>
        <hamcrest.version>2.2</hamcrest.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>net.serenity-bdd</groupId>
            <artifactId>serenity-core</artifactId>
            <version>${serenity.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>net.serenity-bdd</groupId>
            <artifactId>serenity-cucumber</artifactId>
            <version>${serenity.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit</artifactId>
            <version>${cucumber.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>${rest-assured.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.5</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>net.serenity-bdd</groupId>
            <artifactId>serenity-maven-plugin</artifactId>
            <version>${serenity.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.hamcrest</groupId>
            <artifactId>hamcrest</artifactId>
            <version>${hamcrest.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <includes>
                        <include>**/*Test.java</include>
                        <include>**/*Runner.java</include>
                        <include>**/*CucumberTest.java</include>
                    </includes>
                </configuration>
            </plugin>
            <plugin>
                <groupId>net.serenity-bdd</groupId>
                <artifactId>serenity-maven-plugin</artifactId>
                <version>${serenity.version}</version>
                <executions>
                    <execution>
                        <id>serenity-reports</id>
                        <phase>post-integration-test</phase>
                        <goals>
                            <goal>aggregate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

=== ARCHIVO: src/test/java/com/pragma/pagos/models/Pago.java ===
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

=== ARCHIVO: src/test/java/com/pragma/pagos/utils/IdempotenciaHelper.java ===
package com.pragma.pagos.utils;

import com.pragma.pagos.models.Pago;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class IdempotenciaHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdempotenciaHelper.class);
    private static final String KEY_PREFIX = "IDEM";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Map<String, Pago> REGISTRO_IDEMPOTENCIA = new HashMap<>();
    private static final Map<String, Instant> CACHE_EXPIRACION = new HashMap<>();
    private static final long TTL_MINUTOS = 60;

    public static String generarClaveIdempotencia(String cuentaOrigen, BigDecimal monto) {
        String timestamp = Instant.now().format(TIMESTAMP_FORMAT);
        return String.format("%s-%s-%s-%s", KEY_PREFIX, cuentaOrigen, monto.toPlainString(), timestamp);
    }

    public static String generarClaveIdempotencia(String cuentaOrigen, BigDecimal monto, String concepto) {
        String timestamp = Instant.now().format(TIMESTAMP_FORMAT);
        return String.format("%s-%s-%s-%s-%s", KEY_PREFIX, cuentaOrigen, monto.toPlainString(), 
                concepto != null ? concepto : "N/A", timestamp);
    }

    public static boolean esClaveIdempotenciaValida(String claveIdempotencia) {
        return claveIdempotencia != null && claveIdempotencia.startsWith(KEY_PREFIX);
    }

    public static boolean verificarDuplicado(String claveIdempotencia) {
        return REGISTRO_IDEMPOTENCIA.containsKey(claveIdempotencia);
    }

    public static void registrarPago(Pago pago) {
        if (pago != null && pago.getIdempotencyKey() != null) {
            REGISTRO_IDEMPOTENCIA.put(pago.getIdempotencyKey(), pago);
            CACHE_EXPIRACION.put(pago.getIdempotencyKey(), Instant.now().plusSeconds(TTL_MINUTOS * 60));
            LOGGER.debug("Pago registrado con clave de idempotencia: {}", pago.getIdempotencyKey());
        }
    }

    public static Pago obtenerPagoPorClave(String claveIdempotencia) {
        return REGISTRO_IDEMPOTENCIA.get(claveIdempotencia);
    }

    public static boolean verificarYRegistrar(String claveIdempotencia, Pago nuevoPago) {
        if (verificarDuplicado(claveIdempotencia)) {
            LOGGER.warn("Clave de idempotencia duplicada detectada: {}", claveIdempotencia);
            return false;
        }
        registrarPago(nuevoPago);
        return true;
    }

    public static void limpiarRegistrosExpirados() {
        Instant ahora = Instant.now();
        CACHE_EXPIRacion.entrySet().removeIf(entry -> entry.getValue().isBefore(ahora));
        REGISTRO_IDEMPOTENCIA.keySet().removeIf(key -> !CACHE_EXPIRACION.containsKey(key));
    }

    public static Map<String, Object> obtenerMetricasIdempotencia() {
        Map<String, Object> metricas = new HashMap<>();
        metricas.put("totalRegistros", REGISTRO_IDEMPOTENCIA.size());
        metricas.put("ttlMinutos", TTL_MINUTOS);
        return metricas;
    }

    public static void resetearContadorPruebas() {
        REGISTRO_IDEMPOTENCIA.clear();
        CACHE_EXPIRACION.clear();
    }

    public static boolean sonMismosDatos(Pago pago1, Pago pago2) {
        if (pago1 == null || pago2 == null) return false;
        return pago1.getMonto().equals(pago2.getMonto()) &&
               pago1.getCuentaOrigen().equals(pago2.getCuentaOrigen()) &&
               pago1.getCuentaDestino().equals(pago2.getCuentaDestino());
    }

    public static boolean tieneDuplicados(String pagoId, String idempotencyKey) {
        return verificarDuplicado(idempotencyKey);
    }
}

// === ARCHIVO: pom.xml ===
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.pragma</groupId>
    <artifactId>pagos-tests</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Pagos Tests</name>
    <description>Suite de pruebas automatizadas para el sistema de pagos</description>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <serenity.version>4.1.0</serenity.version>
        <cucumber.version>7.15.0</cucumber.version>
        <junit.version>5.10.2</junit.version>
        <selenium.version>4.22.0</selenium.version>
        <rest-assured.version>5.4.0</rest-assured.version>
        <lombok.version>1.18.30</lombok.version>
        <slf4j.version>2.0.13</slf4j.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>net.serenity-bdd</groupId>
            <artifactId>serenity-core</artifactId>
            <version>${serenity.version}</version>
        </dependency>
        <dependency>
            <groupId>net.serenity-bdd</groupId>
            <artifactId>serenity-cucumber</artifactId>
            <version>${serenity.version}</version>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-java</artifactId>
            <version>${cucumber.version}</version>
        </dependency>
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit</artifactId>
            <version>${cucumber.version}</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
        </dependency>
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>${rest-assured.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
            <plugin>
                <groupId>net.serenity-bdd</groupId>
                <artifactId>serenity-maven-plugin</artifactId>
                <version>${serenity.version}</version>
                <executions>
                    <execution>
                        <id>serenity-reports</id>
                        <phase>post-integration-test</phase>
                        <goals>
                            <goal>aggregate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>

// === ARCHIVO: src/test/java/com/pragma/pagos/utils/IdempotenciaHelper.java ===
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

```
