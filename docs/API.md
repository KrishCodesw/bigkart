# BigKart API Reference

Base URL for local development: `http://localhost:8080`

The application currently defines the routes below. No OpenAPI/Swagger configuration is present in the repository.

## Authentication

### POST `/auth/register`

Creates a user.

Request:

```json
{
  "username": "demo-user",
  "password": "change-this-password",
  "role": "USER"
}
```

Fields:

| Field | Type | Notes |
| --- | --- | --- |
| `username` | string | Required by the entity; request validation is not implemented |
| `password` | string | Encoded with BCrypt before storage |
| `role` | string | The current service selects ADMIN when the value is ADMIN; otherwise it selects USER |

Current response: plain text, such as `User registered successfully as USER`. A duplicate username returns `Username already exists`. These conditions are not represented with explicit HTTP status codes in the service.

**Security:** Public callers can request the ADMIN role. Do not expose this endpoint until role assignment is restricted on the server.

### POST `/auth/login`

Authenticates a user.

Request:

```json
{
  "username": "demo-user",
  "password": "change-this-password"
}
```

Successful response:

```json
{
  "token": "<jwt-token>",
  "username": "demo-user",
  "role": "USER"
}
```

The JWT contains the username as its subject, the role as a claim, an issue time, and an expiry time of one hour. Send it in the `Authorization` header:

```text
Authorization: Bearer <jwt-token>
```

## Products

The routes below are defined by `ProductController`. The current `SecurityConfig` uses a different path prefix for product authorization. See [SECURITY.md](SECURITY.md).

### POST `/add`

Creates a product.

Request:

```json
{
  "name": "Keyboard",
  "price": 1499.0,
  "quantity": 10
}
```

The response is a `ProductDTO`, normally including the generated `id`.

### GET `/getAll`

Returns a list of product DTOs. **Current defect:** the service maps an empty in-memory list instead of the list returned by the repository. The response can be an empty array even when products exist in MySQL.

### GET `/getById/{id}`

Returns the product with the supplied ID. The service returns `null` when no record exists; the controller does not convert this case to an explicit 404 response.

Example: `GET /getById/1`

### PUT `/updateById`

Updates a product. The ID is read from the JSON request body.

Request:

```json
{
  "id": 1,
  "name": "Mechanical Keyboard",
  "price": 1999.0,
  "quantity": 8
}
```

The service updates the name, price, and quantity. If the ID does not exist, it returns `null`.

### DELETE `/delete/{id}`

Deletes the product with the supplied ID.

Example: `DELETE /delete/1`

Current response is plain text: `Entity deleted` or `Entity not found`.

## Common request example

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"demo-user","password":"change-this-password"}'
```

Use the returned token for protected requests. Do not place real passwords or tokens in source control, screenshots, or issue reports.
