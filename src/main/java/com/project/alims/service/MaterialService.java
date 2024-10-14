package com.project.alims.service;

import com.project.alims.model.Material;
import com.project.alims.model.Category;
import com.project.alims.model.Laboratory;
import com.project.alims.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    // Create a new material
    public Material createMaterial(Material material) {
        material.setCreatedAt(LocalDateTime.now());
        material.setUpdatedAt(LocalDateTime.now());
        return materialRepository.save(material);
    }

    // Find material by ID
    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
    }

    // Get all materials
    public List<Material> findAllMaterials() {
        return materialRepository.findAll();
    }

    // Find materials by category
    public List<Material> findByCategory(Category category) {
        return materialRepository.findByCategory(category);
    }

    // Find materials by laboratory
    public List<Material> findByLaboratory(Laboratory laboratory) {
        return materialRepository.findByLaboratory(laboratory);
    }

    // Find material by item code
    public Optional<Material> findByItemCode(String itemCode) {
        return Optional.ofNullable(materialRepository.findByItemCode(itemCode));
    }

    // Update an existing material
    public Material updateMaterial(Long id, Material updatedMaterial) {
        return materialRepository.findById(id).map(material -> {
            material.setItemName(updatedMaterial.getItemName());
            material.setItemCode(updatedMaterial.getItemCode());
            material.setCategory(updatedMaterial.getCategory());
            material.setSupplier(updatedMaterial.getSupplier());
            material.setLaboratory(updatedMaterial.getLaboratory());
            material.setUnit(updatedMaterial.getUnit());
            material.setLocation(updatedMaterial.getLocation());
            material.setExpiryDate(updatedMaterial.getExpiryDate());
            material.setCost(updatedMaterial.getCost());
            material.setDescription(updatedMaterial.getDescription());
            material.setNotes(updatedMaterial.getNotes());
            material.setQuantityAvailable(updatedMaterial.getQuantityAvailable());
            material.setReorderThreshold(updatedMaterial.getReorderThreshold());
            material.setMaxThreshold(updatedMaterial.getMaxThreshold());
            material.setUpdatedAt(LocalDateTime.now());
            return materialRepository.save(material);
        }).orElseThrow(() -> new RuntimeException("Material not found with ID: " + id));
    }

    // Delete a material by ID
    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }
}
