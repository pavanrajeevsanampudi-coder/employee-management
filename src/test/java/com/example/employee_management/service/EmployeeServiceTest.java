package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.dto.EmployeeResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void testCreateEmployee() {
        // Use DTO instead of entity
        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("rajeev reddy");
        dto.setEmail("john4@example.com");
        dto.setDepartment("HRes");
        dto.setSalary(5000);

        EmployeeResponseDTO saved = employeeService.saveEmployee(dto);

        assertNotNull(saved.getId(), "Employee ID should not be null after saving");
    }
}
