package com.sheezanmk.employee_creator_api.entity;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sheezanmk.employee_creator_api.dtos.PatchEmployeeDto;
import com.sheezanmk.employee_creator_api.dtos.UpdateEmployeeDto;

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

    public Employee updateEmployee(Long id, UpdateEmployeeDto dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String newEmail = dto.getEmail().trim().toLowerCase();

        if (!existing.getEmail().equalsIgnoreCase(newEmail)
                && employeeRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new RuntimeException("Employee with this email already exists");
        }

        dto.setFirstName(dto.getFirstName().trim());
        // if (dto.getMiddleName() != null)
        // dto.setMiddleName(dto.getMiddleName().trim());
        dto.setLastName(dto.getLastName().trim());
        dto.setEmail(newEmail);
        dto.setMobileNumber(dto.getMobileNumber().trim());
        if (dto.getAddress() != null)
            dto.setAddress(dto.getAddress().trim());
        EmployeeMapper.applyUpdate(existing, dto);

        return employeeRepository.save(existing);
    }

    public Employee patchEmployee(Long id, PatchEmployeeDto dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (dto.getEmail() != null) {
            String newEmail = dto.getEmail().trim().toLowerCase();
            if (!existing.getEmail().equalsIgnoreCase(newEmail)
                    && employeeRepository.existsByEmailIgnoreCase(newEmail)) {
                throw new RuntimeException("Employee with this email already exists");
            }
            dto.setEmail(newEmail);
        }

        if (dto.getFirstName() != null)
            dto.setFirstName(dto.getFirstName().trim());
        // if (dto.getMiddleName() != null)
        // dto.setMiddleName(dto.getMiddleName().trim());
        if (dto.getLastName() != null)
            dto.setLastName(dto.getLastName().trim());
        if (dto.getMobileNumber() != null)
            dto.setMobileNumber(dto.getMobileNumber().trim());
        if (dto.getAddress() != null)
            dto.setAddress(dto.getAddress().trim());

        EmployeeMapper.applyPatch(existing, dto);

        return employeeRepository.save(existing);
    }

}
