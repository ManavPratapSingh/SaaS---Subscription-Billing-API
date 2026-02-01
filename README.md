# SaaS Subscription and Billing API

A scalable, secure backend REST API for managing subscription-based services, built with Spring Boot. This API provides comprehensive functionality for user management, subscription plans, payment processing, and administrative operations.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Running the Application](#-running-the-application)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [API Endpoints](#-api-endpoints)
- [Security](#-security)
- [Caching](#-caching)
- [Rate Limiting](#-rate-limiting)
- [Email Notifications](#-email-notifications)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

- **User Authentication & Authorization**
  - JWT-based authentication
  - Role-based access control (USER, ADMIN)
  - Secure password encryption with BCrypt

- **Subscription Management**
  - Multiple subscription plans
  - Subscription lifecycle management (ACTIVE, INACTIVE, EXPIRED, CANCELLED)
  - Automated subscription expiry scheduling

- **Payment Processing**
  - Mock payment processing system
  - Payment receipt upload support
  - Invoice generation and retrieval
  - Transaction tracking

- **Admin Dashboard**
  - System-wide statistics
  - User and subscription analytics

- **Performance Optimization**
  - Redis caching for plans, users, and subscriptions
  - Configurable TTL for different cache types

- **Security Features**
  - Rate limiting per user
  - CORS configuration
  - Input validation
  - SQL injection protection

- **API Documentation**
  - Interactive Swagger UI
  - OpenAPI 3.0 specification
  - Comprehensive endpoint documentation

## 🛠 Tech Stack

### Backend Framework
- **Spring Boot 4.0.2** - Core framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **Spring Cache** - Caching abstraction

### Database
- **PostgreSQL** - Primary database
- **H2** - In-memory database for testing
- **Redis** - Distributed caching

### Security
- **JWT (JSON Web Tokens)** - Authentication
- **BCrypt** - Password hashing

### Additional Libraries
- **Lombok** - Boilerplate code reduction
- **JJWT** - JWT creation and validation
- **Bucket4j** - Rate limiting
- **SpringDoc OpenAPI** - API documentation
- **Jakarta Validation** - Request validation

### Build Tools
- **Maven** - Dependency management and build automation

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 21** or higher
  ```bash
  java -version
  ```

- **Maven 3.6+**
  ```bash
  mvn -version
  ```

- **PostgreSQL 12+**
  ```bash
  psql --version
  ```

- **Redis 6+**
  ```bash
  redis-server --version
  ```

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/SaaS-subscription-billing-api.git
cd SaaS-subscription-billing-api
```

### 2. Set Up PostgreSQL Database

```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE subscription_db;

-- Grant privileges (if needed)
GRANT ALL PRIVILEGES ON DATABASE subscription_db TO postgres;

-- Exit psql
\q
```

### 3. Set Up Redis

#### Windows (using Redis for Windows)
```bash
# Start Redis server
redis-server

# Or run as Windows service
redis-server --service-start
```

#### Linux/Mac
```bash
# Start Redis
redis-server

# Or as a service
sudo systemctl start redis
```

Verify Redis is running:
```bash
redis-cli ping
# Should return: PONG
```

### 4. Install Dependencies

```bash
mvn clean install
```

## ⚙️ Configuration

### Environment Variables

Create environment variables or update `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/subscription_db
    username: postgres
    password: ${db_password}
  
  mail:
    username: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}

  data:
    redis:
      host: localhost
      port: 6379

jwt:
  secret: ${JWT_SECRET:mySecretKeyForJWTTokenGenerationAndValidationPurpose12345}
  expiration: 86400000  # 24 hours
```

### Required Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `db_password` | PostgreSQL password | `yourpassword` |
| `EMAIL_USERNAME` | SMTP email username | `saas.subscription.billing.api@gmail.com` |
| `EMAIL_PASSWORD` | SMTP email password/app password | `your-app-password` |
| `JWT_SECRET` | Secret key for JWT signing | `your-secret-key-here` |

### Setting Environment Variables

#### Windows (PowerShell)
```powershell
$env:db_password="yourpassword"
$env:EMAIL_USERNAME="saas.subscription.billing.api@gmail.com"
$env:EMAIL_PASSWORD="your-app-password"
$env:JWT_SECRET="your-secret-key"
```

#### Linux/Mac
```bash
export db_password="yourpassword"
export EMAIL_USERNAME="saas.subscription.billing.api@gmail.com"
export EMAIL_PASSWORD="your-app-password"
export JWT_SECRET="your-secret-key"
```

## 🏃 Running the Application

### Development Mode

```bash
mvn spring-boot:run
```

### Production Build

```bash
# Build JAR file
mvn clean package -DskipTests

# Run JAR
java -jar target/SaaS-subscription-billing-api-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

### Verify Application is Running

```bash
curl http://localhost:8080/v3/api-docs
```

## 📚 API Documentation

### Swagger UI

Once the application is running, access the interactive API documentation:

**URL:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### OpenAPI Specification

- **JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **YAML:** [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3/api-docs.yaml)

### Using Swagger UI

1. **Register a User:** Use `POST /api/auth/register`
2. **Login:** Use `POST /api/auth/login` to get JWT token
3. **Authorize:** Click "Authorize" button and enter: `Bearer <your-token>`
4. **Test Endpoints:** Now you can test protected endpoints

## 📁 Project Structure

```
SaaS-subscription-billing-api/
├── src/
│   ├── main/
│   │   ├── java/com/project/SaaS/subscription_billing_api/
│   │   │   ├── annotation/          # Custom annotations (RateLimit)
│   │   │   ├── config/              # Configuration classes
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   ├── RedisCacheConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── PaymentController.java
│   │   │   │   ├── PlanController.java
│   │   │   │   └── SubscriptionController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── entity/              # JPA Entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Plan.java
│   │   │   │   ├── Subscription.java
│   │   │   │   ├── Payment.java
│   │   │   │   └── Invoice.java
│   │   │   ├── exception/           # Custom exceptions and handlers
│   │   │   ├── interceptor/         # Request interceptors
│   │   │   ├── repository/          # JPA Repositories
│   │   │   ├── scheduler/           # Scheduled tasks
│   │   │   ├── security/            # Security configuration
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtUtil.java
│   │   │   │   └── JwtFilter.java
│   │   │   └── service/             # Business logic
│   │   └── resources/
│   │       ├── application.yaml     # Application configuration
│   │       └── static/
│   └── test/                        # Unit and integration tests
├── uploads/                         # Payment receipt uploads
├── pom.xml                          # Maven dependencies
└── README.md                        # This file
```

## 🔌 API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login and get JWT token | No |

### Plans

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/plans` | Get all subscription plans | Yes |

### Subscriptions

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/subscriptions` | Create new subscription | Yes |

### Payments

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/payments/process` | Process payment with receipt | Yes |
| GET | `/api/payments/my-invoices` | Get user's invoices | Yes |

### Admin

| Method | Endpoint | Description | Auth Required | Role |
|--------|----------|-------------|---------------|------|
| GET | `/api/admin/stats` | Get system statistics | Yes | ADMIN |

## 🔒 Security

### Authentication Flow

1. **Register:** `POST /api/auth/register`
   ```json
   {
     "username": "john_doe",
     "email": "john@example.com",
     "password": "SecurePass123"
   }
   ```

2. **Login:** `POST /api/auth/login`
   ```json
   {
     "username": "john_doe",
     "password": "SecurePass123"
   }
   ```

3. **Response:**
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "type": "Bearer",
     "username": "john_doe",
     "email": "john@example.com",
     "role": "USER"
   }
   ```

4. **Use Token:** Include in Authorization header:
   ```
   Authorization: Bearer <token>
   ```

### Password Security

- Passwords are hashed using **BCrypt** with salt
- Minimum password length: 6 characters
- Passwords are never stored in plain text

### JWT Token

- **Expiration:** 24 hours (configurable)
- **Algorithm:** HS256
- **Claims:** username, role, issue date, expiration

## ⚡ Caching

Redis is used for caching to improve performance:

| Cache Type | TTL | Purpose |
|------------|-----|---------|
| `plans` | 1 hour | Subscription plans rarely change |
| `users` | 30 minutes | User data for lookups |
| `subscriptions` | 5 minutes | Active subscription data |

### Cache Configuration

Located in `RedisCacheConfig.java` and `application.yaml`:

```yaml
cache:
  ttl:
    plans: 3600
    users: 1800
    subscriptions: 300
```

## 🚦 Rate Limiting

Rate limiting prevents API abuse:

- **Default:** 100 requests per minute per user
- **Enforced on:** Login, Register, Payment, Subscription endpoints
- **Implementation:** Bucket4j with token bucket algorithm

Configure in `application.yaml`:

```yaml
rate-limit:
  requests-per-minute: 100
```

## 📧 Email Notifications

Email notifications are sent for:
- Subscription creation
- Payment success/failure
- Subscription expiry warnings

### Email Configuration

Configure SMTP settings in `application.yaml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=UserServiceTest
```

### Skip Tests

```bash
mvn clean install -DskipTests
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards

- Follow Java naming conventions
- Use Lombok for boilerplate code
- Write unit tests for new features
- Update API documentation (Swagger annotations)
- Keep commits atomic and descriptive

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

**Project Maintainer:** Manav Pratap Singh

**Project Link:** [https://github.com/ManavPratapSingh/SaaS-subscription-billing-api](https://github.com/ManavPratapSingh/RideShare)

---

## 🎯 Roadmap

- [ ] Add payment gateway integration (Stripe/PayPal)
- [ ] Implement subscription upgrade/downgrade
- [ ] Add webhook support for payment notifications
- [ ] Implement subscription analytics dashboard
- [ ] Add multi-currency support
- [ ] Implement proration for mid-cycle changes
- [ ] Add GraphQL API support
- [ ] Implement automated backup system

---

<div align="center">

**Built with ❤️ using Spring Boot**

⭐ Star this repo if you find it helpful!

</div>
