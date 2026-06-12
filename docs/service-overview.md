# Service Overview

## Service Name

User

## Service Responsibility

User signup, profile lookup/update/delete, internal password authentication, and Google OAuth user authentication/creation.

## Technology Stack

- Language: Java 21
- Framework: Spring Boot 4.0.5
- Build Tool: Gradle
- Database: PostgreSQL via Spring Data JPA
- Other: Lombok is used where visible. JWT and Spring Security are used where security classes are visible.

## Main Package Structure

- Main package: `com.example.user`
- Controller: UserController handles /users APIs. InternalUserController handles /internal/users APIs.
- Service: UserService handles signup and profile operations. InternalUserService handles authentication and Google OAuth user lookup/creation.
- Repository: UserRepository is the persistence boundary.
- DTO: Request records are under dto.request; response records are under dto.response. ApiResponse<T> wraps public user API responses.
- Entity/domain: User is a JPA entity; Role has USER and ADMIN.

## Main Domains

User entity with Role enum. User supports local signup, Google user creation, profile update, and soft deletion through deletedAt.

## Main Features

User signup, profile lookup/update/delete, internal password authentication, and Google OAuth user authentication/creation.

## Main APIs

Visible APIs under /users and /internal/users. Full details are in API_SPEC.yaml.

## Data Access Structure

UserRepository extends JpaRepository<User, String> and queries users by email, provider identity, and soft-deletion state.

## Exception Handling

The service throws IllegalArgumentException and IllegalStateException directly. No @ControllerAdvice or global exception response mapper is visible.

## Test Structure

Only UserApplicationTests context load test is visible.

## API Documentation

This service uses `API_SPEC.yaml` as the main API specification.
When API behavior changes, `API_SPEC.yaml` must be updated in the same PR.

## Development Notes

- Preserve the current single-module service structure.
- Follow the existing package and naming conventions.
- Keep controller, service, repository, entity, and DTO responsibilities separate where those layers exist.
- Do not add cross-service behavior unless it is visible in code or explicitly specified by an Issue.
- If implementation changes API behavior, update `API_SPEC.yaml` in the same PR.

## Needs Confirmation

- HTTP error response format is not visible.
- Whether /internal/users endpoints require network-level protection is Needs confirmation.
- Password policy beyond @NotBlank on signup is not visible.
- Google OAuth token verification is not visible; request data is trusted by this service.
