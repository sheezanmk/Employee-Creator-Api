package com.sheezanmk.employee_creator_api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EmployeeApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void createEmployee_happyPath_returns201() {

        String payload = """
                {
                  "firstName": "Integration",
                  "lastName": "Test",
                  "email": "integration@test.com",
                  "mobileNumber": "+61412345678",
                  "address": "Sydney",
                  "contractType": "PERMANENT",
                  "startDate": "2026-02-01",
                  "finishDate": null,
                  "ongoing": true,
                  "workType": "FULL_TIME",
                  "hoursPerWeek": 38
                }
                """;

        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/employee")
                .then()
                .statusCode(201)
                .body("firstName", equalTo("Integration"))
                .body("lastName", equalTo("Test"))
                .body("email", equalTo("integration@test.com"))
                .body("workType", equalTo("FULL_TIME"));
    }
}