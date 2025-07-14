# Account Service

This project is a Spring Boot REST API for managing accounts.

## Requirements

- Java 21+
- Docker
- Gradle

## Running the Application Locally

### Option 1: Use this when running the Spring Boot app directly with Gradle or IntelliJ.

Start the MySQL database using the local override file:
```bash
docker-compose -f docker-compose.local.yml up -d
```

This will start a MySQL container configured for local development.

Then build and run the Spring Boot application locally:
```bash
./gradlew build
./gradlew bootRun
```
Alternatively, just run it from IntelliJ.


### Option 2: Fully Dockerized (App + MySQL)

Use this to build and run everything inside Docker containers.

```bash
docker-compose up -d --build
```

Then access the app at:
http://localhost:8080

## Swagger UI

API documentation is available at:

http://localhost:8080/swagger-ui/index.html

## Running Tests

Integration tests use Testcontainers and do not require Docker Compose to be running.

Run with:

```bash
./gradlew integrationTest
```