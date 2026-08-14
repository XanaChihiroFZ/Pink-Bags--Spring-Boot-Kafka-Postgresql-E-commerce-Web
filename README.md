# Pink Bags: Spring Boot + Kafka + PostgreSQL E-Commerce Web App

Pink Bags is a full-stack e-commerce web application built with **Spring Boot**, **Thymeleaf**, **PostgreSQL**, and **Apache Kafka**. It implements a classic online storefront, including product browsing, cart, checkout, user authentication, and order persistence, while demonstrating an **event-driven architecture** where order events are published to Kafka and consumed asynchronously by dedicated services for inventory updates, notifications, and analytics.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Libraries & Dependencies](#libraries--dependencies)
- [Skills Demonstrated](#skills-demonstrated)
- [Project Directory Structure](#project-directory-structure)
- [Folder & File Breakdown](#folder--file-breakdown)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Acknowledgement](#acknowledgement)

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.5.5
- **Web Layer:** Spring MVC + Thymeleaf (server-side rendering)
- **Persistence:** Spring Data JPA / Hibernate
- **Database:** PostgreSQL (primary), H2 (in-memory, for local testing)
- **Messaging / Event Streaming:** Apache Kafka (via Spring Kafka)
- **Security:** Spring Security 6
- **Build Tool:** Apache Maven (with Maven Wrapper)
- **PDF Generation:** iText (for invoices/receipts)
- **Frontend:** HTML5, CSS3, vanilla JavaScript (jQuery-style sliders, custom cart/chatbox scripts)

---

## Libraries & Dependencies

Pulled directly from `pom.xml`:

- `spring-boot-starter-web`, REST/MVC web layer and embedded Tomcat server
- `spring-boot-starter-data-jpa`, ORM/repository abstraction over Hibernate
- `spring-boot-starter-thymeleaf`, server-side HTML templating engine
- `spring-boot-starter-security`, authentication/authorization framework
- `spring-boot-starter-test`, JUnit-based testing support (test scope)
- `spring-kafka`, Kafka producer/consumer integration for Spring
- `thymeleaf-layout-dialect`, layout inheritance/composition for Thymeleaf templates
- `thymeleaf-extras-springsecurity6`, exposes Spring Security context (roles, auth state) inside Thymeleaf views
- `postgresql`, JDBC driver for PostgreSQL (runtime scope)
- `h2`, embedded in-memory database, used for local/testing convenience (runtime scope)
- `jackson-databind`, JSON serialization/deserialization (used for Kafka message payloads)
- `itextpdf` (v5.5.13.3), PDF generation library (e.g. order receipts/invoices)
- `lombok`, boilerplate reduction (getters/setters/constructors) via annotations (provided scope)
- `spring-boot-maven-plugin`, packages the app into an executable Spring Boot JAR

---

## Skills Demonstrated

- Designing a **layered Spring Boot architecture** (controller, service, repository, entity)
- Modeling a **relational e-commerce domain** (users, products, categories, orders, order items, shipping addresses) with JPA/Hibernate
- Implementing **event-driven communication** with Apache Kafka: a dedicated producer publishes order events, consumed independently by inventory, notification, and analytics services
- Configuring **Spring Security** for authentication, including a custom `UserDetailsService` and disabling Spring Boot's default auto-generated user
- Building **server-rendered UI** with Thymeleaf layouts, forms, and conditional rendering based on security context
- Generating **PDF documents** programmatically with iText (e.g. order confirmations)
- Using **DTOs** to decouple internal entities from Kafka message payloads and form submissions
- Seeding/loading demo data at startup via a custom `DataLoader`
- Structuring a Maven project with the Maven Wrapper for reproducible builds
- Writing basic **integration/unit test scaffolding** with `spring-boot-starter-test`
- Frontend integration: custom JS for interactive cart, image sliders, and a chat widget

---

## Project Directory Structure

```
pinkbags/
├── log.txt
├── meta_data.csv
└── pinkbags/
    ├── .gitattributes
    ├── .gitignore
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    ├── .mvn/
    │   └── wrapper/
    │       └── maven-wrapper.properties
    ├── logs/
    │   ├── analytics.log
    │   ├── consumer.log
    │   ├── notification.log
    │   └── producer.log
    ├── src/
    │   ├── main/
    │   │   ├── java/com/ecommerce/pinkbags/
    │   │   │   ├── PinkbagsApplication.java
    │   │   │   ├── config/
    │   │   │   │   ├── DataLoader.java
    │   │   │   │   ├── KafkaConsumerConfig.java
    │   │   │   │   ├── KafkaProducerConfig.java
    │   │   │   │   └── SecurityConfig.java
    │   │   │   ├── controller/
    │   │   │   │   └── HomeController.java
    │   │   │   ├── dto/
    │   │   │   │   └── CheckoutForm.java
    │   │   │   ├── entities/
    │   │   │   │   ├── Category.java
    │   │   │   │   ├── Customer.java
    │   │   │   │   ├── Order.java
    │   │   │   │   ├── OrderItem.java
    │   │   │   │   ├── Product.java
    │   │   │   │   ├── ShippingAddress.java
    │   │   │   │   └── User.java
    │   │   │   ├── kafka/
    │   │   │   │   ├── AnalyticsConsumer.java
    │   │   │   │   ├── InventoryConsumer.java
    │   │   │   │   ├── NotificationConsumer.java
    │   │   │   │   ├── OrderProducer.java
    │   │   │   │   ├── dto/
    │   │   │   │   │   ├── OrderDTO.java
    │   │   │   │   │   └── OrderItemDTO.java
    │   │   │   │   └── util/
    │   │   │   │       └── RandomOrderGenerator.java
    │   │   │   ├── repositories/
    │   │   │   │   ├── CategoryRepository.java
    │   │   │   │   ├── CustomerRepository.java
    │   │   │   │   ├── OrderItemRepository.java
    │   │   │   │   ├── OrderRepository.java
    │   │   │   │   ├── ProductRepository.java
    │   │   │   │   ├── ShippingAddressRepository.java
    │   │   │   │   └── UserRepository.java
    │   │   │   └── service/
    │   │   │       ├── CustomUserDetailsService.java
    │   │   │       └── UserService.java
    │   │   └── resources/
    │   │       ├── application.properties
    │   │       ├── static/
    │   │       │   ├── css/styles.css
    │   │       │   ├── images/ (banner, category, product, logo, UI images)
    │   │       │   └── js/
    │   │       │       ├── cart.js
    │   │       │       ├── chatbox.js
    │   │       │       ├── index.js
    │   │       │       ├── slider.js
    │   │       │       └── zoomsl.min.js
    │   │       └── templates/
    │   │           ├── about.html
    │   │           ├── cart.html
    │   │           ├── checkout.html
    │   │           ├── index.html
    │   │           ├── login.html
    │   │           ├── main.html
    │   │           ├── payment_confirmation.html
    │   │           ├── product.html
    │   │           ├── product2.html
    │   │           ├── product_details.html
    │   │           ├── signup.html
    │   │           ├── survey.html
    │   │           └── terms.html
    │   └── test/
    │       └── java/com/ecommerce/pinkbags/
    │           └── PinkbagsApplicationTests.java
```

---

## Folder & File Breakdown

### Root-level (`pinkbags/`)

| Path | Purpose |
|---|---|
| `log.txt` | Ad-hoc/general application log output captured during development or a run. |
| `meta_data.csv` | CSV dataset, likely used as source data for seeding products/categories via `DataLoader`. |

### Project root (`pinkbags/pinkbags/`)

| Path | Purpose |
|---|---|
| `.gitattributes` | Git configuration for line-ending/text handling rules across the repo. |
| `.gitignore` | Specifies files/folders (build output, IDE files, etc.) excluded from version control. |
| `mvnw` / `mvnw.cmd` | Maven Wrapper scripts (Unix/Windows) so the project can be built without a pre-installed Maven. |
| `pom.xml` | Maven build file, declares the Spring Boot parent, Java 17 target, and all project dependencies. |
| `.mvn/wrapper/maven-wrapper.properties` | Configuration for the Maven Wrapper (specifies the Maven distribution version to download/use). |

### `logs/`

| Path | Purpose |
|---|---|
| `producer.log` | Log output specific to the Kafka order producer. |
| `consumer.log` | Log output specific to Kafka consumer activity. |
| `analytics.log` | Log output from the analytics consumer service. |
| `notification.log` | Log output from the notification consumer service. |

### `src/main/java/com/ecommerce/pinkbags/`

| File | Purpose |
|---|---|
| `PinkbagsApplication.java` | Main entry point, bootstraps the Spring Boot application (`@SpringBootApplication`, `main()` method). |

#### `config/`

| File | Purpose |
|---|---|
| `DataLoader.java` | Runs at startup to seed the database with demo data (likely products/categories, possibly sourced from `meta_data.csv`). |
| `KafkaProducerConfig.java` | Configures the Kafka `ProducerFactory` / `KafkaTemplate` beans (serializers, broker address, producer settings). |
| `KafkaConsumerConfig.java` | Configures the Kafka `ConsumerFactory` / listener container beans (deserializers, group IDs, concurrency). |
| `SecurityConfig.java` | Spring Security configuration, defines the security filter chain, protected routes, login handling, and password encoding. |

#### `controller/`

| File | Purpose |
|---|---|
| `HomeController.java` | Handles top-level web routes, likely serves the homepage, product listing/detail pages, cart, checkout, and static content pages (about, terms, survey). |

#### `dto/`

| File | Purpose |
|---|---|
| `CheckoutForm.java` | Data Transfer Object bound to the checkout form, capturing customer/shipping/payment input from the UI. |

#### `entities/`

| File | Purpose |
|---|---|
| `User.java` | JPA entity representing an authenticated application user (credentials, roles). |
| `Customer.java` | JPA entity representing customer profile/contact information. |
| `Category.java` | JPA entity representing a product category for catalog organization. |
| `Product.java` | JPA entity representing a sellable item (name, price, image, category, etc.). |
| `Order.java` | JPA entity representing a placed order (status, customer, totals, timestamps). |
| `OrderItem.java` | JPA entity representing a single line item within an order (product plus quantity plus price). |
| `ShippingAddress.java` | JPA entity representing a delivery address associated with an order/customer. |

#### `kafka/`

| File | Purpose |
|---|---|
| `OrderProducer.java` | Publishes order-related events to a Kafka topic whenever an order is placed. |
| `InventoryConsumer.java` | Consumes order events to update/decrement product inventory levels. |
| `NotificationConsumer.java` | Consumes order events to trigger customer-facing notifications (e.g. order confirmation). |
| `AnalyticsConsumer.java` | Consumes order events for analytics/reporting purposes (e.g. logging sales metrics). |
| `dto/OrderDTO.java` | Serializable representation of an order used as the Kafka message payload. |
| `dto/OrderItemDTO.java` | Serializable representation of an order line item, nested within `OrderDTO`. |
| `util/RandomOrderGenerator.java` | Utility for generating randomized/simulated order data, useful for demoing or load-testing the Kafka pipeline. |

#### `repositories/`

| File | Purpose |
|---|---|
| `UserRepository.java` | Spring Data JPA repository for `User` CRUD operations. |
| `CustomerRepository.java` | Spring Data JPA repository for `Customer` CRUD operations. |
| `CategoryRepository.java` | Spring Data JPA repository for `Category` CRUD operations. |
| `ProductRepository.java` | Spring Data JPA repository for `Product` CRUD operations. |
| `OrderRepository.java` | Spring Data JPA repository for `Order` CRUD operations. |
| `OrderItemRepository.java` | Spring Data JPA repository for `OrderItem` CRUD operations. |
| `ShippingAddressRepository.java` | Spring Data JPA repository for `ShippingAddress` CRUD operations. |

#### `service/`

| File | Purpose |
|---|---|
| `UserService.java` | Business logic layer for user-related operations (registration, lookups, etc.). |
| `CustomUserDetailsService.java` | Implements Spring Security's `UserDetailsService` to load user data for authentication. |

### `src/main/resources/`

| Path | Purpose |
|---|---|
| `application.properties` | Central configuration file: PostgreSQL datasource, JPA/Hibernate settings, Thymeleaf settings, Spring Security overrides, and (commented-out) Kafka producer/consumer configuration. |
| `static/css/styles.css` | Global stylesheet for the storefront UI. |
| `static/images/` | Product photos, category thumbnails, banners, and logo assets used across the site. |
| `static/js/cart.js` | Client-side logic for shopping cart interactions. |
| `static/js/chatbox.js` | Client-side logic for a chat/support widget. |
| `static/js/index.js` | Client-side logic for the homepage. |
| `static/js/slider.js` | Client-side logic for image/banner sliders. |
| `static/js/zoomsl.min.js` | Third-party minified library for image zoom functionality. |

#### `templates/` (Thymeleaf views)

| File | Purpose |
|---|---|
| `main.html` | Base/shared layout template (header, footer, nav) consumed by other pages via layout dialect. |
| `index.html` | Storefront homepage. |
| `product.html` / `product2.html` | Product listing page(s) / catalog views. |
| `product_details.html` | Individual product detail page. |
| `cart.html` | Shopping cart view. |
| `checkout.html` | Checkout page, bound to `CheckoutForm`. |
| `payment_confirmation.html` | Order/payment confirmation page shown after checkout. |
| `login.html` | User login page. |
| `signup.html` | User registration page. |
| `about.html` | Static "About Us" page. |
| `terms.html` | Static Terms & Conditions page. |
| `survey.html` | Customer survey/feedback page. |

### `src/test/java/com/ecommerce/pinkbags/`

| File | Purpose |
|---|---|
| `PinkbagsApplicationTests.java` | Default Spring Boot smoke test verifying the application context loads successfully. |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven (or use the included `mvnw` / `mvnw.cmd` wrapper)
- PostgreSQL running locally with a `pinkbags` database
- (Optional) A running Kafka broker if enabling the messaging features

### Run locally

```bash
# clone the repo
git clone <repo-url>
cd pinkbags/pinkbags

# build and run
./mvnw spring-boot:run      # macOS/Linux
mvnw.cmd spring-boot:run    # Windows
```

The app will start on the default Spring Boot port (`8080`) and connect to PostgreSQL using the credentials in `application.properties`.

---

## Configuration

Key settings live in `src/main/resources/application.properties`:

- **Database:** connects to a local PostgreSQL instance (`jdbc:postgresql://localhost:5432/pinkbags`); `spring.jpa.hibernate.ddl-auto=update` auto-syncs the schema from entities.
- **Security:** disables Spring Boot's auto-generated default user so authentication relies entirely on `CustomUserDetailsService` and the `users` table.
- **Thymeleaf:** caching disabled for easier local development.
- **Kafka:** producer/consumer bootstrap settings are present but commented out by default; uncomment and point `spring.kafka.bootstrap-servers` at a running broker to enable the event-driven order pipeline.

---

## Acknowledgement

This project, **Pink Bags**, is my own original work, built to practice and demonstrate full-stack development with Spring Boot, PostgreSQL, and event-driven architecture using Apache Kafka.
