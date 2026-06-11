# Architecture Guide

## Current Architecture

User is a single-module Gradle service.

- Technology: Java 21, Spring Boot 4.0.5
- Main package: `com.example.user`
- Main responsibility: User and internal authentication responsibilities.
- Security: JWT stateless security is configured. /users/signup is permitAll, /users/me is authenticated, and other requests are permitAll.

## Layer Structure

- Controller layer: UserController handles /users APIs. InternalUserController handles /internal/users APIs.
- Service layer: UserService handles signup and profile operations. InternalUserService handles authentication and Google OAuth user lookup/creation.
- Repository layer: UserRepository is the persistence boundary.
- DTO layer: Request records are under dto.request; response records are under dto.response. ApiResponse<T> wraps public user API responses.
- Entity/domain layer: User is a JPA entity; Role has USER and ADMIN.
- Exception handling: The service throws IllegalArgumentException and IllegalStateException directly. No @ControllerAdvice or global exception response mapper is visible.

## Layer Responsibilities

Controllers should focus on HTTP request and response handling.
Services should contain business logic.
Repositories should handle persistence.
Entities should not be returned directly as API responses.
Request DTOs and Response DTOs should be separated.
Existing architecture must not be changed without explicit instruction.
If API behavior changes, API_SPEC.yaml must be updated in the same PR.

## Transaction Boundary

Transaction boundaries are defined in service classes when `@Transactional` is present. If a service has no transaction annotations, transaction policy is Needs confirmation.

## API Documentation Responsibility

`API_SPEC.yaml` is the source of truth for the service API contract. Any API behavior change must update `API_SPEC.yaml` in the same PR.

## Forbidden Patterns

- Do not move business logic into controllers.
- Do not perform persistence directly from controllers.
- Do not expose JPA entities as API responses when response DTOs exist.
- Do not introduce a different architecture without explicit approval.
- Do not add cross-service assumptions that are not visible from code.
- Do not change build files, dependencies, or application configuration as part of ordinary feature work unless explicitly approved.
