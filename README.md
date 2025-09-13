# Card Vault Service

This project is a Spring Boot application that provides encryption, hashing, and masking services for PAN (Primary Account Number).

## Features

- AES-GCM encryption
- SHA-256 hashing
- PAN masking
- Key versioning (for future key rotation)
- RESTful API with Swagger UI documentation
- In-memory H2 database for development and testing

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on [http://localhost:8080](http://localhost:8080).

### API Documentation (Swagger UI)

Access the Swagger UI for interactive API documentation at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

or

[http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)

### H2 Database Console

Access the H2 in-memory database console at:

[http://localhost:8080/h2-console](http://localhost:8080/h2-console)

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (leave blank)

### Configuration

You can configure application properties in `src/main/resources/application.properties`.

### Project Structure

- `com.example.service.EncryptionService` - Core encryption, hashing, and masking logic
- `src/main/resources/application.properties` - Application configuration