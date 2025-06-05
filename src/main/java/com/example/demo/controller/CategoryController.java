package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Category entities.
 * Provides endpoints to create, retrieve, update, and delete categories.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Constructor-based dependency injection of CategoryService.
     *
     * @param categoryService the service handling category business logic
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Endpoint to add a new category.
     *
     * @param category Category object received in request body
     * @return ResponseEntity containing the saved Category object
     */
    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category saved = categoryService.addCategory(category);
        return ResponseEntity.ok(saved);
    }

    /**
     * Endpoint to retrieve all categories.
     *
     * @return ResponseEntity with list of all Category objects
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * Endpoint to update an existing category identified by id.
     *
     * @param id the id of the category to update
     * @param category the Category object with updated data
     * @return ResponseEntity containing the updated Category object
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updated = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updated);
    }

    /**
     * Endpoint to delete a category by its id.
     *
     * @param id the id of the category to delete
     * @return ResponseEntity with no content status on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
