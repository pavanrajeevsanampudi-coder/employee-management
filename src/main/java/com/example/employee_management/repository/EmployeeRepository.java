package com.example.employee_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.employee_management.model.Employee;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Search employees whose name contains given text
    List<Employee> findByNameContainingIgnoreCase(String name);

    // Search employees by exact department
    List<Employee> findByDepartmentIgnoreCase(String department);
    List<Employee> findBySalaryBetween(Double minSalary, Double maxSalary);
    Long countByDepartmentIgnoreCase(String department);

}
