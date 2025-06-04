# 🏢 Asset Management REST API

This is a Spring Boot 2.4-based REST API application for managing assets in a company. It provides CRUD operations for Employees, Assets, and Categories using Spring Data JPA and an in-memory H2 database.

---

## 🚀 Features

- CRUD APIs for:
  - ✅ Employees
  - 💼 Assets
  - 🗂️ Categories
- H2 in-memory database for quick testing
- RESTful architecture
- Spring Boot 2.4
- Maven build system

---

## 🧪 API Endpoints

### 📌 Employee
| Method | Endpoint             | Description         |
|--------|----------------------|---------------------|
| GET    | `/api/employees`     | Get all employees   |
| POST   | `/api/employees`     | Add a new employee  |

### 💼 Asset
| Method | Endpoint                              | Description       |
|--------|---------------------------------------|-------------------|
| GET    | `/api/assets`                         | Get all assets    |
| POST   | `/api/assets`                         | Add new asset     |
| GET    | `/api/assets/{id}`                    | Get asset by ID   |
| PUT    | `/api/assets/{id}`                    | Update asset      |
| DELETE | `/api/assets/{id}`                    | Delete asset      |
| POST   | `/api/assets/{id}/assign/{employeeId}`| Update asset      |
| POST   | `/api/assets/{id}/recover`            | Delete asset      |

### 🗂️ Category
| Method | Endpoint             | Description         |
|--------|----------------------|---------------------|
| GET    | `/api/categories`    | Get all categories  |
| POST   | `/api/categories`    | Add new category    |
| PUT    | `/api/categories/{id}`| Update category    |

---

## ⚙️ How to Run

```bash
# Clone the project
git clone https://github.com/your-username/asset-management-api.git

# Navigate into the project
cd demo

# Build and run using Maven
./mvnw spring-boot:run
