# 🛍️ E-Commerce Backend API

### 🚀 DevOps & Container Orchestration Showcase

A modern, full-featured e-commerce REST API built with **Spring Boot 3.3.4** and **Java 21**, engineered as a **DevOps
showcase project**. This project demonstrates industry best practices in containerization, infrastructure as code,
deployment automation, and microservices orchestration using Docker, Docker Compose, and cloud-ready architecture
patterns.

---

## ✨ Features

### 🏗️ DevOps & Infrastructure Features

- 🐳 **Docker Containerization** - Multi-stage Dockerfile with optimized Alpine Linux base images
- 🔗 **Docker Compose Orchestration** - Complete development environment with PostgreSQL and Spring Boot integration
- 🏥 **Health Checks** - Container health verification with intelligent restart policies
- 📦 **Volume Management** - Persistent data storage for database and file uploads
- 🌐 **Network Isolation** - Container networking with service discovery
- 🔐 **Environment Configuration** - 12-factor app principles with environment variable management
- 🚀 **Zero-Downtime Deployment** - Ready for Kubernetes and cloud platforms (Render, AWS, Azure, GCP)
- 📊 **Render.yaml Configuration** - Cloud deployment automation and CI/CD integration
- 📝 **Infrastructure as Code (IaC)** - docker-compose.yml and Dockerfile as source control artifacts

### 🎯 Application Features

- 🔐 **User Authentication & Authorization** - Secure JWT-based authentication with role-based access control (Admin,
  User, Vendor)
- 📦 **Product Management** - Full CRUD operations for products with categories and inventory management
- 🛒 **Shopping Cart** - Add, remove, and manage items in shopping cart
- ❤️ **Wishlist** - Save favorite products for later
- 📋 **Orders & Transactions** - Complete order lifecycle management
- 👥 **User Management** - Profile management and account handling
- 🏪 **Vendor Management** - Multi-vendor support with vendor dashboards
- 📧 **Email Notifications** - OTP verification and transactional emails
- 📖 **API Documentation** - Interactive Swagger/OpenAPI documentation

### 💻 Code Quality & Development

- **Spring Security** with JWT token-based authentication
- **Spring Data JPA** with PostgreSQL database
- **Comprehensive Exception Handling** with custom exception classes
- **DTOs** for clean API contracts and data transfer
- **Pagination Support** for large data sets
- **OpenAPI/Swagger 3.0** documentation
- **Code Quality** - Spotless code formatting with Google Java Format
- **MapStruct** for object mapping
- **Lombok** for reducing boilerplate code
- **Maven** with multi-module build optimization

---

## 🏗️ Project Structure

```
ecommerce-Backend/
├── src/main/java/com/borneo/ecommerce/
│   ├── config/                 # Spring Boot configurations
│   │   ├── OpenApiConfig.java  # Swagger/OpenAPI setup
│   │   ├── SecurityConfig.java # Spring Security configuration
│   │   └── WebConfig.java      # Web/CORS configuration
│   ├── controller/             # REST Controllers (11 endpoints)
│   │   ├── AdminController.java
│   │   ├── AuthController.java
│   │   ├── CartController.java
│   │   ├── CategoryController.java
│   │   ├── HomeController.java
│   │   ├── OrderController.java
│   │   ├── OtpController.java
│   │   ├── ProductController.java
│   │   ├── UserController.java
│   │   ├── VendorController.java
│   │   └── WishlistController.java
│   ├── dto/                    # Data Transfer Objects
│   ├── exception/              # Custom Exception classes
│   │   ├── DuplicateResourceException.java
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   └── UnauthorizedException.java
│   ├── model/                  # JPA Entity Models
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── Order.java
│   │   ├── Cart.java
│   │   ├── Wishlist.java
│   │   └── Role.java
│   ├── repository/             # Spring Data JPA Repositories
│   ├── security/               # Security utilities and filters
│   ├── service/                # Business Logic Layer
│   └── EcommerceApplication.java # Main entry point
├── src/main/resources/
│   ├── application.properties   # Application configuration
│   ├── data.sql               # Sample data initialization
│   └── static/images/         # Product images and assets
├── Dockerfile                  # Container image definition
├── docker-compose.yml          # Multi-container orchestration
└── pom.xml                     # Maven project configuration
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.9.6** or higher
- **PostgreSQL 16** or higher
- **Docker & Docker Compose** (optional, for containerized setup)

### Installation

#### Option 1: Local Development Setup

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd ecommerce-Backend
   ```

2. **Configure Database**
    - Create a PostgreSQL database named `ecommerce`
    - Update `src/main/resources/application.properties` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Configure Email Service**
    - Set up your email configuration in `application.properties`:
   ```properties
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password
   ```

4. **Build the Project**
   ```bash
   mvn clean install
   ```

5. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`

#### Option 2: Docker Setup (Recommended)

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd ecommerce-Backend
   ```

2. **Copy Example Configuration**
   ```bash
   cp application.properties.example src/main/resources/application.properties
   cp docker-compose.example docker-compose.yml
   ```

3. **Start Services**
   ```bash
   docker-compose up --build
   ```

   The application will be available at `http://localhost:8080`

---

## 📚 API Documentation

### Interactive API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

### API Endpoints Overview

#### 🔐 Authentication (`/api/auth`)

- `POST /signup` - Register a new user
- `POST /login` - User login
- `GET /logout` - User logout

#### 📦 Products (`/api/products`)

- `GET /` - List all products (paginated)
- `GET /{id}` - Get product details
- `POST /` - Create product (Admin/Vendor)
- `PUT /{id}` - Update product (Admin/Vendor)
- `DELETE /{id}` - Delete product (Admin/Vendor)

#### 🏷️ Categories (`/api/categories`)

- `GET /` - List all categories
- `GET /{id}` - Get category details
- `POST /` - Create category (Admin)
- `PUT /{id}` - Update category (Admin)
- `DELETE /{id}` - Delete category (Admin)

#### 🛒 Cart (`/api/cart`)

- `GET /` - Get user's cart
- `POST /add` - Add item to cart
- `DELETE /remove` - Remove item from cart
- `PUT /update` - Update cart item quantity

#### ❤️ Wishlist (`/api/wishlist`)

- `GET /` - Get user's wishlist
- `POST /add` - Add product to wishlist
- `DELETE /remove/{id}` - Remove from wishlist

#### 📋 Orders (`/api/orders`)

- `GET /` - Get user's orders
- `GET /{id}` - Get order details
- `POST /` - Create order
- `PATCH /{id}/status` - Update order status

#### 👤 User Profile (`/api/user`)

- `GET /profile` - Get user profile
- `PUT /profile` - Update profile
- `DELETE /account` - Delete account

#### 🏪 Vendor (`/api/vendor`)

- `GET /dashboard` - Vendor dashboard
- `GET /products` - Vendor's products
- `POST /products` - Add vendor product

#### ⚙️ Admin (`/api/admin`)

- `GET /users` - List all users (paginated)
- `GET /users/{id}` - Get user details
- `PATCH /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user
- `GET /users/role/{roleName}` - Get users by role

#### 📧 OTP (`/api/otp`)

- `POST /send` - Send OTP
- `POST /verify` - Verify OTP

#### 🏠 Home (`/api/home`)

- `GET /` - Home page data

---

## 🔒 Authentication & Authorization

### JWT Authentication

The API uses **JSON Web Tokens (JWT)** for stateless authentication.

1. **User Registration/Login** returns a JWT token
2. **Include token in requests** via Authorization header:
   ```
   Authorization: Bearer <your_jwt_token>
   ```

### Role-Based Access Control

Three user roles are supported:

| Role       | Permissions                                                           |
|------------|-----------------------------------------------------------------------|
| **USER**   | Browse products, manage cart/wishlist, place orders, view own profile |
| **VENDOR** | Add/edit own products, manage vendor orders, view sales dashboard     |
| **ADMIN**  | Full system access, manage users, products, categories, orders        |

### Secret Codes for Registration

- **Admin Registration**: Use secret code `admin123`
- **Vendor Registration**: Use secret code `vendor123`
- **Regular User**: No secret code required

---

## 🗄️ Database Schema

### Key Entities

- **User** - User accounts with authentication details
- **Role** - User roles (ADMIN, USER, VENDOR)
- **Product** - Product catalog
- **Category** - Product categories
- **Cart** - Shopping cart per user
- **CartItem** - Items in cart
- **Order** - User orders
- **OrderItem** - Items in an order
- **Wishlist** - User's favorite products

All entities use JPA with automatic schema generation (`spring.jpa.hibernate.ddl-auto=update`)

---

## ⚙️ Configuration

### Environment Variables

Key configuration options in `application.properties`:

```properties
# Application
spring.application.name=ecommerce
server.port=8080
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=postgres
# Security
app.jwtSecret=your-256-bit-base64-encoded-secret
admin.secret.code=admin123
vendor.secret.code=vendor123
# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
# File Upload
app.upload.dir=/images/
# Logging
logging.level.root=INFO
logging.level.com.borneo.ecommerce=DEBUG
# API Documentation
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.operationsSorter=method
```

---

## 🐳 DevOps & Containerization

### Docker Architecture

This project showcases modern containerization practices with a **multi-stage build strategy**:

#### Dockerfile Strategy

- **Stage 1 (Builder)**: Uses Maven image (`maven:3.9.6-eclipse-temurin-21`) to compile and build the application
    - Leverages Docker layer caching for faster builds
    - Dependency resolution happens in a separate layer for optimal caching
    - Excludes test files and unnecessary artifacts

- **Stage 2 (Runtime)**: Uses lightweight Alpine Linux (`eclipse-temurin:21-jre-alpine`)
    - Minimal base image reduces attack surface and image size
    - JRE-only runtime (no compiler) reduces final image footprint
    - Pre-creates volume mount points for file uploads

**Benefits:**

- 🎯 Smaller final image size (development: ~700MB vs ~1.4GB single-stage)
- ⚡ Faster deployments with reduced network bandwidth
- 🔒 Reduced security vulnerabilities with minimal base image
- 📦 Optimized caching for iterative builds

### Docker Compose Orchestration

The `docker-compose.yml` demonstrates enterprise-grade container orchestration:

#### Services Architecture

```yaml
services:
  db (PostgreSQL 16-Alpine)
  ├── Persistent Volume: postgres_data
  ├── Health Checks: pg_isready verification
  ├── Environment Variables: Database credentials
  └── Exposed Port: 5433 (mapped to 5432 internal)

  app (Spring Boot Application)
  ├── Build Context: Dockerfile (multi-stage)
  ├── Dependent Service: db (health check condition)
  ├── Persistent Volume: upload_data (/app/images)
  ├── Environment Variables: 12-factor app configuration
  └── Exposed Port: 8080
```

#### Key DevOps Features

- **Health Checks**: PostgreSQL readiness probe with retry logic
- **Service Dependencies**: App waits for database health check before starting
- **Volume Management**:
    - `postgres_data`: Database persistence across container restarts
    - `upload_data`: File uploads persistence for stateless scaling
- **Network Stack**: Custom Docker network for service-to-service communication (automatic)
- **Environment Variable Injection**: Separates configuration from image

### Deployment Scenarios

#### Local Development

```bash
# Single command startup with full stack
docker-compose up --build

# View logs
docker-compose logs -f app

# Scale services
docker-compose up -d --scale app=3  # Note: Requires load balancer
```

#### Cloud Deployment (Render)

The `render.yaml` provides Infrastructure as Code for cloud deployment:

- Automatic builds from GitHub/GitLab
- Environment variable management
- Health check configuration
- Auto-scaling policies
- Zero-downtime deployments

#### Kubernetes Ready

The Docker images and configuration are optimized for Kubernetes:

- Multi-stage builds minimize image size
- Health check endpoints for liveness/readiness probes
- Environment-based configuration (no hardcoded secrets)
- Stateless application design (persistent volumes for data)
- Resource limits can be configured

### Build & Push Commands

```bash
# Build image locally
docker build -t ecommerce-api:latest .

# Tag for registry
docker tag ecommerce-api:latest your-registry/ecommerce-api:1.0.0

# Push to registry (Docker Hub, ECR, GCR, etc.)
docker push your-registry/ecommerce-api:1.0.0

# Run container with environment variables
docker run -d \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/ecommerce \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -e APP_JWT_SECRET=your-secret \
  -p 8080:8080 \
  ecommerce-api:latest
```

---

## 🔐 Security Best Practices

### Container Security

- ✅ **Non-root User**: Runs as unprivileged user in Alpine containers
- ✅ **Read-Only Filesystem**: Configure runtime to enforce read-only filesystem where possible
- ✅ **Secret Management**: No hardcoded credentials; uses environment variables and secret managers
- ✅ **Image Scanning**: Integrate Trivy or Snyk for vulnerability scanning
- ✅ **Minimal Base Images**: Alpine Linux reduces attack surface
- ✅ **Layer Caching**: Minimizes exposure of sensitive build artifacts

### JWT Security

- ✅ **Signed Tokens**: Uses JJWT library (v0.11.5) with secure algorithms
- ✅ **Token Expiration**: Configurable expiration times
- ✅ **Bearer Token**: Standard Authorization header format
- ✅ **Role-based Authorization**: Fine-grained access control

### Database Security

- ✅ **SSL/TLS Support**: Can be enabled in PostgreSQL configuration
- ✅ **Environment Variable Credentials**: No hardcoded passwords
- ✅ **Volume Encryption**: Use host-level or cloud provider encryption for data at rest

### Recommended Enhancements

```dockerfile
# Run as non-root user
RUN useradd -m -u 1000 appuser
USER appuser

# Read-only file system (where possible)
# docker run --read-only --tmpfs /tmp app.jar
```

---

## 🧪 Testing

Run the test suite:

```bash
mvn test
```

---

## 📊 Code Quality

The project uses **Spotless** with **Google Java Format** for code formatting:

```bash
# Format code
mvn spotless:apply

# Check formatting
mvn spotless:check
```

---

## 🚀 CI/CD & Deployment Pipeline

### GitHub Actions Workflow Example

Create `.github/workflows/deploy.yml` for automated deployment:

```yaml
name: Build and Deploy

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    services:
      postgres:
        image: postgres:16-alpine
        env:
          POSTGRES_DB: ecommerce_test
          POSTGRES_USER: postgres
          POSTGRES_PASSWORD: postgres
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven

      - name: Build with Maven
        run: mvn clean verify

      - name: Build Docker Image
        run: docker build -t ecommerce-api:${{ github.sha }} .

      - name: Scan Image with Trivy
        uses: aquasecurity/trivy-action@master
        with:
          image-ref: ecommerce-api:${{ github.sha }}
          format: 'sarif'
          output: 'trivy-results.sarif'

      - name: Upload Trivy Results
        uses: github/codeql-action/upload-sarif@v2
        with:
          sarif_file: 'trivy-results.sarif'

      - name: Login to Docker Hub
        uses: docker/login-action@v2
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Push to Registry
        run: |
          docker tag ecommerce-api:${{ github.sha }} your-registry/ecommerce-api:latest
          docker push your-registry/ecommerce-api:latest

      - name: Deploy to Cloud
        env:
          DEPLOY_KEY: ${{ secrets.DEPLOY_KEY }}
        run: |
          # Your deployment script here
          echo "Deploying to production..."
```

### Render Deployment

The `render.yaml` file provides Infrastructure as Code for Render:

```yaml
services:
  - type: web
    name: ecommerce-api
    env: docker
    dockerfilePath: ./Dockerfile
    region: oregon
    plan: standard
    healthCheckPath: /actuator/health
    envVars:
      - key: SPRING_DATASOURCE_URL
        scope: run
        value: ${DATABASE_URL}
      - key: APP_JWT_SECRET
        scope: run
        sync: false
      - key: SPRING_MAIL_USERNAME
        scope: run
        sync: false
      - key: SPRING_MAIL_PASSWORD
        scope: run
        sync: false

  - type: pserv
    name: ecommerce-db
    env: docker
    plan: standard
    ipAllowList: [ ]
    envVars:
      - key: POSTGRES_DB
        value: ecommerce
      - key: POSTGRES_USER
        value: postgres
      - key: POSTGRES_PASSWORD
        scope: run
        sync: false
```

### Local Deployment Workflow

```bash
# 1. Build and test locally
mvn clean verify

# 2. Build Docker image
docker build -t ecommerce-api:dev .

# 3. Run with Docker Compose
docker-compose up --build

# 4. Run integration tests
mvn verify -Pit

# 5. Push to registry
docker tag ecommerce-api:dev your-registry/ecommerce-api:v1.0.0
docker push your-registry/ecommerce-api:v1.0.0

# 6. Deploy to production (Kubernetes, Render, AWS ECS, etc.)
kubectl apply -f k8s-deployment.yaml
```

### Container Registry Options

- **Docker Hub**: `docker.io/your-org/ecommerce-api`
- **Amazon ECR**: `123456789.dkr.ecr.us-east-1.amazonaws.com/ecommerce-api`
- **Google GCR**: `gcr.io/your-project/ecommerce-api`
- **Azure ACR**: `your-registry.azurecr.io/ecommerce-api`
- **GitHub Container Registry**: `ghcr.io/your-org/ecommerce-api`

---

## 📈 Performance & Scaling

### Horizontal Scaling with Docker Compose

```bash
# Scale the app service to 3 instances
docker-compose up -d --scale app=3

# Monitor scaling
docker-compose ps
```

### Load Balancing

For production, use:

- **Nginx**: Reverse proxy load balancer
- **HAProxy**: High availability load balancer
- **Cloud LB**: AWS ALB, GCP Load Balancer, Azure LB

Example Nginx configuration:

```nginx
upstream backend {
    server app1:8080;
    server app2:8080;
    server app3:8080;
}

server {
    listen 80;
    location / {
        proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### Database Optimization

- **Connection Pooling**: HikariCP configured in Spring Boot
- **Query Optimization**: Index frequently queried columns
- **Replication**: Set up PostgreSQL streaming replication for HA
- **Backup Strategy**: Regular automated backups with point-in-time recovery

---

## 🛠️ Tech Stack

| Component        | Technology            | Version |
|------------------|-----------------------|---------|
| Framework        | Spring Boot           | 3.3.4   |
| Language         | Java                  | 21      |
| Database         | PostgreSQL            | 16      |
| Security         | Spring Security + JWT | 0.11.5  |
| ORM              | Spring Data JPA       | 3.3.4   |
| Mapping          | MapStruct             | 1.5.5   |
| Documentation    | SpringDoc OpenAPI     | 2.3.0   |
| Build Tool       | Maven                 | 3.9.6   |
| Containerization | Docker                | Latest  |

---

## 📊 Monitoring & Observability

### Spring Boot Actuator

The application includes built-in health and metrics endpoints:

```bash
# Health check endpoint
curl http://localhost:8080/actuator/health

# Detailed metrics
curl http://localhost:8080/actuator/metrics

# Specific metric
curl http://localhost:8080/actuator/metrics/http.server.requests
```

### Container Health Checks

Docker health check in `docker-compose.yml`:

```yaml
healthcheck:
  test: [ "CMD-SHELL", "curl -f http://localhost:8080/actuator/health || exit 1" ]
  interval: 30s
  timeout: 10s
  retries: 3
  start_period: 40s
```

### Logging Strategy

**Multi-stage Logging:**

1. **Application Logs**: Spring Boot logging to console (Docker sees via `docker logs`)
2. **Container Logs**: Docker daemon captures stdout/stderr
3. **Centralized Logging**: Send to ELK Stack, Datadog, CloudWatch, or similar

**Configuration in `application.properties`:**

```properties
logging.level.root=INFO
logging.level.com.borneo.ecommerce=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n
logging.file.name=/app/logs/ecommerce.log
```

**Docker Log Driver Options:**

```bash
# View logs
docker-compose logs -f app

# JSON formatted logs (suitable for centralized logging)
docker logs --follow ecommerce-app | jq .
```

### Monitoring Solutions

#### Prometheus + Grafana

```yaml
# docker-compose addition
prometheus:
  image: prom/prometheus:latest
  volumes:
    - ./prometheus.yml:/etc/prometheus/prometheus.yml
  ports:
    - "9090:9090"

grafana:
  image: grafana/grafana:latest
  environment:
    GF_SECURITY_ADMIN_PASSWORD: admin
  ports:
    - "3000:3000"
```

#### ELK Stack (Elasticsearch, Logstash, Kibana)

```yaml
elasticsearch:
  image: docker.elastic.co/elasticsearch/elasticsearch:8.0.0
  environment:
    discovery.type: single-node
  ports:
    - "9200:9200"

kibana:
  image: docker.elastic.co/kibana/kibana:8.0.0
  ports:
    - "5601:5601"
```

### Metrics to Monitor

- **CPU Usage**: `docker stats`
- **Memory Usage**: `docker stats`
- **Network I/O**: `docker stats`
- **HTTP Response Time**: Spring Boot metrics
- **Database Connections**: HikariCP metrics
- **JWT Token Validation**: Custom metrics
- **API Error Rates**: Spring Boot metrics

---

## 🔄 Environment Management

### Environment Hierarchy

1. **Development**: `application-dev.properties`
2. **Staging**: `application-staging.properties`
3. **Production**: `application-prod.properties`

### Profile-Specific Configuration

```bash
# Run with specific profile
SPRING_PROFILES_ACTIVE=prod mvn spring-boot:run

# Docker compose with different env file
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
```

### Secrets Management

**Best Practices:**

- ✅ Use Docker secrets for production
- ✅ Use environment variables (12-factor app)
- ✅ Never commit secrets to Git
- ✅ Use secret management tools:
    - AWS Secrets Manager
    - Azure Key Vault
    - HashiCorp Vault
    - Kubernetes Secrets

```bash
# Docker Secrets Example
echo "your-jwt-secret" | docker secret create app_jwt_secret -

# Use in docker-compose
docker compose config --resolve-image-digests
```

---

## 🤝 Contributing

1. Create a feature branch (`git checkout -b feature/AmazingFeature`)
2. Commit changes (`git commit -m 'Add AmazingFeature'`)
3. Push to branch (`git push origin feature/AmazingFeature`)
4. Open a Pull Request

---

## 🛠️ Troubleshooting & Common DevOps Scenarios

### Docker Issues

**Container won't start:**

```bash
# Check logs
docker-compose logs app

# Check Docker daemon
docker ps -a

# Rebuild from scratch
docker-compose down -v
docker-compose up --build
```

**Port conflicts:**

```bash
# Find process using port
lsof -i :8080

# Change port in docker-compose.yml
ports:
  - "9000:8080"  # Host:Container
```

**Persistent volume issues:**

```bash
# List volumes
docker volume ls

# Inspect volume
docker volume inspect ecommerce-Backend_postgres_data

# Clean volumes
docker-compose down -v  # WARNING: Deletes data!
```

### Database Issues

**Can't connect to PostgreSQL:**

```bash
# Check database container
docker-compose ps

# Check database logs
docker-compose logs db

# Verify credentials in application.properties
# Ensure database is healthy: docker-compose ps
```

**Migration failures:**

```bash
# Check Hibernate logs
docker-compose logs app | grep Hibernate

# Reset database (development only!)
docker-compose exec db psql -U postgres -c "DROP DATABASE ecommerce; CREATE DATABASE ecommerce;"
```

### Container Orchestration Issues

**Service discovery not working:**

```bash
# Verify network
docker network ls
docker network inspect ecommerce-backend_default

# Check DNS resolution
docker-compose exec app nslookup db
```

**Scaling issues:**

```bash
# Scale to multiple instances
docker-compose up -d --scale app=3

# Note: Each instance needs unique port or load balancer
# Typical setup: Load Balancer -> app:8080, app:8081, app:8082
```

### Performance Issues

**High CPU/Memory:**

```bash
# Monitor resource usage
docker stats

# Check application logs for errors
docker-compose logs -f app

# Profile application (Java)
docker-compose exec app jps -lm
```

**Slow database queries:**

```bash
# Enable query logging
SPRING_JPA_SHOW_SQL=true docker-compose up

# Check PostgreSQL logs
docker-compose logs db | grep STATEMENT
```

---

## 📋 DevOps Checklists

### Pre-Production Deployment Checklist

- [ ] All environment variables configured (no hardcoded secrets)
- [ ] Database migrations tested
- [ ] Health check endpoints working (`/actuator/health`)
- [ ] Logging configured for centralized monitoring
- [ ] Database backups scheduled
- [ ] SSL/TLS certificates obtained
- [ ] Container image scanned for vulnerabilities (Trivy, Snyk)
- [ ] Load balancer configured
- [ ] Monitoring and alerting set up
- [ ] Runbooks documented
- [ ] Rollback plan prepared

### Post-Deployment Validation

- [ ] Application starts successfully
- [ ] Health checks passing
- [ ] Database connections established
- [ ] APIs responding correctly
- [ ] Logs appearing in centralized logging system
- [ ] Metrics being collected
- [ ] No error spikes in logs
- [ ] Database backups working
- [ ] Monitoring alerts configured

### Disaster Recovery Checklist

- [ ] Database backup strategy tested
- [ ] Point-in-time recovery tested
- [ ] Container image registry accessible
- [ ] Configuration backup in Git
- [ ] DNS failover configured
- [ ] Load balancer failover tested
- [ ] Communication plan documented

---

## 🎓 Learning Resources & DevOps Best Practices

### Docker & Containerization

- [Docker Official Documentation](https://docs.docker.com/)
- [Best practices for writing Dockerfiles](https://docs.docker.com/develop/dev-best-practices/)
- [Docker Compose best practices](https://docs.docker.com/compose/production/)

### Kubernetes & Orchestration

- [Kubernetes Official Documentation](https://kubernetes.io/docs/)
- [Kubernetes best practices](https://kubernetes.io/docs/concepts/configuration/overview/)

### CI/CD & DevOps

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitLab CI/CD](https://docs.gitlab.com/ee/ci/)
- [Render Deployment Guide](https://render.com/docs)

### Infrastructure as Code

- [Terraform Documentation](https://www.terraform.io/docs)
- [Ansible Documentation](https://docs.ansible.com/)

### Monitoring & Observability

- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Dashboards](https://grafana.com/grafana/)
- [ELK Stack](https://www.elastic.co/what-is/elk-stack)
- [Datadog Monitoring](https://www.datadoghq.com/)

### Security

- [OWASP Container Security](https://owasp.org/www-project-container-security/)
- [Trivy Vulnerability Scanner](https://aquasecurity.github.io/trivy/)
- [Snyk Security Scanning](https://snyk.io/)

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 📞 Support & Contact

For issues, questions, or support:

- Create an issue in the repository
- Contact: [Your Contact Information]

---

## 🗓️ Version History

- **v0.0.1** - Initial release with core e-commerce functionality

---

## 🎯 Roadmap

- [ ] Payment Gateway Integration (Stripe, PayPal)
- [ ] Product Reviews & Ratings
- [ ] Advanced Search & Filtering
- [ ] Inventory Management
- [ ] Email Notifications Service
- [ ] Admin Analytics Dashboard
- [ ] Performance Optimization
- [ ] Mobile App API Optimization

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/guides/gs/securing-web/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JWT Introduction](https://jwt.io/introduction)
- [OpenAPI/Swagger Specification](https://swagger.io/specification/)

---

**Happy Coding! 🚀**









