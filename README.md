# Trip Planner API

> Status: 🚧 Project under construction

A Spring Boot application designed to manage and design travel itineraries. This RESTful API facilitates trip planning by allowing users to create, read, update, and delete trip information.
Built with Java 17, Spring Boot, PostgreSQL, and Maven.

---

## 🚀 Features

- RESTful API for trip planning and management 
- PostgreSQL database integration 
- Spring Data JPA with Hibernate for ORM 
- Maven as the build system 
- Profile-based configuration for different environments
---

## 🧰 Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Maven
- HikariCP connection pool

---

## 🏗️ Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL running locally on port `5432` (default)

### Clone the repository

```bash
git clone https://github.com/lrasata/tripPlannerAPI.git
cd tripPlannerAPI
```

### Configure application

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/<database-name>
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=update
```

### Run the application

With Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Or with IntelliJ (once properly configured).

### Format code

```bash
mvn spotless:apply    # Formats code
mvn spotless:check    # Fails build if not formatted
```

### Build JAR

```bash
./mvnw clean package
java -jar target/tripPlannerAPI-0.0.1-SNAPSHOT.jar
```
---
## 🧪 Testing

```bash
./mvnw test
```

---

## 📁 Project Structure
- src/main/java: Contains the source code
  - controller: REST controllers 
  - service: Business logic 
  - repository: Data access layers 
  - model: Entity classes
- src/main/resources: Configuration files 
  - application.properties: Application configurations 
- pom.xml: Maven configuration file

---
## 📌 Future Enhancements
- Implement authentication and authorization
- Add unit and integration tests
- Integrate Swagger for API documentation
---


## 📝 License

This project is licensed under the MIT License.
