package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for EmployeeService class.
 * 
 * Uses Mockito to mock EmployeeRepository dependency to avoid real DB calls.
 * Uses AssertJ for fluent assertions.
 */
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    // Mock the EmployeeRepository
    @Mock
    private EmployeeRepository employeeRepository;

    // Inject mocks into EmployeeService instance
    @InjectMocks
    private EmployeeService employeeService;

    /**
     * Test creating a new employee.
     * Verifies that the employee is saved and returned with expected details.
     */
    @Test
    void testCreateEmployee() {
        // Employee instance to be saved
        Employee emp = new Employee(1L, "John Doe", "Developer");

        // Simulate repository saving the employee and returning it
        when(employeeRepository.save(emp)).thenReturn(emp);

        // Call the service method
        Employee savedEmployee = employeeService.createEmployee(emp);

        // Assert the returned employee is not null and name matches
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFullName()).isEqualTo("John Doe");

        // Verify that save was called exactly once with the employee
        verify(employeeRepository, times(1)).save(emp);
    }

    /**
     * Test retrieving all employees.
     * Verifies that the service returns all employees provided by the repository.
     */
    @Test
    void testGetAllEmployees() {
        // Create sample employees
        Employee emp1 = new Employee(1L, "John Doe", "Developer");
        Employee emp2 = new Employee(2L, "Jane Smith", "Manager");

        List<Employee> employeeList = Arrays.asList(emp1, emp2);

        // Simulate repository returning the employee list
        when(employeeRepository.findAll()).thenReturn(employeeList);

        // Call the service method
        List<Employee> result = employeeService.getAllEmployees();

        // Assert the returned list is not null, has size 2, and contains the exact employees
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(emp1, emp2);

        // Verify repository's findAll method was called once
        verify(employeeRepository, times(1)).findAll();
    }
}
