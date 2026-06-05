# Sistema de Gestión de Tutorías Académicas

Monolito por capas desarrollado en **Java 17**, **Spring Boot 3.3.x** y **MongoDB** como parte de la evaluación práctica de Arquitectura de Software. El sistema gestiona la publicación de disponibilidad de docentes, reserva de tutorías por estudiantes, validación de reglas de negocio cruzadas y trazabilidad mediante auditoría y notificaciones.

---

## 1. Arquitectura del Sistema (Estructura por Capas)

El monolito sigue una separación estricta de responsabilidades en 4 capas para garantizar mantenibilidad, testabilidad y aislamiento de la lógica de negocio (DDD/Layered Architecture):

```
+-------------------------------------------------------------+
|                          PRESENTACIÓN                       |  <-- Controladores REST, DTOs, Manejo de Errores
+-------------------------------------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|                           APLICACIÓN                        |  <-- Casos de Uso, Coordinación de Transacciones
+-------------------------------------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|                             DOMINIO                         |  <-- Entidades, Value Objects, Reglas de Negocio
+-------------------------------------------------------------+
                               ^
                               | (Inversión de Dependencias)
+-------------------------------------------------------------+
|                         INFRAESTRUCTURA                     |  <-- MongoDB Adapters, Seeder, Configuraciones
+-------------------------------------------------------------+
```

### Paquetes Clave
*   **`com.tutorias.domain`**: Contiene la lógica del negocio pura. Las clases no están acopladas a frameworks ni anotaciones de persistencia.
    *   `entities/`: Entidades centrales (`Usuario`, `Estudiante`, `Docente`, `Materia`, `Disponibilidad`, `Tutoria`, `HistorialTutoria`, `Notificacion`).
    *   `valueobjects/`: Objetos de valor autocontenidos y validados (`CorreoInstitucional`, `MotivoTutoria`, `RangoHorario`, `FechaHoraTutoria`).
    *   `enums/`: Estados y roles (`EstadoTutoria`, `EstadoUsuario`, `RolUsuario`).
    *   `services/`: `ValidadorAgendaTutorias` evalúa reglas complejas multientidad (cruces de horarios, duplicados semanales).
    *   `repositorios/`: Contratos de repositorio abstractos (interfaces).
*   **`com.tutorias.application`**: Orquesta el flujo de datos invocando las validaciones del dominio y persistiendo los resultados.
    *   `servicios/`: `TutoriaApplicationService`, `DisponibilidadApplicationService` y `UsuarioApplicationService`.
*   **`com.tutorias.infrastructure`**: Implementación de adaptadores técnicos.
    *   `documentos/`: Documentos mapeados a colecciones de MongoDB (`TutoriaDocument`, etc.).
    *   `repositorios/`: Repositorios técnicos de Spring Data Mongo.
    *   `adaptadores/`: Implementa las interfaces del dominio delegando a Spring Data y mapeando a entidades de dominio.
    *   `semillas/`: `DatabaseSeeder` puebla la base de datos automáticamente con usuarios de prueba.
*   **`com.tutorias.presentation`**: Expone las interfaces REST externas.
    *   `controladores/`: Controladores REST documentados con OpenAPI.
    *   `dto/`: Clases de transporte de entrada y salida de datos (JSR-380 validation).
    *   `error/`: `GlobalExceptionHandler` intercepta excepciones y formatea respuestas legibles.

---

## 2. Lenguaje Ubicuo Estandarizado

Toda la aplicación utiliza estrictamente la terminología oficial del dominio en código, base de datos y documentación:

*   **Tutoría** (`Tutoria`): Cita académica agendada.
*   **Disponibilidad** (`Disponibilidad`): Horario publicado por el docente.
*   **Estudiante** (`Estudiante`): Perfil del alumno.
*   **Docente** (`Docente`): Perfil del profesor.
*   **Administrador** (`Administrador`): Supervisor general de cuentas.
*   **Motivo** (`MotivoTutoria`): Razón de la consulta.
*   **Estado de tutoría** (`EstadoTutoria`): `PENDIENTE`, `CONFIRMADA`, `ASISTIDA`, `INASISTENCIA`, `CANCELADA`.
*   **Historial** (`HistorialTutoria`): Bitácora de cambios del estado.
*   **Correo institucional** (`CorreoInstitucional`): Validación de formato y dominio.

---

## 3. Reglas de Negocio Protegidas

*   **Autenticación y Cuentas**: Solo se permiten correos bajo los dominios `@uce.edu.ec` y `@universidad.edu.ec`. Usuarios en estado `INACTIVO` no pueden ingresar.
*   **Disponibilidad Docente**: No se permiten solapamientos de horarios para el mismo docente. Una disponibilidad ya reservada no puede eliminarse ni editarse.
*   **Cruces y Duplicidad**:
    *   Un estudiante no puede agendar más de una tutoría con el mismo docente para la misma materia en la misma semana.
    *   Un estudiante no puede cruzarse de horario con otra tutoría activa.
    *   Un docente no puede cruzarse de horario con otra tutoría activa.
    *   No se puede agendar tutorías en fechas pasadas.
*   **Regla de Cancelación de 24 Horas**: Un estudiante solo puede cancelar una tutoría si faltan 24 horas o más para su inicio. Si faltan menos de 24 horas, el sistema arroja un error controlado de regla de negocio (`DomainException`).
*   **Trazabilidad**: Cada cambio de estado registra quién hizo el cambio, cuándo y el detalle de la transición en el historial.

---

## 4. Ejecución del Proyecto en Local

### Prerrequisitos
1. **Java 17** instalado — verificar con `java -version`.
2. **Maven 3.9+** configurado — verificar con `mvn -version`.
3. **MongoDB** disponible en una de estas dos formas:
   - **Local**: MongoDB corriendo en `localhost:27017` (configuración por defecto).
   - **Atlas**: Editar `src/main/resources/application.properties` y reemplazar la URI:
     ```properties
     spring.data.mongodb.uri=mongodb+srv://<usuario>:<password>@<cluster>.mongodb.net/tutorias_db?retryWrites=true&w=majority&appName=Cluster0
     ```

### Levantar el Servidor

En el directorio raíz del proyecto, ejecutar:

```bash
mvn spring-boot:run
```

El servidor arranca en el puerto **8080**. La primera vez que corra con la base de datos vacía, el `DatabaseSeeder` poblará automáticamente los datos de prueba.

### Visualización e Interacción

El sistema ofrece dos interfaces de acceso:

| Interfaz | URL | Descripción |
| :--- | :--- | :--- |
| **Dashboard Web** | [http://localhost:8080](http://localhost:8080) | Interfaz visual con simulador de roles (Estudiante / Docente / Admin) |
| **Swagger UI** | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) | Documentación interactiva de todos los endpoints REST |

---


## 5. Datos de Prueba Iniciales (Semillas)

La base de datos se poblará automáticamente en el primer inicio con las siguientes cuentas y datos de simulación:

| Usuario | Correo Institucional | Contraseña | Rol | Datos Adicionales |
| :--- | :--- | :--- | :--- | :--- |
| **Antony Coello** | `estudiante1@uce.edu.ec` | `estudiante123` | `ESTUDIANTE` | Ing. Computación |
| **Maria Lopez** | `estudiante2@universidad.edu.ec` | `estudiante123` | `ESTUDIANTE` | Ing. Química |
| **Dr. Edison Mora** | `docente1@uce.edu.ec` | `docente123` | `DOCENTE` | Cubículo 402 |
| **Dra. Ana Castro** | `docente2@universidad.edu.ec` | `docente123` | `DOCENTE` | Cubículo 105 |
| **Admin Tutorias** | `admin@uce.edu.ec` | `admin123` | `ADMINISTRADOR` | Supervisor general |

### Materias Sembradas
*   `ARQ-001` - Arquitectura de Software
*   `BD2-002` - Base de Datos II
*   `WEB-003` - Programación Web
