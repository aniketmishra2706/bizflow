# BizFlow Backend

BizFlow is a Workflow and Task Management backend application built using Java and Spring Boot.

The application provides REST APIs for authentication, user management, department management, workflow management, and task management with JWT-based authentication and role-based authorization.

## Features

### Authentication

- User registration
- User login
- JWT authentication
- Password encryption using BCrypt
- Invalid login handling with 401 Unauthorized

### Role-Based Authorization

The application supports the following roles:

- ADMIN
- MANAGER
- USER

Access to APIs is controlled based on user roles.

### User Management

- Get all users
- Get user by ID
- Update user
- Update user status
- Update user roles
- Delete user
- Assign user to a department

### Department Management

- Create department
- Get all departments
- Get department by ID
- Update department
- Delete department
- Get users by department

### Workflow Management

- Create workflow
- Get all workflows
- Get workflow by ID
- Get workflows by department
- Get workflows by status
- Update workflow
- Update workflow status
- Delete workflow

### Task Management

- Create task
- Get all tasks
- Get task by ID
- Get tasks by workflow
- Get tasks assigned to a user
- Get my tasks
- Assign tasks to users
- Update task status
- Role-based task access control

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- JWT
- Maven
- Lombok
- Jakarta Validation
- Flyway
- Swagger / OpenAPI

## Project Architecture

```text
src/main/java/com/bizflow/bizflow_backend
│
├── controller
├── service
├── repository
├── entity
├── dto
│   ├── auth
│   ├── user
│   ├── department
│   ├── workflow
│   ├── task
│   └── common
├── security
├── exception
├── enums
└── config
```

## Database Relationships

```text
User
 ├── Many-to-Many → Roles
 └── Many-to-One → Department

Department
 └── One-to-Many → Users

Workflow
 └── Many-to-One → Department

Task
 ├── Many-to-One → Workflow
 ├── Many-to-One → Created By User
 └── Many-to-One → Assigned To User
```

## Authentication

The application uses JWT for authentication.

After successful login, the API returns an access token.

Example:

```json
{
  "accessToken": "your-jwt-token",
  "id": "user-id",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "roles": [
    "ADMIN"
  ]
}
```

For protected APIs, send the token in the request header:

```text
Authorization: Bearer YOUR_ACCESS_TOKEN
```

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/auth/register` | Register a user |
| POST | `/api/v1/auth/login` | Login |

### Users

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/users` | Get all users |
| GET | `/api/v1/users/{id}` | Get user by ID |
| PUT | `/api/v1/users/{id}` | Update user |
| PATCH | `/api/v1/users/{id}/status` | Update user status |
| PATCH | `/api/v1/users/{id}/roles` | Update user roles |
| PATCH | `/api/v1/users/{id}/department` | Assign department |
| DELETE | `/api/v1/users/{id}` | Delete user |

### Departments

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/departments` | Create department |
| GET | `/api/v1/departments` | Get all departments |
| GET | `/api/v1/departments/{id}` | Get department by ID |
| PUT | `/api/v1/departments/{id}` | Update department |
| DELETE | `/api/v1/departments/{id}` | Delete department |

### Workflows

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/workflows` | Create workflow |
| GET | `/api/v1/workflows` | Get all workflows |
| GET | `/api/v1/workflows/{id}` | Get workflow by ID |
| GET | `/api/v1/workflows/department/{departmentId}` | Get workflows by department |
| GET | `/api/v1/workflows/status/{status}` | Get workflows by status |
| PUT | `/api/v1/workflows/{id}` | Update workflow |
| PATCH | `/api/v1/workflows/{id}/status` | Update workflow status |
| DELETE | `/api/v1/workflows/{id}` | Delete workflow |

### Tasks

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/tasks` | Create task |
| GET | `/api/v1/tasks` | Get all tasks |
| GET | `/api/v1/tasks/{id}` | Get task by ID |
| GET | `/api/v1/tasks/workflow/{workflowId}` | Get tasks by workflow |
| GET | `/api/v1/tasks/assigned/{userId}` | Get tasks assigned to a user |
| GET | `/api/v1/tasks/my-tasks` | Get current user's tasks |
| PATCH | `/api/v1/tasks/{id}/status` | Update task status |
| PATCH | `/api/v1/tasks/{taskId}/assign/{userId}` | Assign task |

## Roles and Permissions

### ADMIN

- Full access to users
- Full access to departments
- Full access to workflows
- Can assign tasks
- Can manage user roles

### MANAGER

- Can manage departments
- Can manage workflows
- Can assign tasks
- Can manage tasks

### USER

- Can access authenticated task APIs
- Can view their assigned tasks
- Can update tasks assigned to them

## Running the Application

### Prerequisites

Make sure you have installed:

- Java 21
- PostgreSQL
- Maven

### Configure Database

Create a PostgreSQL database:

```sql
CREATE DATABASE bizflow;
```

Configure your database connection in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bizflow
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=validate

spring.jpa.show-sql=true
```

### Run the Application

Using IntelliJ:

1. Open the project.
2. Configure the PostgreSQL database.
3. Run the `BizflowBackendApplication` class.

Or using Maven:

```bash
./mvnw spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## API Documentation

Swagger UI can be accessed at:

```text
http://localhost:8080/swagger-ui/index.html
```

## Error Handling

The application provides centralized exception handling for common errors.

Examples:

- 400 Bad Request
- 401 Unauthorized
- 403 Forbidden
- 404 Not Found
- 500 Internal Server Error

Example error response:

```json
{
  "timestamp": "2026-08-21T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password",
  "path": "/api/v1/auth/login"
}
```

## Future Improvements

- Refresh tokens
- Pagination and sorting
- Search and filtering
- Email notifications
- Task comments
- File attachments
- Dashboard analytics
- Unit and integration tests
- Docker support
- CI/CD pipeline
- Audit logging

## Author

Aniket Mishra

## License

This project is currently created for learning and portfolio purposes.