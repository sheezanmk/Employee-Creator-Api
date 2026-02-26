package com.sheezanmk.employee_creator_api.dtos;

import java.time.LocalDate;

import com.sheezanmk.employee_creator_api.enums.ContractType;
import com.sheezanmk.employee_creator_api.enums.WorkType;

public class EmployeeResponseDto {
    private Long id;
    private String firstName;
    // private String middleName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String address;
    private ContractType contractType;
    private LocalDate startDate;
    private LocalDate finishDate;
    private boolean ongoing;
    private WorkType workType;
    private Integer hoursPerWeek;

    public EmployeeResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // public String getMiddleName() {
    // return middleName;
    // }

    // public void setMiddleName(String middleName) {
    // this.middleName = middleName;
    // }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public WorkType getWorkType() {
        return workType;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public Integer getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(Integer hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

}
