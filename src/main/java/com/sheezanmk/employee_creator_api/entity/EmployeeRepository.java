package com.sheezanmk.employee_creator_api.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmailIgnoreCase(String email);
}
