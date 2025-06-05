package com.example.demo.controller;

import com.example.demo.model.Asset;
import com.example.demo.service.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Asset entities.
 * Provides endpoints to create, retrieve, update, delete, assign, and recover assets.
 */
@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    /**
     * Constructor-based dependency injection of AssetService.
     *
     * @param assetService the service handling asset business logic
     */
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * Endpoint to add a new asset.
     *
     * @param asset Asset object received in request body
     * @return ResponseEntity containing the saved Asset object
     */
    @PostMapping
    public ResponseEntity<Asset> addAsset(@RequestBody Asset asset) {
        Asset saved = assetService.addAsset(asset);
        return ResponseEntity.ok(saved);
    }

    /**
     * Endpoint to retrieve all assets.
     *
     * @return ResponseEntity with list of all Asset objects
     */
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    /**
     * Endpoint to search assets by their name.
     *
     * @param name search query parameter for asset name
     * @return ResponseEntity with list of matching assets
     */
    @GetMapping("/search")
    public ResponseEntity<List<Asset>> searchAssets(@RequestParam String name) {
        return ResponseEntity.ok(assetService.searchAssetsByName(name));
    }

    /**
     * Endpoint to update an existing asset identified by id.
     *
     * @param id the id of the asset to update
     * @param asset the Asset object with updated data
     * @return ResponseEntity containing the updated Asset object
     */
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Asset asset) {
        Asset updated = assetService.updateAsset(id, asset);
        return ResponseEntity.ok(updated);
    }

    /**
     * Endpoint to delete an asset by its id.
     *
     * @param id the id of the asset to delete
     * @return ResponseEntity with no content status on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint to assign an asset to an employee.
     *
     * @param assetId id of the asset to assign
     * @param employeeId id of the employee to assign the asset to
     * @return ResponseEntity containing the assigned Asset object
     */
    @PostMapping("/{assetId}/assign/{employeeId}")
    public ResponseEntity<Asset> assignAsset(@PathVariable Long assetId, @PathVariable Long employeeId) {
        Asset assignedAsset = assetService.assignAssetToEmployee(assetId, employeeId);
        return ResponseEntity.ok(assignedAsset);
    }

    /**
     * Endpoint to recover an asset (mark as available).
     *
     * @param assetId id of the asset to recover
     * @return ResponseEntity containing the recovered Asset object
     */
    @PostMapping("/{assetId}/recover")
    public ResponseEntity<Asset> recoverAsset(@PathVariable Long assetId) {
        Asset recoveredAsset = assetService.recoverAsset(assetId);
        return ResponseEntity.ok(recoveredAsset);
    }
}
