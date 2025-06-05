package com.example.demo.model;

import lombok.*;
import javax.persistence.*;

/**
 * Entity representing a Category of assets.
 * Each Category has a unique name and optional description.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /**
     * Primary key for Category entity, auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the category.
     * Must be unique and not null.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Optional description of the category.
     */
    private String description;
}
