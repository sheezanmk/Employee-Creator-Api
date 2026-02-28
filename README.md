## Employee Creator API

A production-ready RESTful API built with Spring Boot for managing employees.
Supports full CRUD operations with validation, logging, testing, and structured error handling.

# Demo & Snippets

# Swagger UI:

http://localhost:8080/swagger-ui.html

# Health Endpoint:

GET /health

# Example response:

{
"id": 1,
"firstName": "John",
"lastName": "Smith",
"email": "john.smith@example.com",
"mobileNumber": "+61412345678",
"contractType": "PERMANENT",
"workType": "FULL_TIME",
"ongoing": true,
"startDate": "2024-01-01"
}

# Requirements / Purpose

This project demonstrates:

- Writing a production-ready REST API

- Applying business rules and validation

- Implementing layered architecture

- Structured exception handling

- Logging strategy with Log4j2

- Unit and controller testing

- Database integration with MySQL

The goal was to build a clean, maintainable backend suitable for real-world use and technical interviews.

## MVP

- The API supports:

- Create employee

- Get all employees

- Get employee by ID

- Update employee (PUT)

- Partial update (PATCH)

- Delete employee

- Business rules enforced:

- Email uniqueness

- Australian mobile number format (+61XXXXXXXXX)

- Employment rule validation:
  - finishDate must be null if ongoing = true

  - finishDate required if ongoing = false

  - PART_TIME requires hoursPerWeek

  - finishDate cannot be before startDate

# Stack Used

- Spring Boot 3.2.5
  - Stable LTS version compatible with Java 17.

- Spring Data JPA
  - Clean ORM abstraction for database operations.

- MySQL
  - Production-style relational database.

- H2 (Test Profile)
  - In-memory database for fast test execution.

- Log4j2
  - Structured logging strategy for production-readiness.

- JUnit 5 + Mockito
  - Unit and controller testing with mocking.

- SpringDoc OpenAPI
  - Automatic Swagger documentation.

## Build Steps

1. Clone the repository
   git clone <repo-url>
   cd employee-creator-api/backend

2. Configure environment variables

Create .env file:

DB_USERNAME=root
DB_PASSWORD=password123

Ensure MySQL database exists:

CREATE DATABASE employee_creator_api;

3. Run application
   mvn spring-boot:run

App runs at:

http://localhost:8080

4. Run tests
   mvn clean test

## Design Goals / Approach

- Clean Layered Architecture
  Controller → Service → Repository

- DTOs are used to:
  - Avoid exposing entities

  - Control request/response structure

  - Apply validation rules

- Mapper layer handles:
  - DTO ↔ Entity conversion

## Why this approach?

- Keeps business logic inside the Service layer

- Controllers remain thin

- Easier testing (mock service layer)

- Production-ready separation of concerns\_

## Features

- RESTful endpoint design

- Structured JSON error responses

- Global exception handling

- Business rule validation

- Email uniqueness constraint at DB + service layer

- PATCH support for partial updates

- Seed data on startup

- Swagger documentation

- Logging strategy with structured levels

- Unit tests

- Controller tests with MockMvc

## Logging Strategy

- INFO → business lifecycle events

- WARN → expected client errors (duplicate, not found)

- ERROR → unexpected failures (with stack trace)

# Log format includes:

- Timestamp

- Log level

- Class name

- Optional request correlation ID

## Known Issues

- No pagination implemented (GET returns all records)

- No authentication layer

- No Dockerized database setup yet

## Future Goals

If extended further:

- Pagination & sorting

- Docker Compose for MySQL

- Integration tests with RestAssured

- Request correlation ID filter

- Role-based access control

- CI/CD GitHub workflow badge

## Challenges faced

- Handling partial updates (PATCH) while preserving existing values

- Coordinating DTO validation with business rule validation

- Managing Spring Boot version conflicts (3.x vs 4.x)

- Ensuring test profile isolation from production DB

These were resolved by:

- Using dedicated Patch DTO

- Centralizing rule validation

- Fixing dependency version mismatches

- Using @ActiveProfiles("test")

## Related Projects

This backend is designed to work with a React + TypeScript frontend (to be implemented).
