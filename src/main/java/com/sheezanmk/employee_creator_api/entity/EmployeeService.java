package com.sheezanmk.employee_creator_api.entity;

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sheezanmk.employee_creator_api.exceptions.BadRequestException;
import com.sheezanmk.employee_creator_api.exceptions.DuplicateResourceException;
import com.sheezanmk.employee_creator_api.exceptions.NotFoundException;

import org.springframework.stereotype.Service;

import com.sheezanmk.employee_creator_api.dtos.PatchEmployeeDto;
import com.sheezanmk.employee_creator_api.dtos.UpdateEmployeeDto;
import com.sheezanmk.employee_creator_api.enums.WorkType;

@Service
public class EmployeeService {

    private static final Logger log = LogManager.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        log.info("Fetched employees count={}", employees.size());
        return employees;
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found id={}", id);

                    return new NotFoundException("Employee not found");
                });
    }

    public Employee createEmployee(Employee employee) {

        log.info("Creating employee email={}", employee.getEmail());

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
            log.warn("Duplicate email attempted email={}", employee.getEmail());
            throw new DuplicateResourceException("Employee with this email already exists");
        }

        validateEmploymentRules(
                employee.isOngoing(),
                employee.getStartDate(),
                employee.getFinishDate(),
                employee.getWorkType(),
                employee.getHoursPerWeek());

        Employee saved = employeeRepository.save(employee);

        log.info("Employee created id={} email={}", saved.getId(), saved.getEmail());

        return saved;
    }

    public void deleteEmployee(Long id) {

        log.info("Deleting employee id={}", id);
        if (!employeeRepository.existsById(id)) {
            log.warn("Delete failed - employee not found id={}", id);
            throw new NotFoundException("Employee not found");
        }
        employeeRepository.deleteById(id);
        log.info("Employee deleted id={}", id);
    }

    public Employee updateEmployee(Long id, UpdateEmployeeDto dto) {

        log.info("Updating employee id={}", id);
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed - employee not found id={}", id);
                    return new NotFoundException("Employee not found");
                });

        String newEmail = dto.getEmail().trim().toLowerCase();

        if (!existing.getEmail().equalsIgnoreCase(newEmail)
                && employeeRepository.existsByEmailIgnoreCase(newEmail)) {
            log.warn("Update duplicate email attempted id={} email={}", id, newEmail);
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

        Employee saved = employeeRepository.save(existing);
        log.info("Employee updated id={} email={}", saved.getId(), saved.getEmail());
        return saved;
    }

    public Employee patchEmployee(Long id, PatchEmployeeDto dto) {

        log.info("Patching employee id={}", id);

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Patch failed - employee not found id={}", id);
                    return new NotFoundException("Employee not found");
                });

        if (dto.getEmail() != null) {
            String newEmail = dto.getEmail().trim().toLowerCase();
            if (!existing.getEmail().equalsIgnoreCase(newEmail)
                    && employeeRepository.existsByEmailIgnoreCase(newEmail)) {
                log.warn("Patch duplicate email attempted id={} email={}", id, newEmail);
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
            log.warn("Patch rule violation id={} - PART_TIME without hoursPerWeek", id);
            throw new BadRequestException("hoursPerWeek is required when setting workType to PART_TIME");
        }

        validateEmploymentRules(
                existing.isOngoing(),
                existing.getStartDate(),
                existing.getFinishDate(),
                existing.getWorkType(),
                existing.getHoursPerWeek());

        Employee saved = employeeRepository.save(existing);
        log.info("Employee patched id={} email={}", saved.getId(), saved.getEmail());
        return saved;
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
