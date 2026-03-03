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
