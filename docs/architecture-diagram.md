# Architecture Diagram

## High-Level Architecture

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

## Component Diagram

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

## Design Patterns Used

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