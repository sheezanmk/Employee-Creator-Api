package com.sheezanmk.employee_creator_api.entity;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Employee createEmployee(Employee employee) {

        employee.setFirstName(employee.getFirstName().trim());
        // if (employee.getMiddleName() != null) {
        // employee.setMiddleName(employee.getMiddleName().trim());
        // }
        employee.setLastName(employee.getLastName().trim());
        employee.setEmail(employee.getEmail().trim().toLowerCase());
        employee.setMobileNumber(employee.getMobileNumber().trim());
        if (employee.getAddress() != null) {
            employee.setAddress(employee.getAddress().trim());
        }

        if (employeeRepository.existsByEmailIgnoreCase(employee.getEmail())) {
            throw new RuntimeException("Employee with this email already exists");
        }

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    public Employee updateEmployee(Long id, Employee updatedData) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        updatedData.setFirstName(updatedData.getFirstName().trim());
        // if (updatedData.getMiddleName() != null)
        // updatedData.setMiddleName(updatedData.getMiddleName().trim());
        updatedData.setLastName(updatedData.getLastName().trim());
        updatedData.setEmail(updatedData.getEmail().trim().toLowerCase());
        updatedData.setMobileNumber(updatedData.getMobileNumber().trim());
        if (updatedData.getAddress() != null)
            updatedData.setAddress(updatedData.getAddress().trim());

        String newEmail = updatedData.getEmail();
        if (!existing.getEmail().equalsIgnoreCase(newEmail)
                && employeeRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new RuntimeException("Employee with this email already exists");
        }

        existing.setFirstName(updatedData.getFirstName());
        // existing.setMiddleName(updatedData.getMiddleName());
        existing.setLastName(updatedData.getLastName());
        existing.setEmail(updatedData.getEmail());
        existing.setMobileNumber(updatedData.getMobileNumber());
        existing.setAddress(updatedData.getAddress());

        existing.setContractType(updatedData.getContractType());
        existing.setStartDate(updatedData.getStartDate());
        existing.setFinishDate(updatedData.getFinishDate());
        existing.setOngoing(updatedData.isOngoing());
        existing.setWorkType(updatedData.getWorkType());
        existing.setHoursPerWeek(updatedData.getHoursPerWeek());

        return employeeRepository.save(existing);

    }

}
