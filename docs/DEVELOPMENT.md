# Development Guide

## Local environment

1. Install a JDK compatible with `pom.xml` (`java.version=26` at the time of review).
2. Install and start MySQL.
3. Clone the repository.
4. Configure the database connection in `src/main/resources/application.properties`.
5. Start the application with `./mvnw spring-boot:run` or `mvnw.cmd spring-boot:run` on Windows.

## Maven commands

Run the application:

```bash
./mvnw spring-boot:run
```

Run tests:

```bash
./mvnw test
```

Build the package:

```bash
./mvnw clean package
```

Run the packaged application:

```bash
java -jar target/bigkart-0.0.1-SNAPSHOT.jar
```

The last command works after a successful package build. The output file name follows the Maven artifact ID and version.

## Code map

| Package | Responsibility |
| --- | --- |
| `controllers` | HTTP route definitions |
| `services` | Authentication and product operations |
| `repository` | Spring Data JPA data access |
| `entities` | Database entity classes |
| `dto` | API request and response models |
| `security` | JWT, user lookup, filters, and authorization |
| Root package | Application startup and shared bean configuration |

## Suggested implementation order

1. Fix `ProductService.getAllProduct()` to map the repository result.
2. Align product controller routes with the security matchers.
3. Remove role selection from public registration and assign USER by default.
4. Move the JWT secret and database credentials to external configuration.
5. Add DTO validation and consistent error responses.
6. Add repository/service/controller tests, including authentication and authorization cases.
7. Add a database migration tool and migration scripts.
8. Add API schema documentation (for example, OpenAPI) after the routes and response contracts are stable.

## Testing expectations

Before merging a change, run `./mvnw test`. Add tests for:

- Registration success and duplicate usernames.
- Password hashing and login success/failure.
- JWT validation and expiration.
- Product create, list, fetch, update, and delete.
- Missing product IDs and invalid input.
- Anonymous, USER, and ADMIN access to every product route.
- Database behavior using a controlled test database.

The current test suite only contains a context-load test. A passing build does not yet prove that all API features work correctly.

## Contribution workflow

1. Create a branch for one change.
2. Keep the change focused.
3. Add tests for new behavior and defects.
4. Run the test suite.
5. Describe the change and test results in the pull request.
