🛒 SB Ecommerce Backend

A RESTful E-Commerce Backend API built using Spring Boot.

This project demonstrates clean layered architecture, DTO pattern implementation, global exception handling, validation, and Spring Data JPA integration.
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
🚀 Tech Stack

Java 17+

Spring Boot

Spring Data JPA (Hibernate)

H2 Database

Maven

Lombok

Jakarta Validation

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

🏗️ Architecture

Layered Architecture:

Controller → Service → Repository → Model → DTO → Exception

Package Structure:

config

controller

exceptions

model

payload (DTO)

repositories

service

Key Design Principles:

Clean separation of concerns

DTO pattern implemented

Centralized exception handling

Validation using annotations

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

✨ Features Implemented

Category CRUD APIs

DTO mapping (Entity ↔ DTO)

Global exception handling

Custom exception classes

Validation using @NotBlank and @Size

H2 in-memory database integration

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

📡 API Endpoints

Category APIs

GET /api/public/categories
POST /api/public/categories
PUT /api/public/categories/{id}
DELETE /api/public/categories/{id}

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

🧪 Sample Request (Create Category)

{
"categoryName": "Electronics"
}

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

🗄️ Database

H2 Console available at:

http://localhost:8080/h2-console

JDBC URL:
jdbc:h2:mem:test

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

▶️ How To Run

Clone the repository

git clone https://github.com/sairohit903/sb-ecommerce-backend.git

Navigate into the project

cd sb-ecommerce-backend

Run the application

mvn spring-boot:run

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

📈 What This Project Demonstrates

REST API development using Spring Boot

Layered architecture design

Data persistence with JPA

Exception handling best practices

DTO-based API response structure

Clean and maintainable code organization

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

🔜 Upcoming Enhancements

Product Module

JWT Authentication

Role-Based Authorization

MySQL Integration

Swagger Documentation

AWS Deployment

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

👨‍💻 Author

Rohit
Backend Developer | Spring Boot | Java

