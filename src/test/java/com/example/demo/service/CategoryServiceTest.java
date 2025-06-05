package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for CategoryService class.
 * 
 * Uses Mockito to mock CategoryRepository dependency and verify interactions.
 * Uses AssertJ for fluent assertions.
 */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    // Mock the CategoryRepository so no actual DB calls are made
    @Mock
    private CategoryRepository categoryRepository;

    // Inject mocks into the CategoryService instance under test
    @InjectMocks
    private CategoryService categoryService;

    /**
     * Test successful addition of a new Category.
     * Verifies that the saved category has an assigned ID and expected properties.
     */
    @Test
    void testAddCategory() {
        // Input category without an ID (new)
        Category category = new Category(null, "Electronics", "All electronic items");

        // Simulate the repository saving and returning a category with ID
        Category savedCategory = new Category(1L, "Electronics", "All electronic items");
        when(categoryRepository.save(category)).thenReturn(savedCategory);

        // Call the service method
        Category result = categoryService.addCategory(category);

        // Assert the returned category is not null and has expected values
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Electronics");

        // Verify that save was called exactly once with the input category
        verify(categoryRepository, times(1)).save(category);
    }

    /**
     * Test retrieval of all categories.
     * Verifies the service returns the full list as provided by the repository.
     */
    @Test
    void testGetAllCategories() {
        // Prepare a list of two categories
        Category c1 = new Category(1L, "Electronics", "Electronic items");
        Category c2 = new Category(2L, "Furniture", "Furniture items");

        // Simulate repository returning the list
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        // Call the service method
        List<Category> categories = categoryService.getAllCategories();

        // Assert the list is not null, contains exactly the two categories
        assertThat(categories).isNotNull();
        assertThat(categories).hasSize(2);
        assertThat(categories).containsExactly(c1, c2);

        // Verify the repository's findAll method was called once
        verify(categoryRepository, times(1)).findAll();
    }

    /**
     * Test successful update of an existing category.
     * Verifies that the category is updated and saved correctly.
     */
    @Test
    void testUpdateCategory_Success() {
        // Existing category in repository
        Category existing = new Category(1L, "Electronics", "Old description");

        // New data to update the existing category
        Category updated = new Category(null, "Electronics", "Updated description");

        // Simulate repository finding the existing category
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));

        // Simulate repository saving the updated category and returning it
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        // Call the service method
        Category result = categoryService.updateCategory(1L, updated);

        // Assert that the description has been updated
        assertThat(result.getDescription()).isEqualTo("Updated description");

        // Verify repository interactions
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(existing);
    }

    /**
     * Test update failure when the category with the given ID does not exist.
     * Verifies that the proper exception is thrown and no save occurs.
     */
    @Test
    void testUpdateCategory_NotFound() {
        // Simulate repository returning empty for given ID
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Category updated = new Category(null, "Electronics", "Updated description");

        // Expect RuntimeException with specific message on update attempt
        assertThatThrownBy(() -> categoryService.updateCategory(1L, updated))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Category not found with id 1");

        // Verify findById was called but save was never called
        verify(categoryRepository).findById(1L);
        verify(categoryRepository, never()).save(any());
    }

    /**
     * Test successful deletion of a category by ID.
     * Verifies that the repository's deleteById method is called.
     */
    @Test
    void testDeleteCategory() {
        // No behavior needed to simulate deleteById, just verify call

        // Call the service method
        categoryService.deleteCategory(1L);

        // Verify repository deleteById was called exactly once with the given ID
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
