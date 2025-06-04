package com.example.demo.controller;

import com.example.demo.model.Asset;
import com.example.demo.service.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // Add Asset
    @PostMapping
    public ResponseEntity<Asset> addAsset(@RequestBody Asset asset) {
        Asset saved = assetService.addAsset(asset);
        return ResponseEntity.ok(saved);
    }

    // List all assets
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    // Search assets by name
    @GetMapping("/search")
    public ResponseEntity<List<Asset>> searchAssets(@RequestParam String name) {
        return ResponseEntity.ok(assetService.searchAssetsByName(name));
    }

    // Update asset
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Asset asset) {
        Asset updated = assetService.updateAsset(id, asset);
        return ResponseEntity.ok(updated);
    }

    // Delete asset
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
    // Assign asset to employee
    @PostMapping("/{assetId}/assign/{employeeId}")
    public ResponseEntity<Asset> assignAsset(@PathVariable Long assetId, @PathVariable Long employeeId) {
        Asset assignedAsset = assetService.assignAssetToEmployee(assetId, employeeId);
        return ResponseEntity.ok(assignedAsset);
    }

    // Recover asset from employee
    @PostMapping("/{assetId}/recover")
    public ResponseEntity<Asset> recoverAsset(@PathVariable Long assetId) {
        Asset recoveredAsset = assetService.recoverAsset(assetId);
        return ResponseEntity.ok(recoveredAsset);
    }

}
