# 📈 Stock Portfolio Monitor

A full-stack Java Spring Boot application to manage and monitor user stock portfolios with features like gain/loss tracking, alerts, reports, and secure authentication.

---

## 🌐 Domain

**Finance / Stock Management / Investment Tracking**

---

## 🎯 Objectives

- Allow users to register and manage their stock holdings
- Track portfolio performance including gain/loss analytics
- Generate alerts for predefined conditions (e.g. major losses)
- Provide reports and summaries of portfolio status
- Secure role-based access and API authentication

---

## 🧱 Tech Stack

| Layer | Technology |
| --- | --- |
| Framework | Spring Boot |
| Security | Spring Security + JWT |
| Persistence | Spring Data JPA |
| Database | MySQL |
| Build Tool | Maven |
| Utilities | Lombok, Jackson Config |
| Testing | JUnit |
| Documentation | Swagger (springdoc-openapi) |

---

## 🧩 Key Modules

- **User Management**: User creation, login, and update
- **Portfolio Tracking**: Add and manage portfolios and stock holdings
- **Gain/Loss Reports**: View performance over time
- **Alerts**: Configurable alerts for financial thresholds
- **Dashboard Reports**: Summarized financial insights

---

## 🔐 Roles & Access

| Role | Permissions |
| --- | --- |
| User | Manage own stocks, view reports and alerts |

---

## 🗃 Entity Overview

- **User**: ID, username, email, password
- **Portfolio**: ID, userID, title, creationDate
- **StockHolding**: ID, portfolioID, stockName, quantity, buyPrice, currentPrice
- **Alert**: ID, userID, type (e.g., LOSS_THRESHOLD), message, createdDate

---

## 🔁 REST API Endpoints

### 🛡 AuthController

- `POST /api/auth/login`

### 👤 UserController

- `GET /api/users`
- `PUT /api/users/update/{id}`

### 💼 PortfolioController

- `POST /api/portfolio`
- `GET /api/portfolio/user/{userId}`
- `GET /api/portfolio/{id}`

### 📊 ReportController

- `GET /api/report/summary/{userId}`
- `GET /api/report/gainloss/{userId}`

### 📈 StockHoldingController

- `POST /api/stock/add`
- `GET /api/stock/portfolio/{portfolioId}`
- `DELETE /api/stock/delete/{id}`

### 🚨 AlertController

- `GET /api/alerts/user/{userId}`

---

# **🖼 ER Diagram**

![ER_StockPortfolio drawio.png](https://github.com/user-attachments/assets/c2535c65-7fa4-4f53-b651-a3f22a3e008e)

## ⚙ Sample Configuration (`application.properties`)

```
spring.application.name=stock-portfolio-monitor
spring.datasource.url=jdbc:mysql://10.9.115.193:3306/stock_portfolio
spring.datasource.username=teamuser
spring.datasource.password=team123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.security.user.name=admin
spring.security.user.password=admin123
spring.jpa.hibernate.ddl-auto=update
#spring.jpa.show-sql=true
#spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
server.port=9000
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=vachan316@gmail.com
spring.mail.password=xyowryeocxqecghy
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.debug=true 
```

## 📁 Project Structure

com.prod.stockmonitor.stock_portfolio_monitor

├── controller

├── dto

├── exceptions

├── model

├── repository

├── service

├── config

├── security

└── StockPortfolioMonitorApplication.java

---

## ▶️ How to Use

### 🛠 Prerequisites

- Java 17+
- Maven
- MySQL
- Postman (optional)
- Swagger UI

### 🚀 Running the App

```bash
git clone <https://github.com/your-username/stock-portfolio-monitor.git>
cd stock-portfolio-monitor

# Create a MySQL database
CREATE DATABASE stock_db;

# Configure application.properties with DB credentials

# Build and run the project
./mvnw clean install
./mvnw spring-boot:run

```

Access Swagger UI at:

[http://localhost:9000/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 📊 Example Use Case

1. User logs in via `/api/auth/login`
2. Creates a portfolio using `/api/portfolio`
3. Adds stocks using `/api/stock/add`
4. System calculates gain/loss and generates reports
5. Alerts are triggered for configured thresholds

---

## 👥 Authors

- Vachan Shetty – Project Lead & Backend Developer
- Animish Radke– User login/registration, JWT Authentication
- Shubham Gupta- Portfolio Module, Reporting Module, Global Exceptions
- Sayantan Mandal- User Profile Management, Gain/Loss Module, JUnit Testing
- Yashvardhan Jaiswal- Alert Module , JUnit Testing
- Rishabh Pathak- Real-Time Price Fetcher module

---

## 

## **🤝 Contributors**

Thanks to everyone who contributed through feedback, testing, or documentation.
