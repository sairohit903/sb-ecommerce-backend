🛒 SB-Ecom Backend API

🚀 A secure and scalable E-Commerce Backend Application built using Spring Boot, featuring JWT Authentication, Role-Based Authorization, and a fully functional Cart Management System.

This project demonstrates strong backend fundamentals including security, database relationships, and RESTful API design.

📌 Project Overview

This is a backend REST API for an E-Commerce system that implements:

🔐 Secure authentication using JWT
👥 Role-based access control (USER & ADMIN)
🛍 Product management
🛒 User-specific cart functionality

Built with clean architecture and production-ready security practices.

✨ Features
🔐 Authentication & Security

✔ JWT-based authentication
✔ Custom UserDetailsService implementation
✔ Stateless session management
✔ Custom JWT filter (OncePerRequestFilter)
✔ AuthenticationEntryPoint for unauthorized access
✔ Password encryption using BCrypt

👥 Role-Based Authorization

✔ ROLE_USER
✔ ROLE_ADMIN
✔ Admin-only product management
✔ User-specific cart access
✔ Endpoint protection using Spring Security

🛍 Product Management

✔ Create product (Admin only)
✔ Update product (Admin only)
✔ Delete product (Admin only)
✔ View all products
✔ Pagination support

🛒 Cart Management

✔ Add product to cart
✔ Update product quantity
✔ Remove product from cart
✔ View authenticated user’s cart
✔ Cart mapped securely to logged-in user

🏗 Tech Stack

☕ Java 17+
🌱 Spring Boot
🔐 Spring Security
🧾 JWT (JJWT)
🗄 Hibernate / JPA
🛢 MySQL
📦 Maven
🧪 Postman

📂 Project Structure

com.rohit.sb_ecom

📁 controller
📁 service
📁 repository
📁 model
📁 payload (DTOs)
📁 security
  📁 jwt
  📁 security_service
  📁 config
📁 exception

🔄 Authentication Flow

1️⃣ User registers
2️⃣ User logs in with username & password
3️⃣ AuthenticationManager validates credentials
4️⃣ JWT token is generated
5️⃣ Client sends JWT in Authorization header
6️⃣ JWT filter validates token
7️⃣ SecurityContext is set
8️⃣ Role-based authorization is enforced

🛡 Role-Based Access Example

👤 USER
• View products
• Add to cart
• View cart

👨‍💼 ADMIN
• Create product
• Update product
• Delete product
• Manage system inventory

⚙️ How to Run

1️⃣ Clone the repository
2️⃣ Configure MySQL database in application.properties
3️⃣ Run Maven clean install
4️⃣ Start the application
5️⃣ Access APIs at localhost:8080

🧠 Key Concepts Implemented

✔ Custom UserDetails implementation
✔ Custom AuthenticationEntryPoint
✔ JWT filter integration
✔ Many-to-Many relationship between User and Role
✔ EAGER role fetching
✔ DTO-based API responses
✔ Global exception handling
✔ Clean layered architecture

🚀 Future Enhancements

💳 Payment Integration
📦 Order Management Module
☁ AWS deployment
📄 Swagger API documentation

👨‍💻 Author

Rohit
Backend Developer | Spring Boot | Security | System Design Enthusiast