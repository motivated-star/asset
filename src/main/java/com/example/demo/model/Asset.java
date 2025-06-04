package com.example.demo.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalDate purchaseDate;

    private String conditionNotes;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus assignmentStatus = AssignmentStatus.AVAILABLE;  // default value

    @ManyToOne
    @JoinColumn(name = "employee_id")  
    private Employee assignedTo;

    public enum AssignmentStatus {
        AVAILABLE,
        ASSIGNED,
        RECOVERED
    }
}
