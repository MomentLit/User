# PLAN.md

## Repository Role

User Service.

## Repository Type

backend

## Tech Stack

- Java 21
- Spring Boot 4.0.5
- Gradle
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Validation
- Spring Actuator
- JWT via `io.jsonwebtoken`
- PostgreSQL
- Lombok
- H2 for tests

## Main Directories

- `src/main/java/com/example/user/controller`: API controllers.
- `src/main/java/com/example/user/service`: application services.
- `src/main/java/com/example/user/repository`: persistence repositories.
- `src/main/java/com/example/user/entity`: domain entities.
- `src/main/java/com/example/user/dto/request`: request DTOs.
- `src/main/java/com/example/user/dto/response`: response DTOs.
- `src/main/java/com/example/user/global`: shared config, security, response, and utility code.
- `src/main/resources`: application configuration.
- `src/test/java/com/example/user`: tests.
- `src/test/resources`: test configuration.
- `docs`: service documentation.

## Domain Responsibilities

- User signup.
- Current user lookup.
- Current user update.
- Current user deletion.
- Internal user credential authentication.
- Internal Google OAuth user handling.

## API Source of Truth

Backend API specifications are managed in Apidog.

The local repository-side API spec is `API_SPEC.yaml`.

The SprintOps Agent compares Apidog API specs with this repository's implementation state.

## Completion Rule

A backend feature is considered completed when:

- the API exists in Apidog or OpenAPI spec
- matching backend implementation exists
- related PR is merged into the main branch

If there is an open PR but no merge, mark it as in progress.

If the API exists but no implementation evidence exists, mark it as missing.

If implementation exists but no API spec exists, mark it as spec mismatch.

## SprintOps Agent Checkpoints

The SprintOps Agent should inspect:

- `API_SPEC.yaml`
- `src/main/java/com/example/user/controller`
- `src/main/java/com/example/user/service`
- `src/main/java/com/example/user/dto`
- `src/main/java/com/example/user/repository`
- `src/main/java/com/example/user/entity`
- `src/main/java/com/example/user/global`
- `src/test/java/com/example/user`
- `docs`
- merged PRs
- open PRs
- issues
- branches
- this `PLAN.md`

## Notes

- Public user routes are under `/users`.
- Internal user routes are under `/internal/users`.
