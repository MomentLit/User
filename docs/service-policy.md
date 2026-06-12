# Service Policy

## Confirmed Policies

- Signup requires email, password, and name to be non-blank; email must use email validation.
- Signup rejects duplicate email with IllegalArgumentException.
- Local signup stores an encoded password.
- Profile update rejects a blank name when name is provided.
- Delete is implemented as soft deletion by setting deletedAt.
- Deleted users cannot be updated or authenticated by the visible service methods.
- Google OAuth authentication requires emailVerified to be true and creates a GOOGLE provider user when provider identity is not found.

## Validation Rules

Visible validation rules are documented from DTO annotations, entity methods, and service methods only. Any validation behavior not present in code is Needs confirmation.

## Authorization Rules

JWT stateless security is configured. /users/signup is permitAll, /users/me is authenticated, and other requests are permitAll.

## Creation Policy

Creation behavior is documented only where visible in service or entity factory methods.

## Update Policy

Update behavior is documented only where visible in service or entity update methods.

## Deletion Policy

Deletion behavior is documented only where visible in service or entity delete methods.

## State Transition Rules

State transitions are documented only where visible in entity or service methods. Missing transitions are Needs confirmation.

## Exception Cases

The service throws IllegalArgumentException and IllegalStateException directly. No @ControllerAdvice or global exception response mapper is visible. HTTP status mapping for these exceptions is Needs confirmation unless explicitly handled in code.

## API Behavior Policy

- API behavior must be documented in `API_SPEC.yaml`.
- If API behavior changes, `API_SPEC.yaml` must be updated in the same PR.

## Needs Confirmation

- HTTP error response format is not visible.
- Whether /internal/users endpoints require network-level protection is Needs confirmation.
- Password policy beyond @NotBlank on signup is not visible.
- Google OAuth token verification is not visible; request data is trusted by this service.
