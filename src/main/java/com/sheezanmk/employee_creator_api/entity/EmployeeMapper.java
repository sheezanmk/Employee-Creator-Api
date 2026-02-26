package com.sheezanmk.employee_creator_api.entity;

import com.sheezanmk.employee_creator_api.dtos.CreateEmployeeDto;
import com.sheezanmk.employee_creator_api.dtos.EmployeeResponseDto;
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

    public static Employee toEntity(UpdateEmployeeDto dto) {
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

}