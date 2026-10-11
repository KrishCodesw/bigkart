# Security Notes

## Current controls

The code includes:

- Spring Security with stateless session management.
- BCrypt password encoding.
- A JWT filter that reads `Authorization: Bearer <token>`.
- JWT signature and expiration checks.
- A role authority derived from the user's stored role.

The JWT secret is currently hard-coded in `JwtUtil.java`. The current configuration is suitable only for local learning and requires changes before deployment.

## High-priority issues

### 1. Public registration can assign ADMIN

`AuthService.register()` reads the caller's `role` field. If it equals `ADMIN`, the new account receives the ADMIN role. A public caller can therefore request privileged access.

**Required change:** assign USER to all public registrations. Add a separate, protected administrator provisioning flow.

### 2. Product authorization paths do not match controller routes

`SecurityConfig` applies role rules to `/products/**`, but `ProductController` maps:

- `POST /add`
- `GET /getAll`
- `GET /getById/{id}`
- `PUT /updateById`
- `DELETE /delete/{id}`

These route patterns do not match `/products/**`. They fall through to `.anyRequest().authenticated()`. This means the intended USER-versus-ADMIN policy is not enforced on the actual product paths. Authenticated users may reach product write routes.

**Required change:** either add a consistent `/products` route prefix or configure authorization against the actual paths. Verify access with integration tests for anonymous, USER, and ADMIN requests.

### 3. Hard-coded JWT signing secret

`JwtUtil.java` contains a static signing key. A source reader can obtain it and may create valid tokens.

**Required change:** load a strong random key from a secret manager or environment-based configuration. Do not commit the key. Plan key rotation if tokens are already in use.

### 4. Database credentials in source

`application.properties` includes local MySQL credentials.

**Required change:** use external configuration and separate credentials for development, testing, and production. Give the database user only the permissions it needs.

### 5. H2 console and SQL logging

The H2 console setting is enabled, while the active datasource is MySQL. The security configuration permits `/h2-console/**` and disables frame options for the console.

**Required change:** disable the console outside local development. Disable SQL output in production.

## Additional recommendations

- Add Jakarta Bean Validation to registration, login, and product DTOs.
- Return correct HTTP status codes and consistent error bodies.
- Avoid exposing exception details to clients.
- Add rate limiting or throttling to authentication routes.
- Use HTTPS in deployed environments.
- Add tests for expired, malformed, and tampered JWTs.
- Add tests for role-based access and privilege escalation.
- Use database migrations for controlled schema changes.
- Consider using `BigDecimal` for prices.
- Review CORS policy before connecting a browser frontend.

## Deployment decision

Do not treat the current implementation as production-ready until the high-priority issues are fixed and verified by automated tests.
