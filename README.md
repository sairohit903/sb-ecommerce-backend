# 🛒 SB-Ecom Backend API

A secure and scalable **E-Commerce Backend Application** built using **Spring Boot**, featuring JWT Authentication, Role-Based Authorization, Cart Management, Order Processing, and Address Management.

This project demonstrates strong backend fundamentals including security, database relationships, and RESTful API design.

---

# 📌 Project Overview

This is a backend REST API for an E-Commerce system that implements:

* 🔐 Secure authentication using JWT
* 👥 Role-based access control (USER & ADMIN)
* 🛍 Product management
* 🛒 User-specific cart functionality
* 📦 Order management
* 📍 Address management

Built with clean architecture and production-ready security practices.

---

# ✨ Features

## 🔐 Authentication & Security

* ✔ JWT-based authentication
* ✔ Custom `UserDetailsService` implementation
* ✔ Stateless session management
* ✔ Custom JWT filter (`OncePerRequestFilter`)
* ✔ Custom `AuthenticationEntryPoint`
* ✔ Password encryption using BCrypt

---

## 👥 Role-Based Authorization

* ✔ ROLE_USER
* ✔ ROLE_ADMIN
* ✔ Admin-only product management
* ✔ User-specific cart access
* ✔ Endpoint protection using Spring Security

### 👤 USER Can:

* View products
* Add to cart
* Update cart
* Place orders
* Manage addresses

### 👨‍💼 ADMIN Can:

* Create products
* Update products
* Delete products
* Manage inventory

---

## 🛍 Product Management

* ✔ Create product (Admin only)
* ✔ Update product (Admin only)
* ✔ Delete product (Admin only)
* ✔ View all products
* ✔ Pagination & sorting support

---

## 🛒 Cart Management

* ✔ Add product to cart
* ✔ Update product quantity
* ✔ Remove product from cart
* ✔ View authenticated user's cart
* ✔ Automatic cart total recalculation
* ✔ Cart securely mapped to logged-in user

---

## 📍 Address Management

* ✔ Add new address
* ✔ Update address
* ✔ Delete address
* ✔ View all addresses of logged-in user
* ✔ Link address to order

---

## 📦 Order Management

* ✔ Place order from cart
* ✔ Order linked with User and Address
* ✔ OrderItems created automatically
* ✔ Product stock reduced after order placement
* ✔ View all orders of logged-in user
* ✔ View order by ID

---

# 🏗 Tech Stack

* ☕ Java 17+
* 🌱 Spring Boot
* 🔐 Spring Security
* 🧾 JWT (JJWT)
* 🗄 Hibernate / JPA
* 🛢 MySQL
* 📦 Maven
* 🧪 Postman

---

# 📂 Project Structure

```
com.rohit.sb_ecom
│
├── controller
├── service
├── repository
├── model
├── payload (DTOs)
├── security
│   ├── jwt
│   ├── security_service
│   └── config
├── exception
└── utils
```

---

# 🔄 Authentication Flow

1. User registers
2. User logs in with username & password
3. `AuthenticationManager` validates credentials
4. JWT token is generated
5. Client sends JWT in `Authorization` header
6. JWT filter validates token
7. SecurityContext is set
8. Role-based authorization is enforced

Example Header:

```
Authorization: Bearer <your_token>
```

---

# 🧠 Key Concepts Implemented

* ✔ Custom `UserDetails` implementation
* ✔ Custom `AuthenticationEntryPoint`
* ✔ JWT filter integration
* ✔ Many-to-Many relationship between User and Role
* ✔ EAGER role fetching
* ✔ DTO-based API responses
* ✔ Global exception handling
* ✔ Clean layered architecture
* ✔ Transaction management
* ✔ Stream API usage
* ✔ Automatic cart total calculation logic

---

# ⚙️ How to Run

1. Clone the repository
2. Configure MySQL database in `application.properties`
3. Run:

```
mvn clean install
mvn spring-boot:run
```

4. Access APIs at:

```
http://localhost:8080
```

---

# 🚀 Future Enhancements

* 💳 Payment Gateway Integration
* 📦 Order Status Tracking (Placed, Shipped, Delivered)
* ☁ AWS Deployment
* 🐳 Docker Support
* 📄 Swagger API Documentation
* 🎟 Coupon System

---

# 👨‍💻 Author

**Rohit**
Backend Developer | Spring Boot | Security | System Design Enthusiast

---

⭐ If you found this project helpful, consider giving it a star!
