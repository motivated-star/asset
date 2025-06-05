package com.example.demo.repository;

import com.example.demo.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Asset entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom method to search assets by name with case-insensitive partial match.
 */
public interface AssetRepository extends JpaRepository<Asset, Long> {

    /**
     * Finds all assets whose names contain the given string, ignoring case.
     *
     * @param name substring to search within asset names
     * @return list of matching Asset entities
     */
    List<Asset> findByNameContainingIgnoreCase(String name);
}
