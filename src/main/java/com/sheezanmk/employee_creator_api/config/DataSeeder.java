package com.sheezanmk.employee_creator_api.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.sheezanmk.employee_creator_api.entity.Employee;
import com.sheezanmk.employee_creator_api.entity.EmployeeRepository;
import com.sheezanmk.employee_creator_api.enums.ContractType;
import com.sheezanmk.employee_creator_api.enums.WorkType;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public DataSeeder(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {

        if (employeeRepository.count() > 0) {
            return;
        }

        Employee e1 = new Employee();
        e1.setFirstName("Alice");
        e1.setLastName("Smith");
        e1.setEmail("alice@example.com");
        e1.setMobileNumber("+61412345678");
        e1.setAddress("Sydney");
        e1.setContractType(ContractType.PERMANENT);
        e1.setStartDate(LocalDate.of(2024, 1, 10));
        e1.setFinishDate(null);
        e1.setOngoing(true);
        e1.setWorkType(WorkType.FULL_TIME);
        e1.setHoursPerWeek(38);

        Employee e2 = new Employee();
        e2.setFirstName("Bob");
        e2.setLastName("Johnson");
        e2.setEmail("bob@example.com");
        e2.setMobileNumber("+61498765432");
        e2.setAddress("Melbourne");
        e2.setContractType(ContractType.CONTRACT);
        e2.setStartDate(LocalDate.of(2023, 6, 1));
        e2.setFinishDate(LocalDate.of(2025, 6, 1));
        e2.setOngoing(false);
        e2.setWorkType(WorkType.PART_TIME);
        e2.setHoursPerWeek(20);

        employeeRepository.save(e1);
        employeeRepository.save(e2);
    }
}
