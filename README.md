Pink Bags is an online e-commerce platform designed for selling premium bags, including purses, handbags, and backpacks. This project was developed using Java and Spring Boot as the backend framework, providing a scalable, modular, and secure architecture for modern web applications.

Tech Stack

Backend: Java (JDK 17+), Spring Boot

Database: JPA/Hibernate with SQL-based persistence (configurable for PostgreSQL, MySQL, or SQL Server)

Security: Spring Security with BCrypt password encoding for user authentication & authorization

Messaging & Logging: Apache Kafka integration (producer/consumer setup for handling order events, system monitoring, and real-time notifications)

Build & Dependency Management: Maven

Frontend (View Layer): Thymeleaf templates integrated with Spring MVC

Other Tools: Logback for logging, REST APIs for product and category management

Core Features

Product & Category Management

Supports three main categories: Purses, Handbags, and Backpacks.

RESTful CRUD APIs for managing products and categories.

User Authentication & Authorization

Secure login and signup using Spring Security.

Passwords stored securely with BCrypt hashing.

Role-based access control (Admin/User).

Order Management

Entity relationship for Orders, linked with products and users.

API support for placing, updating, and viewing orders.

Event-Driven Features with Apache Kafka

Producer/Consumer setup for logging order activities.

Kafka-based event streaming for real-time updates and monitoring.

Improved Logging System

Logback configuration for console and file-based logging.

Separate logs for producer, consumer, and application-level activities.

Enhanced Database Features

Entity classes for Products, Categories, Users, and Orders.

Relationships (One-to-Many, Many-to-One) modeled with JPA/Hibernate.

Scalability & Maintainability

Modular code structure separating configuration, entities, repositories, services, and controllers.

Support for future integration with external payment services.

✨ This Spring Boot version of Pink Bags moves beyond the earlier static HTML/Django experiment, making the system dynamic, secure, and event-driven with enterprise-grade capabilities.
