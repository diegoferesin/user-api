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

## Getting Started

### Local Development

1. Clone the repository
2. Build the project
```bash
mvn clean package
```
3. Run the application
```bash
java -jar target/user-api-0.0.1-SNAPSHOT.jar
```

### Using Docker

1. Build the Docker image
```bash
docker build -t user-api .
```
2. Run the container
```bash
docker run -p 8080:8080 user-api
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
  "id": "4a459a56-2580-4fe9-a641-bc83d4b2f8a5",
  "name": "Juan Rodriguez",
  "email": "juan@rodriguez.org",
  "created": "2023-09-23T13:45:32.145",
  "modified": "2023-09-23T13:45:32.145",
  "lastLogin": "2023-09-23T13:45:32.145",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "isactive": true
}
```

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