package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class to handle business logic related to Category management.
 * Provides operations to add, retrieve, update, and delete categories.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Constructor for dependency injection of CategoryRepository.
     *
     * @param categoryRepository the repository used to manage Category entities
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adds a new category.
     * Additional validation can be added here if required.
     *
     * @param category the Category object to be added
     * @return the saved Category object
     */
    public Category addCategory(Category category) {
        // Validation can be added if needed
        return categoryRepository.save(category);
    }

    /**
     * Retrieves all categories.
     *
     * @return list of all Category objects
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category
     * @return an Optional containing the Category if found, otherwise empty
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Updates an existing category.
     *
     * @param id ID of the category to update
     * @param updatedCategory Category object containing new data
     * @return the updated Category object
     * @throws RuntimeException if the category with the given ID does not exist
     */
    public Category updateCategory(Long id, Category updatedCategory) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(updatedCategory.getName());
            category.setDescription(updatedCategory.getDescription());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     */
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
