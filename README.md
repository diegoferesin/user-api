# User Registration API

A RESTful API for user registration using Spring Boot, JPA, and JWT authentication.

## Requirements

- Java 11+
- Maven 3.6+
- Docker (for containerization)

## Technology Stack

- Spring Boot 2.7.16
- Spring Security
- Spring Data JPA
- H2 Database (in-memory)
- JWT for Authentication
- Swagger/OpenAPI for API Documentation
- JUnit 5 & Mockito for Testing
- Lombok
- Docker

## Architecture

### High-Level Architecture

```
┌──────────────────────────┐
│                          │
│  Client (JSON Requests)  │
│                          │
└──────────────┬───────────┘
               │
               ▼
┌──────────────────────────┐    ┌──────────────────────┐
│                          │    │                      │
│     Spring Boot API      │◄───┤   Swagger / OpenAPI  │
│                          │    │                      │
└──────────────┬───────────┘    └──────────────────────┘
               │
               ▼
┌──────────────────────────┐
│                          │
│     Security Layer       │
│     (JWT, Validation)    │
│                          │
└──────────────┬───────────┘
               │
               ▼
┌──────────────────────────┐
│                          │
│    Controller Layer      │
│   (Request Handling)     │
│                          │
└──────────────┬───────────┘
               │
               ▼
┌──────────────────────────┐
│                          │
│     Service Layer        │
│  (Business Logic, JWT)   │
│                          │
└──────────────┬───────────┘
               │
               ▼
┌──────────────────────────┐    ┌──────────────────────┐
│                          │    │                      │
│   Repository Layer       │◄───┤     H2 Database      │
│  (Data Access Objects)   │    │     (In-memory)      │
│                          │    │                      │
└──────────────────────────┘    └──────────────────────┘
```

### Component Diagram

```
                            ┌─────────────────────┐
                            │                     │
                            │   UserController    │
                            │     (Interface)     │
                            │                     │
                            └──────────┬──────────┘
                                       │
                                       │ implements
                                       ▼
┌────────────────┐          ┌─────────────────────┐         ┌─────────────────┐
│                │  uses    │                     │  uses   │                 │
│ ErrorResponse  │◄─────────┤ UserControllerImpl  ├────────►│ UserService     │
│                │          │                     │         │    (Interface)   │
└────────────────┘          └─────────────────────┘         └────────┬────────┘
                                                                     │
                                                                     │ implements
                                                                     ▼
┌────────────────┐          ┌─────────────────────┐         ┌─────────────────┐
│                │  uses    │                     │  uses   │                 │
│ UserRepository │◄─────────┤  UserServiceImpl    ├────────►│ JwtService      │
│                │          │                     │         │  (Interface)     │
└────────────────┘          └──────────┬──────────┘         └────────┬────────┘
        │                              │                             │
        │                              │                             │ implements
        │                              │                             ▼
        │                              │                    ┌─────────────────┐
        │                              │                    │                 │
        │                              │                    │ JwtServiceImpl  │
        │                              │                    │                 │
        │                              │                    └─────────────────┘
        │                              │
        │                              │ uses
        │                              ▼
        │                   ┌─────────────────────┐
        │                   │                     │
        │                   │  User Entity        │
        │                   │                     │
        └──────────────────►└─────────────────────┘
                                       │
                                       │ contains
                                       ▼
                            ┌─────────────────────┐
                            │                     │
                            │   Phone (Embeddable)│
                            │                     │
                            └─────────────────────┘
```

### Design Patterns Used

1. **Dependency Injection**: Spring's core pattern for loose coupling and testability
2. **Repository Pattern**: For data access abstraction
3. **Interface Segregation**: Separating interfaces from implementations
4. **Builder Pattern**: Used with Lombok for object construction
5. **DTO Pattern**: For data transfer between layers
6. **Factory Pattern**: For creating JWT tokens
7. **Singleton Pattern**: Spring beans are singletons by default
8. **Strategy Pattern**: Different validation strategies
9. **Facade Pattern**: Service layer as a facade to business logic
10. **Controller-Service-Repository Pattern**: For separation of concerns

## Getting Started

### Local Development

1. Clone the repository
2. Build the project
```bash
mvn clean package
```
3. Run the application
```bash
java -jar target/users-api-0.0.1-SNAPSHOT.jar
```

### Using Docker

1. Build the Docker image
```bash
docker build -t users-api .
```
2. Run the container
```bash
docker run -p 8080:8080 users-api
```

## API Documentation

The API documentation is available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## API Endpoints

### Register User

**Endpoint:** `POST /users/register`

**Request Body:**
```json
{
  "name": "Juan Rodriguez",
  "email": "juan@rodriguez.org",
  "password": "Password1@",
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "contrycode": "57"
    }
  ]
}
```

**Response:**
```json
{
  "id": "4102e577-30d6-4c97-b393-446efa378cbe",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "created": "2025-05-03T03:03:14.643834",
  "modified": "2025-05-03T03:03:14.643846",
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTc0NjI0MTM5NCwiZXhwIjoxNzQ2MzI3Nzk0fQ.Igz1i1y0uGngmpQukjYVEKgxH045cL7FjvR4hD5W8lppOts0xJ4BFw6Cv-m3wGxdh9bm3i8GcYt3tb-I8ZMqOg",
  "last_login": "2025-05-03T03:03:14.588429",
  "is_active": true
}
```

## Postman Collection

A Postman collection is available for testing the API endpoints. You can find it [here](postman-collection/Users%20API.postman_collection.json)

This collection includes all the necessary endpoints and example requests to test the API functionality.

## Database

The application uses an H2 in-memory database, which you can access at:
http://localhost:8080/h2-console

Connection details:
- JDBC URL: `jdbc:h2:mem:userdb`
- Username: `sa`
- Password: `password`

## Testing

Run the tests using Maven:
```bash
mvn test
```

## Configuration

The application configuration is in `application.properties`:

- Email validation regex: `^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$`
- Password validation regex (configurable): `^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$`
  - This requires at least 8 characters with at least one digit, lowercase, uppercase, and special character

## Secure Password Storage

The application implements secure password storage using industry best practices:

- **BCrypt Hashing**: Passwords are hashed using BCrypt, a secure password hashing algorithm
- **Salt Generation**: Each password is automatically salted with a unique random value
- **One-way Encryption**: Passwords are never stored in plain text and cannot be reversed
- **Security Features**:
  - Automatic salt generation for each password
  - Work factor (cost) of 10 for the BCrypt algorithm
  - Protection against rainbow table attacks
  - Secure comparison of hashed passwords

The password hashing process ensures that even if the database is compromised, the original passwords cannot be recovered.