package com.example.demo.repository;

import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Employee entities.
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
