# 🛒 Ecommerce Backend API

### DevOps & Containerization Showcase

A production-ready RESTful ecommerce backend built with **Spring Boot 3.3.4** and **Java 21**, engineered as a **DevOps
showcase project**. Demonstrates industry best practices in containerization, CI/CD automation, infrastructure as code,
and cloud deployment.

[![CI/CD Pipeline](https://github.com/Dhaerieshan/ecommerce-backend/actions/workflows/deploy.yml/badge.svg)](https://github.com/Dhaerieshan/ecommerce-backend/actions/workflows/deploy.yml)

---

## 🔗 Live Demo

| Service             | URL                                                                                                                                    |
|---------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| 📖 Swagger API Docs | [https://ecommerce-backend-ik3q.onrender.com/swagger-ui/index.html](https://ecommerce-backend-ik3q.onrender.com/swagger-ui/index.html) |
| ❤️ Health Check     | [https://ecommerce-backend-ik3q.onrender.com/actuator/health](https://ecommerce-backend-ik3q.onrender.com/actuator/health)             |

> ⚠️ First request may take 30-60 seconds — free tier spins down after inactivity

---

## 🏗️ Architecture

```
GitHub (source code)
       │
       ▼
GitHub Actions (CI/CD Pipeline)
  ├── Stage 1: Build & Test (Maven)
  ├── Stage 2: Build Docker Image (tagged with git SHA)
  └── Stage 3: Deploy to Render (webhook trigger)
       │
       ▼
Render.com (Cloud Hosting)
  ├── Spring Boot App (Docker container)
  └── PostgreSQL Database (managed)
```

---

## 🛠️ Tech Stack

| Component        | Technology                  | Version |
|------------------|-----------------------------|---------|
| Framework        | Spring Boot                 | 3.3.4   |
| Language         | Java                        | 21      |
| Database         | PostgreSQL                  | 16      |
| Security         | Spring Security + JWT       | -       |
| ORM              | Spring Data JPA + Hibernate | -       |
| Documentation    | SpringDoc OpenAPI           | 2.3.0   |
| Build Tool       | Maven                       | 3.9.6   |
| Containerization | Docker (multi-stage)        | -       |

---

## ✨ Features

### 🏗️ DevOps Features

- 🐳 **Multi-stage Docker build** — Alpine-based images, ~265MB final size vs 700MB+ single-stage
- 🔗 **Docker Compose orchestration** — Full local stack with PostgreSQL, health checks, volume persistence
- 🚀 **GitHub Actions CI/CD** — 3-stage automated pipeline on every push to main
- 📝 **Infrastructure as Code** — `render.yaml` defines entire cloud infrastructure
- ❤️ **Health Monitoring** — Spring Boot Actuator endpoints for liveness checks
- 🔐 **Secrets Management** — Environment variables only, zero hardcoded credentials

### 🎯 Application Features

- 🔐 JWT Authentication with role-based access (USER, VENDOR, ADMIN)
- 🛍️ Products with category hierarchy and search
- 🛒 Cart management
- ❤️ Wishlist
- 📦 Order management
- 👤 User profiles
- 📧 OTP email verification
- 📖 Auto-generated Swagger API documentation

---

## 🚀 CI/CD Pipeline

Every push to `main` triggers a 3-stage automated pipeline:

```
Stage 1 — Build & Test (46s)
  └── Maven compiles code and runs tests
  └── Uploads compiled jar as artifact

Stage 2 — Docker Build (1m 14s)
  └── Builds multi-stage Docker image
  └── Tags image with git commit SHA

Stage 3 — Deploy (3s)
  └── Triggers Render deployment via webhook
  └── Only runs on main branch pushes
```

---

## 🐳 Docker Architecture

**Multi-stage build strategy:**

- **Stage 1 (Builder)** — `maven:3.9.6-eclipse-temurin-21` compiles code, leverages layer caching for fast rebuilds
- **Stage 2 (Runtime)** — `eclipse-temurin:21-jre-alpine` runs the jar — no Maven, no source code, no JDK in production
  image

**Benefits:**

- Final image ~265MB vs 700MB+ single-stage
- Faster deployments, reduced attack surface
- Optimized layer caching for iterative builds

---

## 🐳 Run Locally with Docker

**Prerequisites:** Docker Desktop installed

```bash
# Clone the repo
git clone https://github.com/Dhaerieshan/ecommerce-backend.git
cd ecommerce-backend

# Copy environment template
cp docker-compose.example.yml docker-compose.yml
# Edit docker-compose.yml with your credentials

# Start everything (app + database)
docker compose up --build

# App runs at http://localhost:8080
# Swagger at http://localhost:8080/swagger-ui/index.html
# Health check at http://localhost:8080/actuator/health
```

---

## 📚 API Endpoints Overview

| Module         | Endpoints                                                        |
|----------------|------------------------------------------------------------------|
| 🔐 Auth        | `POST /api/auth/signup`, `POST /api/auth/login`                  |
| 📦 Products    | `GET/POST/PUT/DELETE /api/products`                              |
| 🏷️ Categories | `GET/POST/PUT/DELETE /api/categories`                            |
| 🛒 Cart        | `GET /api/cart`, `POST /api/cart/add`, `DELETE /api/cart/remove` |
| ❤️ Wishlist    | `GET /api/wishlist`, `POST /api/wishlist/add`                    |
| 📋 Orders      | `GET/POST /api/orders`, `PATCH /api/orders/{id}/status`          |
| 👤 User        | `GET/PUT /api/user/profile`                                      |
| 🏪 Vendor      | `GET /api/vendor/dashboard`, `GET /api/vendor/products`          |
| ⚙️ Admin       | `GET/PATCH/DELETE /api/admin/users`                              |
| 📧 OTP         | `POST /api/otp/send`, `POST /api/otp/verify`                     |

Full interactive documentation available at the Swagger link above.

---

## 🔒 Security

- Passwords hashed with BCrypt
- JWT tokens for stateless authentication
- Role-based endpoint protection (USER / VENDOR / ADMIN)
- Secrets managed via environment variables — never hardcoded
- OTP email verification for sensitive operations
- Minimal Docker base image reduces attack surface

---

## 🔧 Environment Variables

| Variable                     | Description                         |
|------------------------------|-------------------------------------|
| `SPRING_DATASOURCE_URL`      | PostgreSQL JDBC URL                 |
| `SPRING_DATASOURCE_USERNAME` | Database username                   |
| `SPRING_DATASOURCE_PASSWORD` | Database password                   |
| `APP_JWT_SECRET`             | JWT signing secret (min 32 chars)   |
| `ADMIN_SECRET_CODE`          | Secret code for admin registration  |
| `VENDOR_SECRET_CODE`         | Secret code for vendor registration |
| `SPRING_MAIL_USERNAME`       | Gmail address for OTP emails        |
| `SPRING_MAIL_PASSWORD`       | Gmail app password                  |

---

## 📁 Project Structure

```
├── .github/
│   └── workflows/
│       └── deploy.yml          # GitHub Actions CI/CD pipeline
├── src/
│   └── main/java/com/borneo/ecommerce/
│       ├── controller/         # REST API endpoints
│       ├── service/            # Business logic
│       ├── repository/         # Database access layer
│       ├── model/              # JPA entities
│       ├── dto/                # Data transfer objects
│       ├── security/           # JWT authentication & filters
│       ├── config/             # Spring Security & CORS config
│       └── exception/          # Global exception handling
├── Dockerfile                  # Multi-stage Docker build
├── docker-compose.yml          # Local development stack
├── docker-compose.example.yml  # Template for local setup
└── render.yaml                 # Infrastructure as Code (IaC)
```

---

## 📊 Monitoring

```bash
# Health check
curl https://ecommerce-backend-ik3q.onrender.com/actuator/health

# Local health check
curl http://localhost:8080/actuator/health
```

---

## 🗓️ Roadmap

- [ ] Terraform IaC (cloud-agnostic infrastructure)
- [ ] Trivy image vulnerability scanning in pipeline
- [ ] Prometheus + Grafana monitoring
- [ ] Kubernetes deployment manifests
- [ ] Payment gateway integration