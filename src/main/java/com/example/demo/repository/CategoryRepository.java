package com.example.demo.repository;

import com.example.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for Category entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes a method to find a category by its unique name.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds a category by its exact name.
     *
     * @param name the name of the category
     * @return an Optional containing the Category if found, or empty if not found
     */
    Optional<Category> findByName(String name);
}
