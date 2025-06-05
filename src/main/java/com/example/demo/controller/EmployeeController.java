package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Employee entities.
 * Provides endpoints to retrieve all employees and create new employees.
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    /**
     * Constructor-based dependency injection of EmployeeRepository.
     *
     * @param employeeRepository the repository managing employee persistence
     */
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Endpoint to retrieve all employees.
     *
     * @return list of all Employee objects
     */
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Endpoint to create a new employee.
     *
     * @param employee Employee object received in request body
     * @return the saved Employee object
     */
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }
}
