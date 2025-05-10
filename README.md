# PPJ 2025

A Java-based RESTful API for managing meteorological data, including countries, cities, and weather measurements.

## Features

- CRUD operations for countries, cities, and measurements
- Aggregation of measurement data per city
- Integration with MongoDB for data storage
- Swagger/OpenAPI documentation for API endpoints

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- MongoDB
- Lombok
- Jakarta Persistence API
- Swagger/OpenAPI

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- MongoDB

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/BelinaJaroslav/ppj2025.git

    Navigate to the project directory:


2. Build the project using Maven:

   ```bash
   mvn clean install

3. Run the application:
   ```bash
   mvn spring-boot:run

### Configuration

Configure the application properties in src/main/resources/application.properties to connect to your MySQL instance and set other configuration values as needed.
API Documentation

Once the application is running, access the Swagger UI for interactive API documentation at:
   ```bash
http://localhost:8080/swagger-ui.html

