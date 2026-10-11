# BigKart

BigKart is a REST API for a basic e-commerce product catalogue. It uses Java, Spring Boot, Spring Data JPA, MySQL, Spring Security, and JSON Web Tokens (JWT).

The current code provides user registration and login, JWT-based authentication, and product create, read, update, and delete operations. It is a backend project. A web or mobile storefront is not included in this repository.

> **Project status:** Development / learning project. Review the known limitations and security notes before you expose it to a public network.

## Contents

- [Features](#features)
- [Technology stack](#technology-stack)
- [Architecture](#architecture)
- [Requirements](#requirements)
- [Quick start](#quick-start)
- [Configuration](#configuration)
- [Run the application](#run-the-application)
- [API overview](#api-overview)
- [Example workflow](#example-workflow)
- [Database model](#database-model)
- [Tests](#tests)
- [Repository structure](#repository-structure)
- [Known limitations](#known-limitations)
- [Security notes](#security-notes)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

## Features

- Register users with a username, password, and role.
- Authenticate users and return a signed JWT.
- Store password hashes with BCrypt.
- Validate bearer tokens in a request filter.
- Store product records with a generated ID, name, price, and quantity.
- Use Spring Data JPA repositories for database access.
- Use DTOs to transfer request and response data.

## Technology stack

| Component | Technology |
| --- | --- |
| Language | Java (the Maven project sets `java.version` to `26`) |
| Application framework | Spring Boot `4.1.0` |
| HTTP API | Spring MVC |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL; H2 is also declared as a dependency |
| Authentication | Spring Security, JWT (JJWT `0.11.5`) |
| Password hashing | BCrypt |
| DTO mapping | ModelMapper |
| Build | Maven Wrapper |
| Boilerplate reduction | Lombok |

Use a JDK that matches the Java version configured in `pom.xml`. If your installed JDK does not support Java 26, change the project version only after checking compatibility with the selected Spring Boot release and your environment.

## Architecture

The application follows a simple layered design:

```text
HTTP client
    |
    v
Controllers
    |-- AuthController --> AuthService --> UserRepository --> MySQL
    |                         |
    |                         +--> PasswordEncoder / AuthenticationManager
    |                         +--> JwtUtil --> JWT response
    |
    +-- ProductController --> ProductService --> ProductRepository --> MySQL
                                      |
                                      +--> ModelMapper / ProductDTO

Incoming protected request
    |
    v
JwtFilter --> JwtUtil --> CustomUserDetailsService --> UserRepository
    |
    v
Spring Security authorization rules
    |
    v
Controller / service
```

### Main components

- **Controllers:** Define HTTP routes in `controllers/`.
- **Services:** Hold authentication and product operations in `services/`.
- **Repositories:** Use Spring Data JPA in `repository/`.
- **Entities:** Map users and products to database tables in `entities/`.
- **DTOs:** Define JSON input and output objects in `dto/`.
- **Security:** Configure authentication, JWT validation, and access rules in `security/`.
- **AppConfig:** Provides a `ModelMapper` bean.

## Requirements

Install the following:

1. Git.
2. A JDK compatible with the `java.version` value in `pom.xml`.
3. MySQL Server.
4. A terminal. The included Maven Wrapper can run Maven without a separate Maven installation.

## Quick start

### 1. Clone the repository

```bash
git clone https://github.com/KrishCodesw/bigkart.git
cd bigkart
```

### 2. Create the database

The default JDBC URL requests creation of a database named `bigkart` if it does not exist. MySQL must be running, and the configured MySQL user must have permission to create and access it.

You can also create it manually:

```sql
CREATE DATABASE bigkart;
```

### 3. Configure the database connection

Open `src/main/resources/application.properties`. The current file contains local development credentials. Replace them with credentials for your local MySQL instance. Do not commit real credentials.

Example local configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bigkart?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

These are example values. Do not use placeholder text as a real password.

### 4. Start the API

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows Command Prompt or PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The application uses Spring Boot's default HTTP port, **8080**, unless you change it.

### 5. Check the service

There is no dedicated health endpoint in the current code. Check the startup log for a successful Spring Boot start, then test an API route using the examples in [API overview](#api-overview).

## Configuration

The main configuration file is `src/main/resources/application.properties`.

| Property | Current purpose |
| --- | --- |
| `spring.application.name` | Application name: `bigkart` |
| `spring.datasource.url` | MySQL connection URL |
| `spring.datasource.username` / `password` | Database credentials |
| `spring.jpa.hibernate.ddl-auto` | Uses `update` to update the schema automatically |
| `spring.jpa.show-sql` | Prints SQL statements |
| `spring.h2.console.enabled` | Enables the H2 console setting |
| `spring.h2.console.path` | Sets the console path to `/h2-console` |

The active datasource configuration is MySQL. The H2 datasource settings are commented out. Enabling the H2 console does not switch the application from MySQL to H2.

For production, move secrets to environment-based configuration, disable SQL logging and the H2 console, and use a controlled database migration process instead of `ddl-auto=update`.

## API overview

The routes below are defined by the current controllers. They do not all use a common `/api` or `/products` prefix.

| Method | Path | Purpose |
| --- | --- | --- |
| POST | `/auth/register` | Register a user |
| POST | `/auth/login` | Authenticate and return a JWT |
| POST | `/add` | Create a product |
| GET | `/getAll` | List products (currently affected by a code defect; see Known limitations) |
| GET | `/getById/{id}` | Get one product |
| PUT | `/updateById` | Update a product using the ID in the JSON body |
| DELETE | `/delete/{id}` | Delete a product |

See [docs/API.md](docs/API.md) for request examples, response shapes, and current authorization behavior.

## Example workflow

### Register a user

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"demo-user","password":"change-this-password","role":"USER"}'
```

The current service returns a text message. If the username already exists, it returns `Username already exists`.

### Log in

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"demo-user","password":"change-this-password"}'
```

The successful response contains `token`, `username`, and `role`. Copy the token value and send it as a bearer token to protected routes.

### Create a product

```bash
curl -X POST http://localhost:8080/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT" \
  -d '{"name":"Keyboard","price":1499.0,"quantity":10}'
```

### Read a product

```bash
curl http://localhost:8080/getById/1 \
  -H "Authorization: Bearer YOUR_JWT"
```

The ID is generated by the database. Use the ID returned when a product is created.

**Note:** The current security configuration and product route paths do not align. Review [docs/SECURITY.md](docs/SECURITY.md) before relying on role restrictions.

## Database model

### User

The `UserEntity` class maps to the `users` table.

| Field | Description |
| --- | --- |
| `id` | Generated integer primary key |
| `username` | Required, unique username |
| `password` | BCrypt password hash |
| `role` | `USER` or `ADMIN`, stored as a string |

### Product

The `ProductEntity` class maps to a JPA-managed table.

| Field | Description |
| --- | --- |
| `id` | Generated integer primary key |
| `name` | Product name |
| `price` | Product price as a Java `double` |
| `quantity` | Available quantity as an integer |

For financial data, consider using `BigDecimal` instead of `double` to avoid floating-point rounding errors.

## Tests

Run the test suite:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

The repository currently contains a Spring application-context smoke test. It does not yet provide complete unit tests, API integration tests, authentication tests, or product CRUD tests. Test execution also depends on the application's database configuration.

## Repository structure

```text
bigkart/
├── .mvn/wrapper/                  # Maven Wrapper configuration
├── mvnw                           # Maven Wrapper (Unix)
├── mvnw.cmd                       # Maven Wrapper (Windows)
├── pom.xml                         # Maven dependencies and build settings
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/bigkart/
│   │   │   ├── AppConfig.java
│   │   │   ├── BigkartApplication.java
│   │   │   ├── controllers/
│   │   │   ├── dto/
│   │   │   ├── entities/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   └── services/
│   │   └── resources/application.properties
│   └── test/java/com/ecommerce/bigkart/
│       └── BigkartApplicationTests.java
└── docs/
    ├── API.md
    ├── DEVELOPMENT.md
    └── SECURITY.md
```

## Known limitations

The repository is an early backend implementation. Important current limitations include:

1. **Product listing defect:** `ProductService.getAllProduct()` reads records from the repository but maps the separate, empty in-memory `productList`. The `GET /getAll` route can therefore return an empty array even when the database contains products.
2. **Security route mismatch:** Security rules refer to `/products/**`, but the product controller maps routes such as `/add` and `/getAll`. The intended USER/ADMIN rules are not applied to these paths as written.
3. **Public role selection:** Registration accepts a role field and allows a caller to request `ADMIN`. This must be changed before deployment.
4. **Hard-coded JWT secret:** The signing key is in `JwtUtil.java`. Replace it with a secret supplied through a secure configuration source.
5. **Local database credentials:** The properties file contains a local username and password. Do not reuse these credentials outside a local environment.
6. **Missing validation and error handling:** Request DTOs have no validation annotations. Some missing product lookups return `null` instead of a clear HTTP 404 response.
7. **Limited automated tests:** Only a context-load test is present.
8. **No frontend or order flow:** The repository does not currently implement a storefront, cart, checkout, payment, or order management.
9. **Schema management:** Hibernate uses `ddl-auto=update`; production deployments should use versioned migrations.
10. **Java version:** `pom.xml` sets Java 26. Ensure your local and deployment build environments support this version.

## Security notes

- Do not deploy the current configuration to a public environment without changes.
- Do not expose the H2 console in production.
- Keep JWT signing keys and database credentials out of source control.
- Prevent public registration from assigning privileged roles.
- Apply authorization rules to the actual controller paths and test them with USER and ADMIN tokens.
- Add request validation, consistent error responses, HTTPS, rate limiting, and security tests.

See [docs/SECURITY.md](docs/SECURITY.md).

## Development

Read [docs/DEVELOPMENT.md](docs/DEVELOPMENT.md) for the local workflow and suggested implementation order.

## Contributing

1. Fork the repository.
2. Create a focused feature branch.
3. Make one logical change at a time.
4. Add or update tests.
5. Run `./mvnw test`.
6. Open a pull request with the change summary and test results.

## License

No license file is present in the repository at the time of writing. Until a license is added, do not assume that the source is available for unrestricted reuse.
