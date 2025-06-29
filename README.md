# Trip Planner API

> Status: 🚧 Project under construction

A Spring Boot application designed to manage and design travel itineraries. This RESTful API facilitates trip planning by allowing users to create, read, update, and delete trip information.
Built with Java 17, Spring Boot, PostgreSQL, and Maven.

## 🚀 Features

- RESTful API for trip planning and management 
- PostgreSQL database integration 
- Spring Data JPA with Hibernate for ORM 
- Maven as the build system 
- Profile-based configuration for different environments

## 📌 Future enhancements and features

- ~~Integrate Swagger for API documentation~~
- Implement authentication and authorization
  - ~~implement Spring security to handle authentication and authorisation~~
  - for sign up workflow, integrate email validation
  - implement Two-Factor Authentication
- User story : User is able to manage activities for a trip
- User story : User is able to manage trip budget
- Add unit and integration tests - consider testing with @WebMvcTest + MockMvc (currently using @InjectMocks + Mockito)


## 🧰 Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Maven
- HikariCP connection pool

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

#### Database properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/<database-name>
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=update
```

#### Security properties
```properties
security.jwt.secret-key=
# access token expiration time: 15 min in millisecond
security.jwt.access-token.expiration=900000
# refresh token expiration time: 60 min in millisecond
security.jwt.refresh-token.expiration=3600000
```

#### Application properties
```properties
# allowed origin : domain that is explicitly permitted to access resources  in the context of Cross-Origin Resource Sharing (CORS)
trip-design-app.allowed-origin=

# fullname, email and password of boostraped SuperAdmin user when app starts
trip-design-app.super-admin.fullname=
trip-design-app.super-admin.email=
trip-design-app.super-admin.password=
```

#### Logging level
```
logging.level.com.lrasata.tripPlannerAPI=
```


### Run the application

With Maven Wrapper:

```bash
./mvnw spring-boot:run
```

#### Access Swagger UI
Open http://localhost:8080/swagger-ui/index.html in your browser to explore and test the API endpoints.

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

## 📁 Project Structure
- src/main/java: Contains the source code
  - controller: REST controllers 
  - service: Business logic 
  - repository: Data access layers 
  - model: Entity classes
- src/main/resources: Configuration files 
  - application.properties: Application configurations 
- pom.xml: Maven configuration file


## 📝 License

This project is licensed under the MIT License.
