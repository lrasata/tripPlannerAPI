# Trip Design App - Restful web API
> Project under construction

A Spring Boot application to manage and design travel itineraries.  
Built with Java 17, Spring Boot, PostgreSQL, and Maven.

---

## 🚀 Features

- RESTful API for trip planning and management
- PostgreSQL database integration
- JPA with Hibernate
- Maven build system
- Profile-based configuration

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
git clone https://github.com/your-username/trip-design-app.git
cd trip-design-app
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
java -jar target/tripDesignApp-0.0.1-SNAPSHOT.jar
```

---

## 🔌 API Endpoints

Example (update with your real endpoints):

| Method | Endpoint          | Description        |
|--------|-------------------|--------------------|
| GET    | `/api/trips`      | List all trips     |
| POST   | `/api/trips`      | Create a new trip  |
| GET    | `/api/trips/{id}` | Get trip by ID     |
| PUT    | `/api/trips/{id}` | Update trip        |
| DELETE | `/api/trips/{id}` | Delete trip        |

---

## 🧪 Testing

```bash
./mvnw test
```

---

## 📝 License

This project is licensed under the MIT License.
