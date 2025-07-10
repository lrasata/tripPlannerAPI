# Trip Planner API 🚧

[![CI](https://github.com/lrasata/tripPlannerAPI/actions/workflows/springboot-ci.yml/badge.svg)](https://github.com/lrasata/tripPlannerAPI/actions/workflows/ci.yml)


> Status: 🚧 Project under construction

Trip Planner is a web app built for organizing travel plans such as flights, stays, activities, notes,
and maps. It supports real-time collaboration, budget tracking, and personal touches like photos.

This Spring Boot application serves as the backend counterpart to the [Trip Planner Web App](https://github.com/lrasata/trip-planner-web-app). 
This RESTful API facilitates trip planning by allowing users to create, read, update, and delete trips.

## Purpose
*The Story Behind Trip Planner*

I love to travel and even more than that, I love organizing travel. For me, it feels like a MUST, because I  want to
make the best out of a trip, I have got some homework to do. Planning gives me almost as much excitement as the trip
itself. But until recently, planning a trip meant writing things down all over my computer : in notes apps, random
document, my calendar and my email box.

I always knew there has to be a better way but when I tried different travel apps, but none of them felt right.
They were either too complicated, too limited, or just not made for the way I think.

So I decided to build my own. Something clean, flexible, and genuinely useful.

## Features

- Itinerary builder :bulb:
- Budget tracking :bulb:
- Collaborative planning (comments, task assignments)
- Personalization (cover photo, trip notes)
- Map and timeline views
- Modular architecture for adding future features easily

## Dev perspective
Trip Planner is not only a practical tool. Since there are countless features that could be added from weather integration,
AI-powered suggestions, local event feeds, to journals, all that makes this personal project exciting and expandable for full-stack experimentation.

## Tech Stack
- **Frontend**: React, TypeScript, Emotion CSS, MUI
- **Backend**: Java Spring Boot (REST API)
  - Java 17
  - Spring Boot 3.x
  - Spring Data JPA
  - PostgreSQL
  - Maven
  - HikariCP connection pool

## Why Java and Spring Framework for the Backend

I chose Java Spring Framework for the backend of Trip Planner because it offers a solid balance of robust architecture, 
scalability, and mature tooling which is ideal for building production-grade APIs.

*Stability & Performance:* Spring Boot is heavily used in enterprise environments. It’s fast, stable, and highly 
optimized for building RESTful services that scale well as the app grows.

*Strong Typing & Structure:* Coming from a TypeScript frontend, using Java gives me strong typing on the backend too. 
It helps maintain a clear contract between client and server and reduces bugs.

*Built-in Features:* Spring Boot handles things such as dependency injection, security, validation, and configuration 
cleanly out of the box, which makes development faster and more maintainable.

*REST API:* It’s particularly well-suited for creating a clean and well-documented REST API, which is the backbone of 
this project. Combined with tools like Swagger or Spring Data, it speeds up backend development without losing 
flexibility.

---

## Getting started

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

Edit `.env`, each env variable corresponds to the following app properties:
````text
# .env

SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
JWT_SECRET_KEY=
ALLOWED_ORIGIN=
COOKIE_SECURE_ATTRIBUTE=
COOKIE_SAME_SITE=
SUPER_ADMIN_FULLNAME=
SUPER_ADMIN_EMAIL=
SUPER_ADMIN_PASSWORD=
````

#### Database properties
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

#### Security properties
```properties
security.jwt.secret-key=${JWT_SECRET_KEY}
# access token expiration time: 15 min in millisecond
security.jwt.access-token.expiration=900000
# refresh token expiration time: 60 min in millisecond
security.jwt.refresh-token.expiration=3600000
```

#### Application properties
```properties
# allowed origin : domain that is explicitly permitted to access resources  in the context of Cross-Origin Resource Sharing (CORS)
trip-design-app.allowed-origin=${ALLOWED_ORIGIN}
# trip-design-app.cookie.secure-attribute should be true in production
trip-design-app.cookie.secure-attribute=${COOKIE_SECURE_ATTRIBUTE}
# trip-design-app.cookie.same-site should be true if cross origin + use credentials
trip-design-app.cookie.same-site=${COOKIE_SAME_SITE}
# fullname, email and password of bootsraped SuperAdmin user when app starts
trip-design-app.super-admin.fullname=${SUPER_ADMIN_FULLNAME}
trip-design-app.super-admin.email=${SUPER_ADMIN_EMAIL}
trip-design-app.super-admin.password=${SUPER_ADMIN_PASSWORD}
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

### Testing

```bash
./mvnw test
```

---

## Docker

You can containerize the Trip Planner backend app using Docker and manage multi-container setups with Docker Compose.

### Using Docker Compose

```bash
docker compose up --build
```

This will build and start services in `docker-compose.yml` and `docker-compose.override.yml` . In this file, the web app is set up to be accessible on port `8080` : `http://localhost:8080/`

Make sure Docker is installed and running on your system.

---

## License

This project is licensed under the MIT License.
