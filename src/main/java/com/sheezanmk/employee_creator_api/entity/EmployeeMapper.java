package com.sheezanmk.employee_creator_api.entity;

import com.sheezanmk.employee_creator_api.dtos.CreateEmployeeDto;
import com.sheezanmk.employee_creator_api.dtos.EmployeeResponseDto;
import com.sheezanmk.employee_creator_api.dtos.PatchEmployeeDto;
import com.sheezanmk.employee_creator_api.dtos.UpdateEmployeeDto;

public class EmployeeMapper {
    private EmployeeMapper() {
    }

    public static EmployeeResponseDto toDto(Employee employee) {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        // dto.setMiddleName(employee.getMiddleName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setMobileNumber(employee.getMobileNumber());
        dto.setAddress(employee.getAddress());
        dto.setContractType(employee.getContractType());
        dto.setStartDate(employee.getStartDate());
        dto.setFinishDate(employee.getFinishDate());
        dto.setOngoing(employee.isOngoing());
        dto.setWorkType(employee.getWorkType());
        dto.setHoursPerWeek(employee.getHoursPerWeek());
        return dto;
    }

    public static Employee toEntity(CreateEmployeeDto dto) {
        Employee employee = new Employee();

        employee.setFirstName(dto.getFirstName());
        // employee.setMiddleName(dto.getMiddleName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setMobileNumber(dto.getMobileNumber());
        employee.setAddress(dto.getAddress());

        employee.setContractType(dto.getContractType());
        employee.setStartDate(dto.getStartDate());
        employee.setFinishDate(dto.getFinishDate());
        employee.setOngoing(Boolean.TRUE.equals(dto.getOngoing()));
        employee.setWorkType(dto.getWorkType());
        employee.setHoursPerWeek(dto.getHoursPerWeek());
        return employee;
    }

    public static void applyUpdate(Employee employee, UpdateEmployeeDto dto) {
        employee.setFirstName(dto.getFirstName());
        // employee.setMiddleName(dto.getMiddleName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setMobileNumber(dto.getMobileNumber());
        employee.setAddress(dto.getAddress());
        employee.setContractType(dto.getContractType());
        employee.setStartDate(dto.getStartDate());
        employee.setFinishDate(dto.getFinishDate());
        employee.setOngoing(Boolean.TRUE.equals(dto.getOngoing()));
        employee.setWorkType(dto.getWorkType());
        employee.setHoursPerWeek(dto.getHoursPerWeek());

    }

    public static void applyPatch(Employee employee, PatchEmployeeDto dto) {
        if (dto.getFirstName() != null)
            employee.setFirstName(dto.getFirstName());
        // if (dto.getMiddleName() != null)
        // employee.setMiddleName(dto.getMiddleName());
        if (dto.getLastName() != null)
            employee.setLastName(dto.getLastName());
        if (dto.getEmail() != null)
            employee.setEmail(dto.getEmail());
        if (dto.getMobileNumber() != null)
            employee.setMobileNumber(dto.getMobileNumber());
        if (dto.getAddress() != null)
            employee.setAddress(dto.getAddress());

        if (dto.getContractType() != null)
            employee.setContractType(dto.getContractType());
        if (dto.getStartDate() != null)
            employee.setStartDate(dto.getStartDate());
        if (dto.getFinishDate() != null)
            employee.setFinishDate(dto.getFinishDate());
        if (dto.getOngoing() != null)
            employee.setOngoing(dto.getOngoing());
        if (dto.getWorkType() != null)
            employee.setWorkType(dto.getWorkType());
        if (dto.getHoursPerWeek() != null)
            employee.setHoursPerWeek(dto.getHoursPerWeek());
    }

}