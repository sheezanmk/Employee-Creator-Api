package com.sheezanmk.employee_creator_api.entity;

import java.time.LocalDate;
import java.util.List;

import com.sheezanmk.employee_creator_api.exceptions.BadRequestException;
import com.sheezanmk.employee_creator_api.exceptions.DuplicateResourceException;
import com.sheezanmk.employee_creator_api.exceptions.NotFoundException;

import org.springframework.stereotype.Service;

import com.sheezanmk.employee_creator_api.dtos.PatchEmployeeDto;
import com.sheezanmk.employee_creator_api.dtos.UpdateEmployeeDto;
import com.sheezanmk.employee_creator_api.enums.WorkType;

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
                .orElseThrow(
                        () -> new NotFoundException("Employee not found"));
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
            throw new DuplicateResourceException("Employee with this email already exists");
        }

        validateEmploymentRules(
                employee.isOngoing(),
                employee.getStartDate(),
                employee.getFinishDate(),
                employee.getWorkType(),
                employee.getHoursPerWeek());

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new NotFoundException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    public Employee updateEmployee(Long id, UpdateEmployeeDto dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        String newEmail = dto.getEmail().trim().toLowerCase();

        if (!existing.getEmail().equalsIgnoreCase(newEmail)
                && employeeRepository.existsByEmailIgnoreCase(newEmail)) {
            throw new DuplicateResourceException("Employee with this email already exists");
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

        validateEmploymentRules(
                existing.isOngoing(),
                existing.getStartDate(),
                existing.getFinishDate(),
                existing.getWorkType(),
                existing.getHoursPerWeek());

        return employeeRepository.save(existing);
    }

    public Employee patchEmployee(Long id, PatchEmployeeDto dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (dto.getEmail() != null) {
            String newEmail = dto.getEmail().trim().toLowerCase();
            if (!existing.getEmail().equalsIgnoreCase(newEmail)
                    && employeeRepository.existsByEmailIgnoreCase(newEmail)) {
                throw new DuplicateResourceException("Employee with this email already exists");
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

        if (dto.getOngoing() != null && dto.getOngoing()) {
            existing.setFinishDate(null);
        }

        if (dto.getWorkType() != null && dto.getWorkType() == WorkType.PART_TIME && dto.getHoursPerWeek() == null) {
            throw new BadRequestException("hoursPerWeek is required when setting workType to PART_TIME");
        }

        validateEmploymentRules(
                existing.isOngoing(),
                existing.getStartDate(),
                existing.getFinishDate(),
                existing.getWorkType(),
                existing.getHoursPerWeek());

        return employeeRepository.save(existing);
    }

    private void validateEmploymentRules(
            Boolean ongoing,
            LocalDate startDate,
            LocalDate finishDate,
            WorkType workType,
            Integer hoursPerWeek) {

        if (ongoing != null) {
            if (Boolean.TRUE.equals(ongoing)) {
                if (finishDate != null) {
                    throw new BadRequestException("finishDate must be null when ongoing is true");
                }
            } else {
                if (finishDate == null) {
                    throw new BadRequestException("finishDate is required when ongoing is false");
                }
            }
        }

        if (startDate != null && finishDate != null) {
            if (finishDate.isBefore(startDate)) {
                throw new BadRequestException("finishDate cannot be before startDate");
            }
        }

        if (workType != null) {
            if (workType == WorkType.PART_TIME) {
                if (hoursPerWeek == null) {
                    throw new BadRequestException("hoursPerWeek is required for PART_TIME employees");
                }
            }
        }

    }
}
