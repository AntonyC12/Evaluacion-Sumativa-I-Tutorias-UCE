# Sistema de Gestión de Tutorías Académicas (Rama: Monolito-Capas)

Este repositorio contiene la segunda fase de evolución del **Sistema de Gestión de Tutorías Académicas** para la Universidad Central del Ecuador (UCE). Esta versión representa una refactorización completa del diseño original "Spaghetti" hacia un **Monolito por Capas** estructurado, limpio y desacoplado, utilizando **Spring Boot** y **Spring Data MongoDB**.

El objetivo de esta versión es aplicar una separación rigurosa de responsabilidades (Separation of Concerns) e introducir la inversión de dependencias en la persistencia (**Opción A**), asegurando que las reglas de negocio estén centralizadas y protegidas dentro de un **Dominio puro**, libre de acoplamientos técnicos de base de datos.

---

## 1. Arquitectura Lógica y Dirección de Dependencias

El sistema backend se organiza en cuatro capas lógicas principales con dependencias estrictamente unidireccionales y descendentes:

```
┌─────────────────────────────────────────────────────────┐
│                   Presentación (REST)                   │
└────────────────────────────┬────────────────────────────┘
                             │ (Invoca)
┌────────────────────────────▼────────────────────────────┐
│                  Aplicación (Servicios)                 │
└────────────────────────────┬────────────────────────────┘
                             │ (Usa contratos / interfaces)
┌────────────────────────────▼────────────────────────────┐
│                    Dominio (Modelos)                    │
└────────────────────────────▲────────────────────────────┘
                             │ (Implementa contratos)
┌────────────────────────────┴────────────────────────────┐
│              Infraestructura (Persistencia)             │
└─────────────────────────────────────────────────────────┘
```

1. **Dominio (`com.tutorias.domain`):** Es el corazón del sistema. Contiene las reglas del negocio, validaciones centrales de comportamiento, objetos de valor y las interfaces (contratos) de los repositorios. Es **100% puro**, lo que significa que no contiene anotaciones técnicas de Spring ni anotaciones de MongoDB (como `@Document` o `@Id`).
2. **Aplicación (`com.tutorias.application`):** Coordina los casos de uso del sistema. Carga las entidades del dominio usando los contratos de repositorio, ejecuta los métodos de comportamiento del dominio para validar las reglas, persiste los resultados de forma atómica y despacha notificaciones y logs de auditoría.
3. **Presentación (`com.tutorias.presentation`):** Es la puerta de entrada HTTP. Contiene los controladores REST anotados con `@RestController`, los DTOs (`LoginRequest`, `TutoriaRequest`, etc.) para el mapeo JSON, y un manejador global de excepciones (`GlobalExceptionHandler`) que traduce los fallos de negocio en códigos de estado `400 Bad Request` limpios para el frontend.
4. **Infraestructura (`com.tutorias.infrastructure`):** Contiene los detalles técnicos. Define los documentos físicos de MongoDB (`@Document`), las interfaces de Spring Data `MongoRepository`, los mapeadores de persistencia (`PersistenciaMapper`) y la implementación concreta de los repositorios de dominio, resolviendo la inversión de dependencias.

---

## 2. Mapa de Estructura del Proyecto

La estructura física del proyecto organizada bajo el paquete raíz `com.tutorias` es la siguiente:

```
Evaluacion-Sumativa-I-Parte-Practica/
│
├── pom.xml                                           # Configuración de Spring Boot 3.2.5 y dependencias
├── README.md                                         # Esta documentación detallada
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── tutorias/
    │   │           ├── TutoriasCapasApplication.java # Punto de arranque del servidor Spring Boot
    │   │           │
    │   │           ├── domain/                       # [CAPA DE DOMINIO - 100% PURA]
    │   │           │   ├── model/                    # Entidades puras (Usuario, Tutoria, etc.)
    │   │           │   ├── valueobject/              # Objetos de Valor (CorreoInstitucional, etc.)
    │   │           │   ├── repository/               # Contratos/Interfaces de persistencia de dominio
    │   │           │   └── exception/                # DomainException para reglas de negocio
    │   │           │
    │   │           ├── application/                  # [CAPA DE APLICACIÓN - CASOS DE USO]
    │   │           │   └── service/                  # Servicios (TutoriaService, AuthService, etc.)
    │   │           │
    │   │           ├── presentation/                 # [CAPA DE PRESENTACIÓN - REST API]
    │   │           │   ├── controller/               # Controladores REST y GlobalExceptionHandler
    │   │           │   └── dto/                      # Data Transfer Objects estables para JSON
    │   │           │
    │   │           └── infrastructure/               # [CAPA DE INFRAESTRUCTURA - DETALLES TÉCNICOS]
    │   │               ├── repository/               # Implementaciones de las interfaces de dominio
    │   │               │   ├── mongo/                # Spring Data interfaces e infra Mongo
    │   │               │   │   ├── document/         # Documentos de persistencia (@Document)
    │   │               │   │   └── mapper/           # Mapeador de datos (Persistencia <-> Dominio)
    │   │               │   └── ...RepositoryImpl.java
    │   │               └── config/                   # Sembrador de datos iniciales (DataSeeder)
    │   │
    │   └── resources/
    │       ├── application.properties                # Configuración de puerto y URI de MongoDB
    │       └── static/
    │           └── index.html                        # Cliente SPA Glassmorphism (Externo al Backend)
    │
    └── test/
        └── java/
            └── com/
                └── tutorias/
                    └── domain/
                        └── model/
                            └── TutoriaTest.java      # Pruebas unitarias de las reglas del dominio
```

---

## 3. Especificación de Validaciones por Capa

Para mantener la cohesión y evitar que la capa de aplicación actúe como un "cajón de sastre", las validaciones se distribuyen estrictamente de la siguiente manera:

* **Presentación (DTOs y Controladores):**
  - Campos obligatorios nulos o vacíos en peticiones.
  - Validación sintáctica y de tipos del JSON.
* **Aplicación (Servicios):**
  - Existencia real de las entidades relacionadas en base de datos (ej. verificar si el ID del estudiante, el docente o la materia existen antes de agendar).
  - Carga y recuperación de entidades del dominio.
  - Comprobación de límites lógicos cruzados (ej. consultar duplicados semanales o solapamientos en base a múltiples registros).
  - Persistencia de transacciones, logs de auditoría (historial) y disparo de notificaciones.
* **Dominio (Entidades y Objetos de Valor):**
  - Formato y sufijo obligatorio del correo institucional (`@uce.edu.ec`).
  - Restricción temporal de fecha y hora futura para nuevas tutorías.
  - Sanidad de horas de disponibilidad (`horaInicio < horaFin`).
  - Ciclo de vida y transiciones válidas de estados de la tutoría.
  - Regla de las **24 horas de anticipación** real para cancelaciones realizadas por estudiantes (usando `Duration` de Java).

---

## 4. Reglas de Negocio Implementadas

1. **RN-01 (Correo Institucional):** Solo se permite el acceso e inicio de sesión a correos que pertenezcan estrictamente al dominio de la Universidad Central del Ecuador (`@uce.edu.ec`).
2. **RN-02 (Cuentas Activas):** El sistema bloquea el inicio de sesión y cualquier operación a usuarios marcados con estado `Inactivo`.
3. **RN-03 (Horarios de Docente Coherentes):** Un docente solo puede registrar bloques de disponibilidad donde la hora de inicio sea menor a la hora de fin.
4. **RN-04 (No Solapamiento de Disponibilidad):** Un docente no puede registrar bloques horarios disponibles que se crucen en fecha y hora con disponibilidades existentes de sí mismo.
5. **RN-05 (Protección de Horarios Reservados):** No se permite modificar ni eliminar bloques de disponibilidad que hayan cambiado a estado `Reservado` (agendados por un estudiante).
6. **RN-06 (Fechas Futuras):** Las tutorías solo se pueden agendar para fechas y horas futuras con respecto al tiempo real de ejecución del servidor.
7. **RN-07 (Límite Semanal por Materia/Docente):** Un estudiante no puede agendar más de una tutoría activa con el **mismo docente** y en la **misma materia** dentro de la **misma semana del año** (semana calendario calculada de lunes a domingo bajo la norma ISO-8601).
8. **RN-08 (No Solapamiento del Estudiante):** El sistema impide registrar una tutoría si el estudiante ya cuenta con otra cita activa (estados `Pendiente` o `Confirmada`) cuyos horarios se crucen.
9. **RN-09 (Estados y Transiciones Terminales):** Una tutoría inicia en `Pendiente`. Puede confirmarse (`Confirmada`), cancelarse (`Cancelada`), o marcarse asistencia/inasistencia (`Asistida`, `Inasistencia`). Ningún estado terminal (`Asistida`, `Inasistencia`, `Cancelada`) puede revertirse o cambiar a otro estado.
10. **RN-10 (Regla de Cancelación de 24 Horas):** El estudiante puede cancelar su tutoría con al menos **24 horas o más** de anticipación del inicio de la cita. Si falta menos de 24 horas, el sistema arrojará un error de dominio. Los docentes y administradores tienen permitido cancelar la tutoría en cualquier momento.
11. **RN-11 (Trazabilidad e Historial):** Cada transición de estado (creación, confirmación, asistencia, cancelación) guarda automáticamente un registro inmutable en la colección `historial_cambios` con el autor, fecha/hora, descripción y estados involucrados.
12. **RN-12 (Notificaciones Simuladas):** Cada evento importante inserta un registro de notificación en la base de datos simulando el envío de alertas físicas al correo institucional.

---

## 5. Endpoints de la API REST

Los controladores REST de la capa de presentación exponen los siguientes contratos JSON estables:

### Autenticación
* **`POST /api/auth/login`**
  - *Request Body:* `{"correoInstitucional": "...", "contrasena": "..."}`
  - *Response:* Documento del usuario autenticado.

### Usuarios
* **`GET /api/usuarios`** (Permite filtro query `?rol=docente` u otros)
* **`POST /api/usuarios`** (Crea usuario y su perfil extendido)
* **`PUT /api/usuarios/{id}/toggle`** (Activa/Desactiva cuenta)
* **`GET /api/estudiantes/usuario/{userId}`** (Obtiene perfil de estudiante)
* **`GET /api/docentes/usuario/{userId}`** (Obtiene perfil de docente)

### Materias
* **`GET /api/materias`** (Lista todas las materias de la facultad)

### Disponibilidad
* **`GET /api/disponibilidades`** (Permite filtros: `?estado=Disponible&docenteId=...&materiaId=...&fecha=...`)
* **`POST /api/disponibilidades`**
  - *Request Body:* `{"docenteId": "...", "materiaId": "...", "fecha": "YYYY-MM-DD", "horaInicio": "HH:MM", "horaFin": "HH:MM"}`
* **`DELETE /api/disponibilidades/{id}`** (Borra disponibilidad no reservada)

### Tutorías
* **`GET /api/tutorias`** (Filtra por `estudianteId`, `docenteId`, `estado`, y por coincidencia de nombres `estudianteNombre`, `docenteNombre` con soporte regex insesitivo)
* **`POST /api/tutorias`** (Agenda tutoría)
  - *Request Body:* `{"estudianteId": "...", "disponibilidadId": "...", "motivo": "..."}`
* **`PUT /api/tutorias/{id}/status`** (Cambia el estado de la tutoría)
  - *Request Body:* `{"nuevoEstado": "...", "observacion": "...", "usuarioResponsable": "email_de_usuario"}`
* **`GET /api/tutorias/{id}/historial`** (Lista logs de trazabilidad)

### Notificaciones
* **`GET /api/notificaciones?destinatario={correo}`** (Lista alertas del usuario)

---

## 6. Credenciales de Prueba y Datos Semilla

Al arrancar la aplicación, se cargan por defecto los siguientes datos iniciales si la base de datos se encuentra vacía:

### Cuentas de Acceso (Contraseñas por defecto):

| Rol | Correo Institucional | Contraseña | Detalles |
| :--- | :--- | :--- | :--- |
| **Administrador** | `admin@uce.edu.ec` | `admin123` | Control de usuarios y auditoría global |
| **Docente 1** | `docente1@uce.edu.ec` | `docente123` | Carlos Perez (Dept. Computación) |
| **Docente 2** | `docente2@uce.edu.ec` | `docente123` | Maria Flores (Dept. Redes) |
| **Estudiante 1** | `estudiante1@uce.edu.ec` | `estudiante123` | Antony Coello (Carrera Computación, Nivel 7) |
| **Estudiante 2** | `estudiante2@uce.edu.ec` | `estudiante123` | Juan Lopez (Carrera Computación, Nivel 7) |
| **Estudiante Inactivo**| `inactivo@uce.edu.ec` | `inactivo123` | Cuenta suspendida |

### Materias:
* `1` - `AS-001` - Arquitectura de Software
* `2` - `BD-002` - Base de Datos
* `3` - `RC-003` - Redes de Computadoras

---

## 7. Instrucciones para la Ejecución Local

### Requisitos Previos:
1. **Java Development Kit (JDK) 17** instalado y configurado.
2. **Apache Maven 3.6+** instalado y configurado.
3. Una instancia de **MongoDB** activa:
   - **Local:** MongoDB corriendo por defecto en `localhost:27017` (URI configurada en properties).
   - **Atlas:** Si utilizas MongoDB Atlas, edita el archivo `src/main/resources/application.properties` y reemplaza la URI local con tu cadena de conexión.

### Paso 1: Compilar y Ejecutar Pruebas de Dominio
En la terminal, ejecuta:
```bash
mvn clean test
```
Esto validará la compilación del código y ejecutará los 9 tests unitarios críticos que comprueban la regla de 24 horas y el flujo de transiciones.

### Paso 2: Arrancar la Aplicación
Levanta el servidor Spring Boot local en el puerto `8080` ejecutando:
```bash
mvn spring-boot:run
```

### Paso 3: Abrir la Interfaz de Usuario
Abre el navegador web e ingresa a:
```
http://localhost:8080/index.html
```
Podrás usar la **Dev Toolbar** ubicada en la esquina inferior derecha para iniciar sesión de forma automática en cualquier perfil y realizar simulaciones de todo el flujo en capas.

---

## 8. Resolución de Problemas Comunes (Troubleshooting)

1. **Errores del IDE de Java (`enum` is a reserved keyword / The import cannot be resolved):**
   - El código fue refactorizado para usar `com.tutorias.domain.enums` en lugar de `enum` (que es una palabra reservada en Java).
   - Sin embargo, los IDEs a veces mantienen indexado el directorio viejo o se marean.
   - **Solución:** Ejecuta *Maven -> Update Project* (Eclipse) o *Java: Clean Language Server Workspace* (VS Code).
   - **Confirmación:** Siempre confía en la ejecución desde terminal (`mvn clean test`). Si en terminal compila y los tests pasan (30/30), el problema es solo del caché del IDE.

2. **Error `[object Object]` o "Invalid character found in the request target" en la API:**
   - Ocurre si previamente habías levantado la versión "Código Spaghetti" y el navegador aún conserva el objeto de sesión viejo en memoria (`sessionStorage`), el cual almacenaba el correo institucional con una estructura anidada.
   - **Solución:** Abre la app (`http://localhost:8080`), presiona `F12`, ve a la pestaña **Application -> Session Storage**, elimínalo todo y refresca la página con `Ctrl + Shift + R` (Hard Refresh). El nuevo código gestionará correctamente la sesión.
   - El `index.html` ha sido parcheado para detectar automáticamente esto, pero el "Hard Refresh" limpia la caché del navegador.
