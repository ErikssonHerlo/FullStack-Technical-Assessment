# 🏷️ Task Management Web Application


# 📝 Task Management Backend – Java Spring Boot

## 📚 Overview

This project is the **backend implementation of the Task Management Web Application**, developed as part of the **Senior Full Stack Developer technical assessment**.  
The system provides a **RESTful API** for managing tasks and users, with full CRUD operations, validation, authentication with JWT, and database migrations using Flyway.

The backend follows a **monolithic architecture with a layered structure**, inspired by **Domain-Driven Design (DDD)** and applying **SOLID principles**. Additionally, it uses **vertical slicing by feature**, grouping related files (controller, service, repository, DTOs, models, etc.) into their corresponding domain package (`task`, `user`, `auth`).

---

## 🏗️ Project Structure and Responsibilities

```
src/main/java/com/erikssonherlo/taskmanagement/
├── auth/                         # Authentication logic (login, register)
│   ├── controller/               # AuthController (REST endpoints)
│   ├── dto/                      # LoginDTO, RegisterDTO
│   └── service/                  # AuthService and AuthServiceImpl
├── common/                       # Shared configurations and utilities
│   ├── config/                   # AppConfig, Security, Swagger, DataInitializer (seed admin)
│   ├── exception/                # GlobalExceptionHandler, custom exceptions
│   ├── payload/                  # ApiResponse, AuthResponse, PaginatedResponse
│   ├── security/                 # JWTService, JWTAuthenticationFilter
│   └── util/                     # Shared utilities (if needed)
├── task/                         # Task feature (Vertical Slicing)
│   ├── controller/               # TaskController (REST endpoints)
│   ├── dto/                      # CreateTaskForSelfDTO, CreateTaskForOtherDTO, UpdateTaskDTO, TaskDTO
│   ├── entity/                   # TaskEntity (JPA entity)
│   ├── mapper/                   # TaskMapper (MapStruct)
│   ├── model/                    # TaskPriority, TaskStatus (Domain enums)
│   ├── repository/               # TaskRepository (JpaRepository)
│   └── service/                  # TaskService, TaskServiceImpl
└── user/                         # User feature (Vertical Slicing)
    ├── controller/               # UserController (User endpoints)
    ├── dto/                      # UserDTO
    ├── entity/                   # UserEntity (JPA entity)
    ├── mapper/                   # UserMapper, UserMapperImpl
    ├── model/                    # Role, User (Domain model)
    ├── repository/               # UserRepository
    └── service/                  # UserService, UserServiceImpl
```

### 🟢 Why this structure?
- **Layered separation** promotes testability, maintainability, and scalability.
- **Vertical slicing** ensures each feature is isolated and independent.
- **Clear responsibilities** per folder (controllers expose endpoints, services handle logic, repositories access DB, mappers convert data).
- Allows easy growth of features without mixing concerns.

---

## ⚙️ Setup Instructions

### ✅ Requirements
- Java 17+
- Maven 3.8+
- PostgreSQL

### 🛠️ Installation and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/ErikssonHerlo/Leverest-FullStack-Dev-Test.git
   cd backend/
   ```

2. Configure your database:
   Copy the example config:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
   Then modify your `application.properties` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/task_db
   spring.datasource.username=your_user
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=validate
   spring.flyway.enabled=true
   ```

3. Run migrations automatically on startup.

4. Start the application:
   ```bash
   ./mvnw clean spring-boot:run
   ```

5. Swagger UI available at:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```
---

## 🚀 Technologies and Libraries Used

The following technologies and libraries were used to build the backend of the Task Management application:

### 🟢 **Core Framework and Language**
- **Java 17**: Main programming language.
- **Spring Boot 3.4.4**: Framework for rapid development of RESTful services.

### 🟠 **Database and Persistence**
- **PostgreSQL**: Relational database used for data storage.
- **Hibernate / JPA**: ORM (Object-Relational Mapping) for database interactions.
- **Flyway**: Manages database versioning and schema migrations via SQL scripts.

### 🔒 **Security and Authentication**
- **Spring Security**: Secures the application, handles authentication and authorization.
- **JWT (Java Web Token) - jjwt (API, Impl, Jackson)**: Used for generating and validating authentication tokens.

### 🟣 **Validation and API Documentation**
- **Spring Boot Starter Validation (Hibernate Validator)**: Handles input validation.
- **OpenAPI / Swagger (springdoc-openapi-starter-webmvc-ui)**: API documentation and interactive testing via Swagger UI.

### 🟡 **Dependency Management and Build Tools**
- **Maven**: Dependency management and build automation.
- **Maven Compiler Plugin**: Handles Java compilation and annotation processing.
- **Spring Boot Maven Plugin**: Builds and packages the Spring Boot application.

### 🟤 **Testing and Coverage**
- **JUnit 5**: Unit testing framework.
- **Mockito**: Mocking framework for isolated tests.
- **JaCoCo**: Code coverage reports, integrated with Maven.

### 🟠 **Utilities**
- **Lombok**: Reduces boilerplate code via annotations like `@Builder`, `@Data`, `@RequiredArgsConstructor`, etc.
- **Spring Boot DevTools**: Provides hot reload and development-time conveniences.

---

## 🛡️ Security & Authentication

- **JWT-based Authentication**:  
  Login and register endpoints generate JWT tokens signed with HMAC.  
  Tokens are validated by the `JWTAuthenticationFilter` on each request.

- **Roles Supported**:  
  - `ADMIN`: Full permissions, including user management.  
  - `MANAGER`: Can manage tasks for other users (except private self-managed tasks).  
  - `MEMBER`: Can only create, update, and delete their own tasks (self-managed only).

- **Token Handling**:  
  Tokens must be sent in the `Authorization` header as:  
  ```
  Authorization: Bearer <token>
  ```

---

## ❌ Exception Handling

Global exception handling is implemented via:
```java
@RestControllerAdvice
public class GlobalExceptionHandler { ... }
```

### Custom exceptions:
- `BadRequestException`
- `ResourceNotFoundException`
- `ResourceAlreadyExistsException`
- `CommunicationException`

Consistent API responses through `ApiResponse<T>`:
```json
{
  "code": 400,
  "message": "Email is required to create a user.",
  "status": "BAD_REQUEST",
  "data": null
}
```

---

## 🚀 Testing & Coverage

### ✅ Run tests:
```bash
./mvnw test
```

### 🟠 Generate coverage report (JaCoCo):
```bash
./mvnw jacoco:report
```
Report located at:
```
target/site/jacoco/index.html
```

**Unit tests were written for:**
- `TaskServiceImpl`
- `UserServiceImpl`
- `AuthServiceImpl`

Coverage is enforced through JaCoCo rules for complexity and line coverage.

---

## 🌐 API Documentation

The API is fully documented using **Springdoc OpenAPI** (Swagger).  
You can explore available endpoints and try them directly via the Swagger UI at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🟦 Endpoints Overview

| Method | Endpoint                   | Description                                      | Auth Required | Role Restrictions              |
|--------|----------------------------|--------------------------------------------------|---------------|---------------------------------|
| POST   | `/api/v1/auth/register`     | Register a new user                              | No            | Public                         |
| POST   | `/api/v1/auth/login`        | Authenticate user and return JWT                 | No            | Public                         |
| GET    | `/api/v1/users`             | List all users                                   | Yes           | ADMIN                          |
| GET    | `/api/v1/users/role`        | List users by role                               | Yes           | ADMIN, MANAGER                 |
| PATCH  | `/api/v1/users/{email}`     | Update user data                                 | Yes           | ADMIN, MANAGER                 |
| DELETE | `/api/v1/users/{email}`     | Soft delete a user                               | Yes           | ADMIN                          |
| POST   | `/api/v1/tasks/self`        | Create task for self                             | Yes           | MEMBER, MANAGER, ADMIN         |
| POST   | `/api/v1/tasks/other`       | Create task for other user                       | Yes           | MANAGER, ADMIN                 |
| GET    | `/api/v1/tasks`             | List tasks (optional filters: status, priority)  | Yes           | Role-based filtering applies   |
| PATCH  | `/api/v1/tasks/{taskId}`    | Update a task                                    | Yes           | Role-based restrictions apply  |
| DELETE | `/api/v1/tasks/{taskId}`    | Delete a task                                    | Yes           | Role-based restrictions apply  |

---

## 📌 Initial Admin User (Seed Data)

Upon application startup, the system initializes one `ADMIN` user with the following credentials (defined in `DataInitializer.java`):

| Email              | Password  |
|--------------------|-----------|
| `admin@taskmanager.com`  | `admin`   |

Password is securely hashed with BCrypt.

---

Perfecto, aquí tienes la sección **💡 Future Improvements** y la respuesta mejorada a la pregunta **5. Scalability & Maintainability**, ambas mucho más completas, explicativas y conectadas con la arquitectura y roles definidos.

---

## 💡 Future Improvements

The current architecture and role-based access control system lay the groundwork for a broad range of future enhancements that can scale both functionally and structurally. Some key improvements and extensions include:

- **User account creation with email verification**  
  New users could register and receive a one-time security code via email to activate their account. This can be securely managed using a temporary code stored in **Redis**, allowing expiration and ensuring the validation flow is robust.

- **Secure password recovery**  
  Users who forget their password could trigger a reset flow where a temporary code or token is sent to their email. This would also benefit from **Redis-backed expiration** and allow seamless validation on the backend.

- **Team and project management support**  
  Thanks to the well-defined **Role system (ADMIN, MANAGER, MEMBER)** and feature-oriented structure, the backend can be easily extended to support **Teams**, **Projects**, and **multi-user task management**.  
  - Example: Managers could manage team-level boards, and users could be assigned to teams with permissions scoped by team/project.

- **Dynamic Kanban boards**  
  Currently, task status is managed through a fixed `enum` (`TO_DO`, `IN_PROGRESS`, `DONE`). However, the system can evolve to support **customizable statuses** per project or team.  
  This would enable each board to have its own columns like "Design", "Development", "QA", "Deployed", etc., providing flexibility for diverse workflows.

- **Real-time collaboration with WebSockets**  
  Integrating **WebSocket** or **SSE** technologies would allow real-time task updates, status changes, and notifications across users working on the same board or team.

- **Full-featured admin dashboard**  
  A centralized panel for managing users, roles, teams, and system metrics. While currently out of scope, the backend design anticipates such growth by separating responsibilities and enforcing role validation.

- **Advanced filtering, pagination, and search**  
  Current endpoints support basic pagination. This could be enhanced with:
  - Full-text search
  - Filter by due date, status, assigned user
  - Sort by priority or creation date
  - Combined filtering for analytics and reporting.

- **Kanban task reordering**  
  The task service is capable of supporting multiple statuses and custom flows (up to 5+ stages). A drag-and-drop interface can be introduced to allow task reordering, with positional tracking in the database.

---

# ❓ Q&A – Technical Decisions

### 1. **Architecture Decisions**  
The backend follows a **layered architecture** combined with **vertical slicing per feature**, inspired by **DDD**.  
- Controllers, services, and repositories are grouped per domain (`auth`, `task`, `user`).  
- Business logic is centralized in the service layer.  
- Entities are decoupled from domain models via mappers (MapStruct).

This approach allows scalability, clean separation of concerns, and maintainability.

---

### 2. **API Design**  
RESTful design with **versioning (`/api/v1`)** and consistent error handling via a global exception handler.  
Standardized JSON responses (`ApiResponse`).  
Swagger/OpenAPI is used for documentation and testing.

---

### 3. **State Management**  
No frontend state management implemented (Redux, Pinia, etc.).  
Given the project's scope and time constraints, state is handled via API calls and token storage on the frontend (likely in-memory or localStorage, depending on the frontend decisions).

---

### 4. **Security**  
- **JWT Authentication** with token validation in a security filter.  
- **Role-based access control** with logic in service methods (checked against roles).  
- Tokens are signed and expire (configurable via JWTService).  
- Tokens are expected via the `Authorization` header.

---

### 5. Scalability & Maintainability (Expanded)

The architecture was intentionally designed to be **modular, role-aware, and easily extendable**. It uses vertical slicing per feature (task, user, auth), making it straightforward to introduce new domains like teams, boards, or analytics.

- **Role-based access logic** enables the backend to support multi-user features like:
  - Teams (users grouped by context)
  - Dynamic task sharing and delegation
  - Per-user and per-role board access

- **Domain-driven organization** ensures that logic and data transformation remain isolated per concern, which eases future feature additions without code tangling.

- If the system needs to scale:
  - Redis can be introduced for **caching**, **token/code expiration**, and **rate-limiting**.
  - The backend can evolve into a **microservices architecture**, splitting auth, task, and user services.
  - Task status can be moved from an enum to a **custom configuration table**, allowing teams to define their own workflow schemas.
  - Message brokers (e.g., RabbitMQ, Kafka) could be used for **notifications**, background processing, and auditing.
  - WebSocket or Server-Sent Events can provide **real-time updates** to connected clients.

This foundation, combined with careful adherence to clean principles and domain separation, ensures the backend can evolve to support thousands of users, real-time interaction, and enterprise-grade functionality with minimal rework.

---

### 6. **Time Constraints**  
Due to time limitations, the following were intentionally skipped:
- Complete **admin panel for user management**.
- **Frontend Kanban reordering**.
- **Dynamic statuses and team creation** (logic supports this, but UI and related endpoints not implemented).
- **Email verification and password reset flow**.

These features were planned and partially considered in the architecture for future extension.

---

## 👨‍💻 Author

**Eriksson Hernández**  
📧 erikssonhernandez25@gmail.com

🌐 [LinkedIn](https://www.linkedin.com/in/erikssonherlo/)