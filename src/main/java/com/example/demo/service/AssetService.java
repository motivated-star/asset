package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.model.Asset;
import com.example.demo.model.Category;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class to handle business logic related to Asset management.
 * Includes operations to add, update, delete, assign, recover, and search assets.
 */
@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor-based dependency injection for repositories.
     *
     * @param assetRepository Repository for Asset entity
     * @param categoryRepository Repository for Category entity
     * @param employeeRepository Repository for Employee entity
     */
    public AssetService(AssetRepository assetRepository, CategoryRepository categoryRepository, EmployeeRepository employeeRepository) {
        this.assetRepository = assetRepository;
        this.categoryRepository = categoryRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Assigns an asset to an employee.
     * Validates if asset exists and is not already assigned.
     * Validates if employee exists.
     *
     * @param assetId ID of the asset to assign
     * @param employeeId ID of the employee to assign the asset to
     * @return the updated Asset object after assignment
     * @throws RuntimeException if asset or employee not found or asset already assigned
     */
    public Asset assignAssetToEmployee(Long assetId, Long employeeId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with id " + assetId));
        if (asset.getAssignmentStatus() == Asset.AssignmentStatus.ASSIGNED) {
            throw new RuntimeException("Asset is already assigned");
        }
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId));
        asset.setAssignmentStatus(Asset.AssignmentStatus.ASSIGNED);
        asset.setAssignedTo(employee);
        return assetRepository.save(asset);
    }

    /**
     * Recovers an assigned asset from an employee.
     * Validates if asset exists and is currently assigned.
     *
     * @param assetId ID of the asset to recover
     * @return the updated Asset object after recovery
     * @throws RuntimeException if asset not found or not currently assigned
     */
    public Asset recoverAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with id " + assetId));
        if (asset.getAssignmentStatus() != Asset.AssignmentStatus.ASSIGNED) {
            throw new RuntimeException("Asset is not currently assigned");
        }
        asset.setAssignmentStatus(Asset.AssignmentStatus.RECOVERED);
        asset.setAssignedTo(null);
        return assetRepository.save(asset);
    }

    /**
     * Adds a new asset after validating the associated category.
     *
     * @param asset the Asset object to add
     * @return the saved Asset object
     * @throws RuntimeException if the category is missing or not found
     */
    public Asset addAsset(Asset asset) {
        if (asset.getCategory() == null || asset.getCategory().getId() == null) {
            throw new RuntimeException("Category is required with a valid ID");
        }
        Long categoryId = asset.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
        asset.setCategory(category);
        asset.setAssignmentStatus(Asset.AssignmentStatus.AVAILABLE);
        return assetRepository.save(asset);
    }

    /**
     * Retrieves all assets.
     *
     * @return list of all Asset objects
     */
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    /**
     * Searches assets by name with case-insensitive partial matching.
     *
     * @param name substring to search within asset names
     * @return list of matching Asset objects
     */
    public List<Asset> searchAssetsByName(String name) {
        return assetRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Retrieves an asset by its ID.
     *
     * @param id the ID of the asset
     * @return an Optional containing the Asset if found, empty otherwise
     */
    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    /**
     * Updates an existing asset.
     * Validates asset existence and category association.
     *
     * @param id ID of the asset to update
     * @param updatedAsset Asset object containing updated data
     * @return the updated Asset object
     * @throws RuntimeException if asset or category not found
     */
    public Asset updateAsset(Long id, Asset updatedAsset) {
        return assetRepository.findById(id).map(asset -> {
            asset.setName(updatedAsset.getName());
            asset.setPurchaseDate(updatedAsset.getPurchaseDate());
            asset.setConditionNotes(updatedAsset.getConditionNotes());

            Long categoryId = updatedAsset.getCategory().getId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
            asset.setCategory(category);
            asset.setAssignmentStatus(updatedAsset.getAssignmentStatus());
            return assetRepository.save(asset);
        }).orElseThrow(() -> new RuntimeException("Asset not found with id " + id));
    }

    /**
     * Deletes an asset if it is not currently assigned.
     *
     * @param id ID of the asset to delete
     * @throws RuntimeException if asset not found or currently assigned
     */
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id " + id));
        if (asset.getAssignmentStatus() == Asset.AssignmentStatus.ASSIGNED) {
            throw new RuntimeException("Cannot delete asset that is assigned.");
        }
        assetRepository.delete(asset);
    }
}
