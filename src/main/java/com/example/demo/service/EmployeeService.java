package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class to handle basic Employee operations.
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Constructor for dependency injection of EmployeeRepository.
     * 
     * @param employeeRepository repository to manage Employee entities
     */
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Creates and saves a new Employee.
     * 
     * @param employee the Employee object to create
     * @return the saved Employee
     */
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * Retrieves all employees.
     * 
     * @return list of all Employee objects
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
