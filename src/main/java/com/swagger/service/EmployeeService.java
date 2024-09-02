package com.swagger.service;

import com.swagger.entity.Employee;
import com.swagger.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee createEmployee(final Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(final String employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(null);
    }

    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public String deleteById(final String employeeId) {
        employeeRepository.deleteById(employeeId);
        return "delete";
    }
}
