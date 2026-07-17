package com.example.employee_management.service;

import com.example.employee_management.model.Employee;
import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.dto.EmployeeResponseDTO;
import com.example.employee_management.repository.EmployeeRepository;
import com.example.employee_management.exception.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    // Save employee using DTO
    public EmployeeResponseDTO saveEmployee(EmployeeDTO dto) {
        logger.info("Saving new employee with email: {}", dto.getEmail());
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());

        Employee savedEmployee = employeeRepository.save(employee);
        return convertToResponseDTO(savedEmployee);
    }

    // Get all employees (no pagination)
    public List<EmployeeResponseDTO> getAllEmployees() {
        logger.info("Fetching all employees");
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get employees with pagination + sorting
    public Page<EmployeeResponseDTO> getEmployeesPaged(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching employees with pagination: page={}, size={}, sortBy={}, sortDir={}", 
                    page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                    : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return employeeRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    // Get employee by ID
    public EmployeeResponseDTO getEmployeeById(Long id) {
        logger.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return convertToResponseDTO(employee);
    }

    // Update employee using DTO
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeDTO dto) {
        logger.info("Updating employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());

        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToResponseDTO(updatedEmployee);
    }

    // Delete employee
    public void deleteEmployee(Long id) {
        logger.info("Deleting employee with id: {}", id);
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    // 🔹 New: Search employees by name
    public List<EmployeeResponseDTO> searchEmployeesByName(String name) {
        logger.info("Searching employees by name containing: {}", name);
        return employeeRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 🔹 New: Search employees by department
    public List<EmployeeResponseDTO> searchEmployeesByDepartment(String department) {
        logger.info("Searching employees by department: {}", department);
        return employeeRepository.findByDepartmentIgnoreCase(department)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    public List<EmployeeResponseDTO> searchEmployeesBySalaryRange(Double min, Double max) {
        logger.info("Searching employees with salary between {} and {}", min, max);
        return employeeRepository.findBySalaryBetween(min, max)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    public Long countEmployeesByDepartment(String department) {
        logger.info("Counting employees in department: {}", department);
        return employeeRepository.countByDepartmentIgnoreCase(department);
    }


    // Helper method to convert entity to DTO
    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setEmail(employee.getEmail());
        response.setDepartment(employee.getDepartment());
        response.setSalary(employee.getSalary());
        return response;
    }
}
