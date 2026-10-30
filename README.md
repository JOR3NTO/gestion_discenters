# gestion_discenters

API REST (Spring Boot 3.5, Java 21, MySQL) para gestionar estudiantes, salones, horarios de clase, asignación de salones y quizzes de evaluación.

## Requisitos

- JDK 21 o superior (el `pom.xml` compila con `java.version=21`; se probó con JDK 25).
- Maven 3.9+ (o el `mvnw` incluido).
- MySQL 8 en `localhost:3306` (solo para ejecutar la aplicación; las pruebas usan H2 en memoria).

## Configuración

La conexión se define en [application.properties](src/main/resources/application.properties) y se puede sobrescribir con variables de entorno:

| Variable      | Valor por defecto                                                                  |
|---------------|------------------------------------------------------------------------------------|
| `DB_URL`      | `jdbc:mysql://localhost:3306/sistema_examenes?createDatabaseIfNotExist=true`       |
| `DB_USER`     | `root`                                                                             |
| `DB_PASSWORD` | *(vacía)*                                                                          |

PowerShell:

```powershell
$env:DB_PASSWORD = "tu_clave"
mvn spring-boot:run
```

El esquema se crea/actualiza solo (`ddl-auto=update`). Al primer arranque se insertan los roles `ADMIN` (1), `PROFESOR` (2) y `ESTUDIANTE` (3).

## Pruebas

```bash
mvn test
```

Usan H2 en memoria ([src/test/resources/application.properties](src/test/resources/application.properties)); no requieren MySQL.
Las pruebas de integración están en [GestionApiTests](src/test/java/com/sistema/examenes/sistema_examenes_backend/GestionApiTests.java).

## Arquitectura

Paquete base `com.sistema.examenes`:

| Paquete           | Contenido                                                              |
|-------------------|------------------------------------------------------------------------|
| `entidades`       | Entidades JPA y validaciones (`jakarta.validation`)                    |
| `repositorios`    | Interfaces `JpaRepository`                                             |
| `servicios`       | Interfaces de servicio; `servicios.impl` tiene la lógica de negocio    |
| `controladores`   | Controladores REST bajo `/api/...`                                     |
| `dto`             | Objetos de petición/respuesta (`record`)                               |
| `excepciones`     | Excepciones de negocio y `GlobalExceptionHandler`                      |
| `configuracion`   | Cifrador BCrypt y carga inicial de roles                               |

### Modelo de datos

- **Usuario** ⟷ **Rol** a través de **UsuarioRol** (usuarios del sistema; la contraseña se guarda con BCrypt y nunca se devuelve).
- **Estudiante** (`matricula` y `email` únicos).
- **Salon** (`nombre` único, `capacidad` ≥ 1).
- **HorarioClase** (`diaSemana`, `horaInicio`, `horaFin`).
- **AsignacionSalon**: salón + horario (único) + estudiantes inscritos.
- **Quiz** → **Pregunta** → **Opcion**, e **IntentoQuiz** (resultado de un estudiante).

## Reglas de negocio

- Matrícula, email de estudiante, nombre de salón, username y email de usuario no se pueden repetir.
- `horaInicio` debe ser anterior a `horaFin`.
- Un salón no puede tener dos asignaciones con horarios que se crucen el mismo día (10:00–12:00 no cruza con 08:00–10:00).
- Un salón no admite más estudiantes que su capacidad.
- Un estudiante no puede inscribirse en dos asignaciones con horarios cruzados, ni dos veces en la misma.
- Cada pregunta de un quiz necesita al menos dos opciones y exactamente una correcta.
- Las opciones correctas solo se aceptan al crear el quiz; nunca se devuelven en las respuestas.
- Puntaje de un quiz: `correctas / total × 5` (redondeado a 2 decimales). Un quiz con intentos no se puede eliminar.

## Endpoints

Errores: JSON `{timestamp, status, error, mensaje, detalle?}`. `400` datos inválidos, `404` no existe, `409` regla de negocio o restricción de BD.

### Estudiantes — `/api/estudiantes`
`POST` · `GET` · `GET /{id}` · `PUT /{id}` · `DELETE /{id}`

```json
{ "nombre": "Ana", "apellido": "Gil", "email": "ana@mail.com", "matricula": "2024001" }
```

### Salones — `/api/salones`
`POST` · `GET` · `GET /{id}` · `PUT /{id}` · `DELETE /{id}`

```json
{ "nombre": "A-101", "capacidad": 30 }
```

### Horarios — `/api/horarios`
`POST` · `GET` · `GET /{id}` · `PUT /{id}` · `DELETE /{id}`

```json
{ "diaSemana": "LUNES", "horaInicio": "08:00", "horaFin": "10:00" }
```

Días válidos: `LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO`.

### Asignaciones de salón — `/api/asignaciones`

| Método | Ruta                                | Descripción                                |
|--------|-------------------------------------|--------------------------------------------|
| POST   | `/`                                 | Asigna un salón a un horario               |
| GET    | `/`, `/{id}`                        | Lista / detalle (incluye estudiantes)      |
| GET    | `/salon/{salonId}`                  | Asignaciones de un salón                   |
| GET    | `/estudiante/{estudianteId}`        | Clases de un estudiante                    |
| POST   | `/{id}/estudiantes/{estudianteId}`  | Inscribe un estudiante                     |
| DELETE | `/{id}/estudiantes/{estudianteId}`  | Retira un estudiante                       |
| DELETE | `/{id}`                             | Elimina la asignación                      |

```json
{ "salonId": 1, "horarioId": 1 }
```

### Quizzes — `/api/quizzes`

| Método | Ruta                                  | Descripción                          |
|--------|---------------------------------------|--------------------------------------|
| POST   | `/`                                   | Crea quiz con preguntas y opciones   |
| GET    | `/`, `/{id}`                          | Lista / detalle (sin respuestas)     |
| PUT    | `/{id}`                               | Reemplaza título, estado y preguntas |
| DELETE | `/{id}`                               | Elimina (si no tiene intentos)       |
| POST   | `/{id}/responder`                     | Califica y guarda un intento         |
| GET    | `/{id}/intentos`                      | Intentos de un quiz                  |
| GET    | `/intentos/estudiante/{estudianteId}` | Intentos de un estudiante            |

Crear:

```json
{
  "titulo": "Java básico",
  "preguntas": [
    { "enunciado": "2+2", "opciones": [ { "texto": "3" }, { "texto": "4", "correcta": true } ] }
  ]
}
```

Responder (`preguntaId → opcionId`):

```json
{ "estudianteId": 1, "respuestas": { "10": 21, "11": 24 } }
```

Respuesta: `{ "intentoId": 1, "total": 2, "correctas": 1, "puntaje": 2.5 }`.

### Usuarios y roles — `/api/usuarios`, `/api/roles`
`POST /api/usuarios` · `GET` · `GET /{id}` · `DELETE /{id}` · `GET /api/roles`

```json
{ "username": "jorge", "password": "secreto123", "nombre": "Jorge", "email": "j@mail.com", "rolIds": [1] }
```

## Cambios realizados en `dev`

- **Corregido**: la clase `SistemaExamenesBackendApplication` estaba en un subpaquete y Spring no encontraba entidades ni repositorios; se movió al paquete raíz `com.sistema.examenes`.
- **Corregido**: las entidades no tenían getters/setters (no se podían serializar) y `Usuario` tenía un `;;` sobrante. Se evitaron ciclos JSON entre `Usuario`, `UsuarioRol` y `Rol`.
- **Nuevo**: módulos de estudiantes, salones, horarios, asignación de salones (con cupo y cruces), quizzes con calificación automática y usuarios con roles (los de las ramas `feature/*` eran solo esqueletos o estaban vacíos; se reescribieron con validación, 404/409 y constructor injection).
- **Seguridad de configuración**: la contraseña de MySQL ya no está en el repositorio (variable `DB_PASSWORD`); se eliminó la propiedad inválida `spring.jpa.dialect` y se desactivó `open-in-view`.
- **Pruebas**: H2 en memoria y 8 pruebas de integración.
- Dependencias añadidas: `spring-security-crypto` (BCrypt) y `h2` (test).

## Pendiente

- Autenticación y autorización por rol (Spring Security + JWT); hoy todos los endpoints son públicos.
- Actualización de usuarios (`PUT`) y gestión de roles (alta/baja).
- Paginación en los listados.
- Documentación OpenAPI/Swagger.
- Limitar un único intento por estudiante y quiz, o un tiempo máximo (según se defina).
