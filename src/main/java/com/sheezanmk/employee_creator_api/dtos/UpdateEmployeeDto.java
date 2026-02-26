package com.sheezanmk.employee_creator_api.dtos;

import java.time.LocalDate;

import com.sheezanmk.employee_creator_api.enums.ContractType;
import com.sheezanmk.employee_creator_api.enums.WorkType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateEmployeeDto {

    @NotBlank
    @Size(max = 50)
    private String firstName;

    // @Size(max = 50)
    // private String middleName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^\\+61\\d{9}$", message = "Mobile number must be in format +61XXXXXXXXX")
    private String mobileNumber;

    @Size(max = 255)
    private String address;

    @NotNull(message = "Contract type is required")
    private ContractType contractType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate finishDate;

    @NotNull(message = "Ongoing flag is required")
    private Boolean ongoing;

    @NotNull(message = "Work type is required")
    private WorkType workType;

    @Min(1)
    @Max(60)
    private Integer hoursPerWeek;

    public UpdateEmployeeDto() {
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

    public Boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(Boolean ongoing) {
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
