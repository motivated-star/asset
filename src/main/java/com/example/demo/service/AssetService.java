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

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;
    private final EmployeeRepository employeeRepository;


    public AssetService(AssetRepository assetRepository, CategoryRepository categoryRepository, EmployeeRepository employeeRepository) {
        this.assetRepository = assetRepository;
        this.categoryRepository = categoryRepository;
        this.employeeRepository = employeeRepository;
    }

    public Asset assignAssetToEmployee(Long assetId, Long employeeId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with id " + assetId));
        if (asset.getAssignmentStatus() == Asset.AssignmentStatus.ASSIGNED) {
            throw new RuntimeException("Asset is already assigned");
        }
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId));
        asset.setAssignmentStatus(Asset.AssignmentStatus.ASSIGNED);
        asset.setAssignedTo(employee); // assuming you add this field in Asset entity
        return assetRepository.save(asset);
    }

    // Recover asset from employee
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

    public Asset addAsset(Asset asset) {
        // Validate category exists
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

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public List<Asset> searchAssetsByName(String name) {
        return assetRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public Asset updateAsset(Long id, Asset updatedAsset) {
        return assetRepository.findById(id).map(asset -> {
            asset.setName(updatedAsset.getName());
            asset.setPurchaseDate(updatedAsset.getPurchaseDate());
            asset.setConditionNotes(updatedAsset.getConditionNotes());
            // Update category
            Long categoryId = updatedAsset.getCategory().getId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
            asset.setCategory(category);
            asset.setAssignmentStatus(updatedAsset.getAssignmentStatus());
            return assetRepository.save(asset);
        }).orElseThrow(() -> new RuntimeException("Asset not found with id " + id));
    }

    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id " + id));
        if (asset.getAssignmentStatus() == Asset.AssignmentStatus.ASSIGNED) {
            throw new RuntimeException("Cannot delete asset that is assigned.");
        }
        assetRepository.delete(asset);
    }
}
