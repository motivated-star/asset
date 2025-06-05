package com.example.demo.service;

import com.example.demo.model.Asset;
import com.example.demo.model.Category;
import com.example.demo.model.Employee;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for AssetService class.
 * Uses Mockito to mock dependencies (repositories) and isolate service logic.
 */
@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;  // Mock Asset repository to simulate DB operations

    @Mock
    private CategoryRepository categoryRepository; // Mock Category repository

    @Mock
    private EmployeeRepository employeeRepository; // Mock Employee repository

    @InjectMocks
    private AssetService assetService; // Service instance with mocked dependencies injected

    private Category category;
    private Employee employee;
    private Asset asset;

    /**
     * Initialize test data before each test.
     */
    @BeforeEach
    void setup() {
        // Create a sample Category object
        category = new Category(1L, "Electronics", "Electronic devices");

        // Create a sample Employee object
        employee = new Employee(1L, "Alice", "Engineer");

        // Create a sample Asset object linked with above category
        asset = new Asset();
        asset.setId(1L);
        asset.setName("Laptop");
        asset.setCategory(category);
        asset.setPurchaseDate(LocalDate.now().minusDays(10));
        asset.setAssignmentStatus(Asset.AssignmentStatus.AVAILABLE);
        asset.setAssignedTo(null);
    }

    /**
     * Test case for successfully adding a new asset.
     * - Mocks category lookup success
     * - Mocks save operation to return the passed asset
     * - Asserts returned asset has correct category and status
     */
    @Test
    void testAddAsset_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArgument(0));

        Asset newAsset = new Asset();
        newAsset.setName("Laptop");
        newAsset.setCategory(category);

        Asset savedAsset = assetService.addAsset(newAsset);

        assertThat(savedAsset).isNotNull();
        assertThat(savedAsset.getCategory()).isEqualTo(category);
        assertThat(savedAsset.getAssignmentStatus()).isEqualTo(Asset.AssignmentStatus.AVAILABLE);

        // Verify that mocks were called as expected
        verify(categoryRepository).findById(1L);
        verify(assetRepository).save(newAsset);
    }

    /**
     * Test case for adding an asset with no category.
     * Expects RuntimeException because category is required.
     */
    @Test
    void testAddAsset_CategoryMissing() {
        Asset newAsset = new Asset();
        newAsset.setName("Laptop");
        newAsset.setCategory(null); // No category set

        assertThatThrownBy(() -> assetService.addAsset(newAsset))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Category is required");
    }

    /**
     * Test case for adding an asset with a non-existing category ID.
     * Expects RuntimeException because category not found in DB.
     */
    @Test
    void testAddAsset_CategoryNotFound() {
        Asset newAsset = new Asset();
        Category unknownCategory = new Category();
        unknownCategory.setId(999L);
        newAsset.setCategory(unknownCategory);

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.addAsset(newAsset))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Category not found");
    }

    /**
     * Test for getting all assets.
     * Mocks repository to return a list with one asset.
     */
    @Test
    void testGetAllAssets() {
        when(assetRepository.findAll()).thenReturn(Arrays.asList(asset));

        List<Asset> assets = assetService.getAllAssets();

        assertThat(assets).isNotEmpty();

        verify(assetRepository).findAll();
    }

    /**
     * Test for searching assets by name (case-insensitive, partial).
     */
    @Test
    void testSearchAssetsByName() {
        when(assetRepository.findByNameContainingIgnoreCase("lap")).thenReturn(Arrays.asList(asset));

        List<Asset> assets = assetService.searchAssetsByName("lap");

        assertThat(assets).hasSize(1);

        verify(assetRepository).findByNameContainingIgnoreCase("lap");
    }

    /**
     * Test successful assignment of an asset to an employee.
     * Verifies status change and employee association.
     */
    @Test
    void testAssignAssetToEmployee_Success() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArgument(0));

        Asset assigned = assetService.assignAssetToEmployee(1L, 1L);

        assertThat(assigned.getAssignmentStatus()).isEqualTo(Asset.AssignmentStatus.ASSIGNED);
        assertThat(assigned.getAssignedTo()).isEqualTo(employee);
    }

    /**
     * Test assigning an asset which is already assigned.
     * Expects exception.
     */
    @Test
    void testAssignAssetToEmployee_AlreadyAssigned() {
        asset.setAssignmentStatus(Asset.AssignmentStatus.ASSIGNED);

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

        assertThatThrownBy(() -> assetService.assignAssetToEmployee(1L, 1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("already assigned");
    }

    /**
     * Test assigning an asset that does not exist.
     * Expects exception.
     */
    @Test
    void testAssignAssetToEmployee_AssetNotFound() {
        when(assetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.assignAssetToEmployee(1L, 1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Asset not found");
    }

    /**
     * Test assigning an asset to an employee who does not exist.
     * Expects exception.
     */
    @Test
    void testAssignAssetToEmployee_EmployeeNotFound() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.assignAssetToEmployee(1L, 1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Employee not found");
    }

    /**
     * Test successful recovery of an assigned asset.
     * Verifies status set to RECOVERED and assigned employee removed.
     */
    @Test
    void testRecoverAsset_Success() {
        asset.setAssignmentStatus(Asset.AssignmentStatus.ASSIGNED);
        asset.setAssignedTo(employee);

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArgument(0));

        Asset recovered = assetService.recoverAsset(1L);

        assertThat(recovered.getAssignmentStatus()).isEqualTo(Asset.AssignmentStatus.RECOVERED);
        assertThat(recovered.getAssignedTo()).isNull();
    }

    /**
     * Test recovering an asset which is not currently assigned.
     * Expects exception.
     */
    @Test
    void testRecoverAsset_NotAssigned() {
        asset.setAssignmentStatus(Asset.AssignmentStatus.AVAILABLE);

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

        assertThatThrownBy(() -> assetService.recoverAsset(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("not currently assigned");
    }

    /**
     * Test recovering a non-existent asset.
     * Expects exception.
     */
    @Test
    void testRecoverAsset_AssetNotFound() {
        when(assetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.recoverAsset(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Asset not found");
    }

    /**
     * Test successful update of an asset.
     * Mocks asset and category existence and save operation.
     */
    @Test
    void testUpdateAsset_Success() {
        Asset updatedAsset = new Asset();
        updatedAsset.setName("Updated Laptop");
        updatedAsset.setPurchaseDate(LocalDate.now());
        updatedAsset.setConditionNotes("Good condition");
        updatedAsset.setCategory(category);
        updatedAsset.setAssignmentStatus(Asset.AssignmentStatus.AVAILABLE);

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArgument(0));

        Asset result = assetService.updateAsset(1L, updatedAsset);

        assertThat(result.getName()).isEqualTo("Updated Laptop");
        assertThat(result.getConditionNotes()).isEqualTo("Good condition");

        verify(assetRepository).save(any(Asset.class));
    }

    /**
     * Test updating an asset that does not exist.
     * Expects exception.
     */
    @Test
    void testUpdateAsset_NotFound() {
        Asset updatedAsset = new Asset();
        updatedAsset.setCategory(category);

        when(assetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.updateAsset(1L, updatedAsset))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Asset not found");
    }

    /**
     * Test successful deletion of an asset.
     * Asset must be AVAILABLE for deletion.
     */
    @Test
    void testDeleteAsset_Success() {
        asset.setAssignmentStatus(Asset.AssignmentStatus.AVAILABLE);

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        doNothing().when(assetRepository).delete(asset);

        assetService.deleteAsset(1L);

        verify(assetRepository).delete(asset);
    }

    /**
     * Test deletion attempt on an assigned asset.
     * Expects exception because assigned assets cannot be deleted.
     */
    @Test
    void testDeleteAsset_Assigned() {
        asset.setAssignmentStatus(Asset.AssignmentStatus.ASSIGNED);

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

        assertThatThrownBy(() -> assetService.deleteAsset(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cannot delete asset that is assigned");
    }

    /**
     * Test deletion attempt on a non-existent asset.
     * Expects exception.
     */
    @Test
    void testDeleteAsset_NotFound() {
        when(assetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assetService.deleteAsset(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Asset not found");
    }
}
