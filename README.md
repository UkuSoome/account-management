# Account Service

This project is a Spring Boot REST API for managing accounts.

## Requirements

- Java 21+
- Docker
- Gradle

## Running the Application Locally

1. Start the MySQL database using Docker Compose:

```bash
docker-compose up -d
```

This will start a MySQL container configured for the project.

Build and run the Spring Boot Application

```bash
./gradlew build
./gradlew bootRun
```

## Swagger UI

API documentation and testing UI is available at:

http://localhost:8080/api/swagger-ui/index.html

## Running Tests

Integration tests use Testcontainers and do not require Docker Compose to be running.

Run with:

```bash
./gradlew integrationTest
```