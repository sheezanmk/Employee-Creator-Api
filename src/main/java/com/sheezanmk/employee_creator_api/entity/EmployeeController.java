package com.sheezanmk.employee_creator_api.entity;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sheezanmk.employee_creator_api.dtos.CreateEmployeeDto;
import com.sheezanmk.employee_creator_api.dtos.EmployeeResponseDto;
import com.sheezanmk.employee_creator_api.dtos.UpdateEmployeeDto;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeResponseDto> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return employees.stream().map(EmployeeMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public EmployeeResponseDto getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return EmployeeMapper.toDto(employee);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody @Valid CreateEmployeeDto body) {
        Employee employee = EmployeeMapper.toEntity(body);
        Employee created = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeMapper.toDto(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public EmployeeResponseDto updateEmployee(@PathVariable Long id, @RequestBody @Valid UpdateEmployeeDto body) {
        {
            Employee saved = employeeService.updateEmployee(id, body);
            return EmployeeMapper.toDto(saved);
        }

    }

}
