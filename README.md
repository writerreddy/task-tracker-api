# Task Tracker API

A production-style Spring Boot REST API for the SymphonyAI take-home assignment using **Java 8** and the preferred Spring Boot backend stack. It manages Projects, Users, and Tasks with validation, structured error handling, H2 support, OpenAPI documentation, Swagger UI, unit tests, integration tests, Docker support, and optional pagination/sorting.

## Tech stack

- Java 8
- Spring Boot 2.7.18
- Spring Web
- Spring Data JPA / Hibernate
- H2 in-memory database
- Bean Validation
- JUnit 5 + Mockito
- Spring Boot Test + MockMvc
- OpenAPI 3.0 YAML
- Swagger UI via springdoc-openapi
- Docker + docker-compose
- Maven

## Assignment coverage

### Required

- Spring Boot project with Maven
- In-memory H2 database
- JPA/Hibernate entities and relationships
- Service layer separated from controllers
- Externalized config from `application.properties`
- `/api/info` endpoint
- DTO validation
- Global exception handling
- Unit tests using JUnit 5 and Mockito
- `openapi.yaml` in project root
- `README.md` with clear setup and curl examples

### Bonus included

- Dockerfile
- docker-compose.yml
- Integration tests with `@SpringBootTest` and `MockMvc`
- Pagination and sorting support on list endpoints
- Swagger UI auto-generated from the application

## Project structure

```text
task-tracker-api-java8-top-tier/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── openapi.yaml
├── README.md
├── src/main/java/com/example/tasktracker
├── src/main/resources/application.properties
└── src/test/java/com/example/tasktracker
```

## Prerequisites

Install these tools:

- JDK 8
- Maven 3.8+
- IntelliJ IDEA
- Docker Desktop (optional, for container run)

Check local setup:

```bash
java -version
mvn -version
docker --version
```

## Open in IntelliJ

1. Extract the ZIP.
2. Open IntelliJ IDEA.
3. Click **Open** and choose the folder containing `pom.xml`.
4. Let IntelliJ import Maven dependencies.
5. Set **Project SDK = Java 8**.
6. Run `TaskTrackerApiApplication`.

## Run locally

```bash
mvn clean test
mvn spring-boot:run
```

Available URLs:

- API: [http://localhost:8080](http://localhost:8080)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## H2 login

- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: leave empty

## Run with Docker

Build and run:

```bash
docker compose up --build
```

Or with older Docker Compose:

```bash
docker-compose up --build
```

## Configuration properties

The project reads these from `application.properties`:

```properties
app.name=Task Tracker API
app.version=1.0.0
task.default-priority=MEDIUM
pagination.default-page-size=10
```

## Endpoints

### Info
- `GET /api/info`

### Projects
- `POST /api/projects`
- `GET /api/projects`
- `GET /api/projects/{id}`
- `PUT /api/projects/{id}`
- `DELETE /api/projects/{id}`
- `GET /api/projects/{id}/tasks`

### Users
- `POST /api/users`
- `GET /api/users`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

### Tasks
- `POST /api/tasks`
- `GET /api/tasks`
- `GET /api/tasks/{id}`
- `PUT /api/tasks/{id}`
- `DELETE /api/tasks/{id}`

## Pagination and sorting

List endpoints support optional pagination and sorting.

Examples:

```bash
curl "http://localhost:8080/api/projects?page=0&size=5&sortBy=name&sortDir=asc"
curl "http://localhost:8080/api/users?page=0&size=10&sortBy=name&sortDir=desc"
curl "http://localhost:8080/api/tasks?status=IN_PROGRESS&projectId=1&page=0&size=10&sortBy=dueDate&sortDir=asc"
```

If `page` and `size` are omitted, the API returns the normal non-paginated list for backward compatibility.

## Sample curl requests

### Create project

```bash
curl -X POST http://localhost:8080/api/projects   -H "Content-Type: application/json"   -d '{"name":"Media Platform","description":"Task tracker backend"}'
```

### Create user

```bash
curl -X POST http://localhost:8080/api/users   -H "Content-Type: application/json"   -d '{"name":"Alice","email":"alice@example.com","role":"DEVELOPER"}'
```

### Create task

```bash
curl -X POST http://localhost:8080/api/tasks   -H "Content-Type: application/json"   -d '{"title":"Implement dashboard","status":"IN_PROGRESS","priority":"HIGH","projectId":1,"assigneeId":1,"dueDate":"2026-05-20"}'
```

### Filter tasks

```bash
curl "http://localhost:8080/api/tasks?status=TODO"
curl "http://localhost:8080/api/tasks?projectId=1"
```

## Tests

Run all tests:

```bash
mvn test
```

Included test types:

- Service-layer unit tests for Project, User, and Task services
- Integration tests using `MockMvc`
- Validation error checks
- `/api/info` contract check

## Design decisions

- DTOs are used to separate persistence models from API contracts.
- External configuration is injected rather than hardcoded, matching the assignment requirement.
- Default task priority is resolved from `task.default-priority`.
- Pagination is optional and uses `page`, `size`, `sortBy`, and `sortDir` query parameters.
- Swagger UI is included to improve reviewer experience.

## Submission checklist

Before pushing to GitHub:

```bash
mvn clean test
```

Also verify:

- Java 8 is selected
- `openapi.yaml` is in the root
- `README.md` is updated
- `.idea/` and `target/` are not committed
- API starts successfully in IntelliJ and with Maven
