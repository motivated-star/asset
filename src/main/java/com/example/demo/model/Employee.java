package com.example.demo.model;

import lombok.*;
import javax.persistence.*;

/**
 * Entity representing an Employee in the company.
 * Employees can be assigned assets.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    /**
     * Primary key for Employee entity.
     * Note: Not auto-generated, so must be assigned manually.
     */
    @Id
    private Long id;

    /**
     * Full name of the employee.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * Designation or job title of the employee.
     */
    private String designation;
}
