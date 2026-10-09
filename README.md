# Ecommerce Store

Full-stack ecommerce project with a Spring Boot REST API and a React/Vite frontend.

## Requirements

- Java 17 or newer
- MySQL 8
- Node.js 20.19+ or 22.12+

## Backend setup

1. Make sure MySQL is running. The default JDBC URL creates the `ecommerce_db` database if it does not already exist.
2. Set these environment variables in the terminal used to run the API. Use your own local credentials and never commit them.

PowerShell example (replace the example values locally):

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-local-mysql-password"
$env:JWT_SECRET = "use-a-unique-random-secret-at-least-32-characters-long"
$env:ADMIN_EMAIL = "admin@example.com"
$env:ADMIN_PASSWORD = "choose-a-strong-admin-password"
```

Run from `EcommerceAPINew`:

```powershell
.\mvnw.cmd spring-boot:run
```

The API runs at `http://localhost:8080`; Swagger UI is at `http://localhost:8080/swagger-ui/index.html`.

The admin initializer creates an admin only when that email is not already in the database. Changing `ADMIN_PASSWORD` does not update an existing account; reset an existing admin password separately.

## Frontend setup

From `EcommerceFrontend`, copy `.env.example` to `.env` (PowerShell: `Copy-Item .env.example .env`), then install dependencies and start Vite:

```powershell
npm install
npm run dev
```

The frontend runs at `http://localhost:5173`. Set `VITE_API_BASE_URL` in the local `.env` if the API is hosted somewhere other than `http://localhost:8080/api`.

## Security

Credentials and signing keys must be supplied through environment variables or a secret manager, not committed to Git. Any credentials previously committed to a public repository should be rotated; removing them in a later commit does not erase Git history.