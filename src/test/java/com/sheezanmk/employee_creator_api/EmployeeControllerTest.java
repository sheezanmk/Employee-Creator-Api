package com.sheezanmk.employee_creator_api;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sheezanmk.employee_creator_api.dtos.PatchEmployeeDto;
import com.sheezanmk.employee_creator_api.entity.Employee;
import com.sheezanmk.employee_creator_api.entity.EmployeeController;
import com.sheezanmk.employee_creator_api.entity.EmployeeService;
import com.sheezanmk.employee_creator_api.enums.ContractType;
import com.sheezanmk.employee_creator_api.enums.WorkType;
import com.sheezanmk.employee_creator_api.exceptions.BadRequestException;
import com.sheezanmk.employee_creator_api.exceptions.DuplicateResourceException;
import com.sheezanmk.employee_creator_api.exceptions.GlobalExceptionHandler;
import com.sheezanmk.employee_creator_api.exceptions.NotFoundException;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.time.LocalDate;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)

public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getEmployeeById_returns404_whenNotFound() throws Exception {
        Long id = 999L;

        when(employeeService.getEmployeeById(id))
                .thenThrow(new NotFoundException("Employee not found"));

        mockMvc.perform(get("/api/employee/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Employee not found"))
                .andExpect(jsonPath("$.path").value("/api/employee/" + id));
    }

    @Test
    void createEmployee_returns201_andEmployeeResponse() throws Exception {

        String payload = """
                {
                  "firstName": "Alice",
                  "lastName": "Smith",
                  "email": "alice@example.com",
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

        Employee saved = new Employee();
        saved.setId(1L);
        saved.setFirstName("Alice");
        saved.setLastName("Smith");
        saved.setEmail("alice@example.com");
        saved.setMobileNumber("+61412345678");
        saved.setAddress("Sydney");
        saved.setContractType(ContractType.PERMANENT);
        saved.setStartDate(LocalDate.of(2026, 2, 1));
        saved.setFinishDate(null);
        saved.setOngoing(true);
        saved.setWorkType(WorkType.FULL_TIME);
        saved.setHoursPerWeek(38);

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.mobileNumber").value("+61412345678"))
                .andExpect(jsonPath("$.contractType").value("PERMANENT"))
                .andExpect(jsonPath("$.workType").value("FULL_TIME"))
                .andExpect(jsonPath("$.hoursPerWeek").value(38));
    }

    @Test
    void createEmployee_returns400_whenEmailIsInvalid() throws Exception {
        String payload = """
                {
                  "firstName": "Alice",
                  "lastName": "Smith",
                  "email": "not-an-email",
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

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void createEmployee_returns409_whenEmailAlreadyExists() throws Exception {
        String payload = """
                {
                  "firstName": "Alice",
                  "lastName": "Smith",
                  "email": "alice@example.com",
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

        when(employeeService.createEmployee(any(Employee.class)))
                .thenThrow(new DuplicateResourceException("Employee with this email already exists"));

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Employee with this email already exists"))
                .andExpect(jsonPath("$.path").value("/api/employee"));
    }

    @Test
    void patchEmployee_returns400_whenBusinessRuleFails() throws Exception {
        Long id = 1L;

        String payload = """
                {
                  "workType": "PART_TIME"
                }
                """;

        when(employeeService.patchEmployee(eq(id), any(PatchEmployeeDto.class)))
                .thenThrow(new BadRequestException("hoursPerWeek is required when workType is PART_TIME"));

        mockMvc.perform(patch("/api/employee/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("hoursPerWeek is required when workType is PART_TIME"))
                .andExpect(jsonPath("$.path").value("/api/employee/" + id));
    }

}
