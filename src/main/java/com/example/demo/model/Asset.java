package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entity representing an Asset in the system.
 * An Asset can be assigned to an Employee and belongs to a Category.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    /**
     * Primary key for Asset entity, auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the asset.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Date when the asset was purchased.
     */
    private LocalDate purchaseDate;

    /**
     * Additional notes on the condition of the asset.
     */
    private String conditionNotes;

    /**
     * The Category this asset belongs to.
     * Many assets can belong to one category.
     * This association is mandatory.
     */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * The assignment status of the asset.
     * Can be AVAILABLE, ASSIGNED, or RECOVERED.
     * Defaults to AVAILABLE.
     */
    @Enumerated(EnumType.STRING)
    private AssignmentStatus assignmentStatus = AssignmentStatus.AVAILABLE;

    /**
     * The Employee to whom the asset is assigned.
     * Many assets can be assigned to one employee.
     * This association is optional.
     */
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee assignedTo;

    /**
     * Enum representing possible assignment statuses for the asset.
     */
    public enum AssignmentStatus {
        AVAILABLE,
        ASSIGNED,
        RECOVERED
    }
}
