
# Eatsmeet 🍱

**Eatsmeet** is a robust backend REST API for a Food Ordering System built with Java and Spring Boot. It provides comprehensive features for managing menus, orders, users, and employees with secure role-based access control.

☁ Cloud Architecture Style Diagram 

                      ┌──────────────────────────┐ 
                      │        Internet          │
                      └─────────────┬────────────┘
                                    │
                                    ▼
                ┌──────────────────────────────────┐
                │          Vercel Platform         │
                │  - Angular Static Hosting        │
                │  - CDN Distribution              │
                │  - HTTPS SSL                     │
                └─────────────┬────────────────────┘
                              │
                              ▼
                ┌──────────────────────────────────┐
                │            AWS Cloud             │
                │                                  │
                │  ┌──────────────────────────┐    │
                │  │        EC2 Instance      │    │
                │  │  Spring Boot (Docker)    │    │
                │  └─────────────┬────────────┘    │
                │                │                 │
                │        ┌───────────────┐         │
                │        │   MySQL DB    │         │
                │        │ (Local/RDS)   │         │
                │        └───────────────┘         │
                └──────────────────────────────────┘

## Table of Contents

* [Features](https://www.google.com/search?q=%23-features)
* [Tech Stack](https://www.google.com/search?q=%23-tech-stack)
* [Prerequisites](https://www.google.com/search?q=%23-prerequisites)
* [Project Structure](https://www.google.com/search?q=%23project-structure)
* [Configuration & Setup](https://www.google.com/search?q=%23-configuration--setup)
* [API Documentation & Testing](https://www.google.com/search?q=%23-api-documentation--testing)
* [How to Contribute](https://www.google.com/search?q=%23how-to-contribute)
* [Coding Standards](https://www.google.com/search?q=%23coding-standards)
* [Pull Request Process](https://www.google.com/search?q=%23pull-request-process)
* [Reporting Issues](https://www.google.com/search?q=%23reporting-issues)
* [License](https://www.google.com/search?q=%23-license)
* [Author](https://www.google.com/search?q=%23-author)

---

## 🚀 Features

* **User Authentication & Authorization**: Secure login and registration using **JWT (JSON Web Tokens)** with Access and Refresh token mechanisms.
* **Role-Based Access Control (RBAC)**: Distinct roles for `USER`, `ADMIN`, `SUB_ADMIN`, and `EMPLOYEE`.
* **Food & Menu Management**:
* CRUD operations for Foods, Curries, Snacks, and Bites.
* Dynamic menu scheduling: Assign specific curries to foods based on Date and Meal Time (Breakfast, Lunch, Dinner).


* **Employee Management**: Admin tools to register, update, and manage employee roles and details.
* **Order System**: (Backend foundation) Cart management and Order processing entities.
* **Email Notifications**: Integration with JavaMailSender for account validation and credential distribution.
* **Search & Filtering**: Search foods by name, price range, and categories.

## 🛠️ Tech Stack

* **Language**: Java 21
* **Framework**: Spring Boot 3.3.4
* **Build Tool**: Maven
* **Database**: MySQL
* **Security**: Spring Security, JWT (jjwt-api 0.12.6)
* **ORM**: Spring Data JPA (Hibernate)
* **Email**: Spring Boot Starter Mail

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

* [Java Development Kit (JDK) 21](https://www.google.com/search?q=https://www.oracle.com/java/technologies/downloads/%23java21)
* [suspicious link removed]
* [Maven](https://maven.apache.org/) (optional, as the project includes the Maven Wrapper)

## Project Structure

The source code is organized into a standard Spring Boot layered architecture under `src/main/java/com/wak/eatsmeet`:

```text
com.wak.eatsmeet
├── config          # Security (JWT, CORS) & App Configurations
├── controller      # REST Controllers (API Endpoints)
├── dto             # Data Transfer Objects (Request/Response models)
├── model           # JPA Entities (Database Tables)
│   ├── cart        # Cart & CartItems entities
│   ├── food        # Food, Curry, Snacks entities
│   ├── order       # Orders & OrderItems entities
│   └── user        # Users & Employees entities
├── repository      # Data Access Layer (JPA Repositories)
├── service         # Business Logic Layer
└── EatsmeetApplication.java  # Main Entry Point

```

## ⚙️ Configuration & Setup

1. **Clone the Repository**
```bash
git clone https://github.com/pathugit/eatsmeet.git
cd eatsmeet

```


2. **Database Setup**
* Create a MySQL database named `eatsmeet`.
* *Note: The tables will be automatically created via Hibernate (`ddl-auto=update`).*


3. **Configure Environment Variables**
Open `src/main/resources/application.properties` and update the following settings with your local configurations:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/eatsmeet
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Email Configuration (for verification emails)
spring.mail.username=YOUR_GMAIL_ADDRESS
spring.mail.password=YOUR_GMAIL_APP_PASSWORD

# JWT Security Configuration
jwt.secret=YOUR_SUPER_SECRET_KEY_MUST_BE_LONG_ENOUGH

```


4. **Create Initial Admin Account**
Since registration is for standard users, you must manually insert the first Admin into the database. Run this SQL command in your MySQL client after running the application once (so tables are created):
```sql
INSERT INTO employees (active, address, bod, contact, email, name, password, role)
VALUES (true, 'Galle', '2001-04-09', '0700000001', 'wdummy00@gmail.com', 'Dummy Person', '$2a$12$CFtl5iFUWNUFOs8sLsQ/fuUUjU.7eMsJcIosZLVJCpf130AbArfUq', 'ADMIN');

```


* **Default Admin Email:** `wdummy00@gmail.com`
* **Default Admin Password:** `123456`



## 🧪 API Documentation & Testing

You can import the full API collection into **Postman** to test endpoints, view request formats, and see example responses.

**[View Postman Collection](https://galactic-moon-315292.postman.co/workspace/My-Workspace~eaa54b7c-323a-4df6-8a4a-ef8ccef88d2f/collection/33857072-c1ed03bc-99bf-47f0-a8f9-6a6dccc5044f?action=share&source=copy-link&creator=33857072)**

Updated postman_collection:  https://github.com/pathuGIT/eatsmeet/blob/main/eatsmeet.postman_collection.json
The API will be available at `http://localhost:8080`.

## How to Contribute

1. **Fork the Repository**: Click the "Fork" button on the top right of the GitHub page.
2. **Clone your Fork**:
```bash
git clone https://github.com/YOUR-USERNAME/eatsmeet.git

```


3. **Create a Branch**: Create a new branch for your feature or bug fix.
```bash
git checkout -b feature/my-new-feature

```


4. **Make Changes**: Write your code and tests.
5. **Commit**: Use descriptive commit messages.
```bash
git commit -m "Add feature: Search food by category"

```


6. **Push**: Push your branch to your fork.
```bash
git push origin feature/my-new-feature

```


7. **Submit a Pull Request**: Open a PR from your branch to the `main` branch of the original repository.

## Coding Standards

Please adhere to the following coding conventions used in this project:

* **Naming**:
* Classes: `PascalCase` (e.g., `FoodController`, `AuthService`).
* Methods/Variables: `camelCase` (e.g., `getFoodsByCurryId`, `totalAmount`).


* **Architecture**:
* Controllers should **only** handle HTTP requests and delegate logic to Services.
* Services should handle business logic and call Repositories.
* Repositories should strictly be interfaces extending `JpaRepository`.


* **Error Handling**:
* Use `try-catch` blocks in Controllers to return appropriate `ResponseEntity` with HTTP status codes.
* Use `ApiResponse` DTO for consistent JSON response structure.



## Pull Request Process

When submitting a Pull Request (PR):

1. **Title**: Use a clear and descriptive title.
2. **Description**: Describe what changes you made and why.
3. **Dependencies**: If you added new Maven dependencies, list them.
4. **Clean Code**: Ensure you have removed any `System.out.println` debug statements (unless necessary for logging) and unused imports.
5. **Secrets**: **NEVER** commit real passwords or API keys in `application.properties`.

## Reporting Issues

If you find a bug or have a feature request, please open an Issue on GitHub.

* **Bug Reports**: Include steps to reproduce, expected behavior, and actual behavior.
* **Feature Requests**: Describe the feature and the problem it solves.

## 📄 License

Distributed under the **MIT License**. See `LICENSE` for more information.

## 👤 Author

**pathuGIT**
