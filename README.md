
# Task Tracker API

A RESTful Spring Boot application built for the SymphonyAI Media take-home assignment.

This project manages:
- Projects
- Users
- Tasks

It supports CRUD operations, validation, exception handling, OpenAPI documentation, Swagger UI, unit tests, integration tests, pagination, and filtering.

---

# Tech Stack

- Java 8
- Spring Boot 2.7
- Spring Data JPA (Hibernate)
- H2 In-Memory Database
- Maven
- JUnit 5 + Mockito
- MockMvc Integration Tests
- OpenAPI 3.0
- Swagger UI

---

# Project Structure

```text
task-tracker-api/
├── src/
├── pom.xml
├── README.md
├── openapi.yaml
└── .gitignore
````

---

# Run the Application

## Prerequisites

Install:

* Java 8+
* Maven
* IntelliJ IDEA (recommended)

Verify installation:

```bash
java -version
mvn -version
```

---

## Run Locally

```bash
mvn clean test
mvn spring-boot:run
```
---

# Swagger UI

Open Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---

# H2 Database Console

Open:

```text
http://localhost:8080/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:mem:taskdb
Username: sa
Password:
```

(leave password empty)

---

# API Endpoints

## Info

| Method | Endpoint    |
| ------ | ----------- |
| GET    | `/api/info` |

---

## Projects

| Method | Endpoint                   |
| ------ | -------------------------- |
| POST   | `/api/projects`            |
| GET    | `/api/projects`            |
| GET    | `/api/projects/{id}`       |
| PUT    | `/api/projects/{id}`       |
| DELETE | `/api/projects/{id}`       |
| GET    | `/api/projects/{id}/tasks` |

---

## Users

| Method | Endpoint          |
| ------ | ----------------- |
| POST   | `/api/users`      |
| GET    | `/api/users`      |
| GET    | `/api/users/{id}` |
| PUT    | `/api/users/{id}` |
| DELETE | `/api/users/{id}` |

---

## Tasks

| Method | Endpoint          |
| ------ | ----------------- |
| POST   | `/api/tasks`      |
| GET    | `/api/tasks`      |
| GET    | `/api/tasks/{id}` |
| PUT    | `/api/tasks/{id}` |
| DELETE | `/api/tasks/{id}` |

---

# Pagination and Filtering

Examples:

```bash
GET /api/projects?page=0&size=5

GET /api/users?page=0&size=10&sortBy=name&sortDir=asc

GET /api/tasks?status=IN_PROGRESS

GET /api/tasks?projectId=1
```

---

# Sample API Requests

## Create Project

```bash
curl -X POST http://localhost:8080/api/projects \
-H "Content-Type: application/json" \
-d '{
  "name":"Media Platform",
  "description":"Task tracking backend"
}'
```

---

## Create User

```bash
curl -X POST http://localhost:8080/api/users \
-H "Content-Type: application/json" \
-d '{
  "name":"Alice",
  "email":"alice@example.com",
  "role":"DEVELOPER"
}'
```

---

## Create Task

```bash
curl -X POST http://localhost:8080/api/tasks \
-H "Content-Type: application/json" \
-d '{
  "title":"Implement Dashboard",
  "status":"IN_PROGRESS",
  "priority":"HIGH",
  "projectId":1,
  "assigneeId":1,
  "dueDate":"2026-05-20"
}'
```

---

# Testing

Run all tests:

```bash
mvn test
```

Included tests:

* Service layer unit tests
* Mockito-based tests
* Integration tests using MockMvc
* Validation tests
* Exception handling tests

---

# OpenAPI Specification

The OpenAPI YAML file is available at:

```text
openapi.yaml
```

Swagger documentation is auto-generated from the application.

---

# Design Highlights

* Clean layered architecture
* DTO-based request/response handling
* Enum-based task status and priority
* Externalized configuration
* Structured error responses
* Optional pagination and sorting
* Production-style logging support

---

# Submission Checklist

Before submission:

```bash
mvn clean test
```

Verify:

* Application starts successfully
* Swagger UI works
* H2 console works
* All tests pass
* `openapi.yaml` exists at root
* `.idea/` and `target/` are excluded from Git

---

# Author

Deepak M
