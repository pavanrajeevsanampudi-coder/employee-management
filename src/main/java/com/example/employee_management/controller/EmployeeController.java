package com.example.employee_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.dto.EmployeeResponseDTO;
import com.example.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Create a new employee", description = "Creates a new employee record. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public EmployeeResponseDTO createEmployee(@Valid @RequestBody EmployeeDTO employeeDto) {
        return employeeService.saveEmployee(employeeDto);
    }

    @Operation(summary = "Get all employees", description = "Fetches all employees. Accessible by ADMIN and USER roles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN or USER role")
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @Operation(summary = "Get employees with pagination and sorting",
               description = "Fetches employees with pagination and sorting. Accessible by ADMIN and USER roles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN or USER role")
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/paged")
    public Page<EmployeeResponseDTO> getEmployeesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return employeeService.getEmployeesPaged(page, size, sortBy, sortDir);
    }

    @Operation(summary = "Get employee by ID", description = "Fetches employee details by ID. Accessible by ADMIN and USER roles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee found"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN or USER role")
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @Operation(summary = "Update employee", description = "Updates an existing employee record. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDto) {
        return employeeService.updateEmployee(id, employeeDto);
    }

    @Operation(summary = "Delete employee", description = "Deletes an employee record by ID. Requires ADMIN role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted successfully!";
    }

    // 🔹 New: Search employees by name
    @Operation(summary = "Search employees by name", description = "Search employees whose name contains given text. Accessible by ADMIN and USER roles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN or USER role")
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/searchByName")
    public List<EmployeeResponseDTO> searchEmployeesByName(@RequestParam String name) {
        return employeeService.searchEmployeesByName(name);
    }

    // 🔹 New: Search employees by department
    @Operation(summary = "Search employees by department", description = "Search employees by department name. Accessible by ADMIN and USER roles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN or USER role")
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/searchByDepartment")
    public List<EmployeeResponseDTO> searchEmployeesByDepartment(@RequestParam String department) {
        return employeeService.searchEmployeesByDepartment(department);
    }
    @Operation(summary = "Search employees by salary range", description = "Fetch employees whose salary is between min and max. Accessible by ADMIN and USER roles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employees fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN or USER role")
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/searchBySalaryRange")
    public List<EmployeeResponseDTO> searchEmployeesBySalaryRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return employeeService.searchEmployeesBySalaryRange(min, max);
    }
    @Operation(summary = "Count employees by department", description = "Returns the number of employees in a given department. Accessible by ADMIN and USER roles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN or USER role")
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/countByDepartment")
    public Long countEmployeesByDepartment(@RequestParam String department) {
        return employeeService.countEmployeesByDepartment(department);
    }

}
