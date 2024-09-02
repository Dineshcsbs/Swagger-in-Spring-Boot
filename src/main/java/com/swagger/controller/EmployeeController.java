package com.swagger.controller;

import com.swagger.entity.Employee;
import com.swagger.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody final Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping("/employee/{empId}")
    public Employee getById(@PathVariable final String empId) {
        return employeeService.getEmployeeById(empId);
    }

    @GetMapping("/employee-all")
    @PreAuthorize("EMPLOYEE")
    public List<Employee> getAllEmployee() {
        return employeeService.getAllEmployee();
    }

    @DeleteMapping("/employee/{empId}")
    public String deleteEmployee(@PathVariable final String empId) {
        return employeeService.deleteById(empId);
    }
}
