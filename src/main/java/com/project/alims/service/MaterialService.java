package com.project.alims.service;

import com.project.alims.model.Material;
import com.project.alims.model.Category;
import com.project.alims.model.Supplier;
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

    public Material createMaterial(Material material) {
        return materialRepository.save(material);
    }

    public Material findById(Long id) {
        return materialRepository.findByMaterialId(id);
    }

    public List<Material> findAllMaterials() {
        return materialRepository.findAll();
    }

    public List<Material> findByCategory(Category category) {
        return materialRepository.findByCategory(category);
    }

    public List<Material> findBySupplier(Supplier supplier) {
        return materialRepository.findBySupplier(supplier);
    }

    public Optional<Material> findByItemCode(String itemCode) {
        return Optional.ofNullable(materialRepository.findByItemCode(itemCode));
    }

    public Material updateMaterial(Long id, Material updatedMaterial) {
        Material material = materialRepository.findByMaterialId(id);
        if (material != null) {
            if (updatedMaterial.getItemName() != null) {
                material.setItemName(updatedMaterial.getItemName());
            }
            if (updatedMaterial.getItemCode() != null) {
                material.setItemCode(updatedMaterial.getItemCode());
            }
            if (updatedMaterial.getCategoryId() != null) {
                material.setCategoryId(updatedMaterial.getCategoryId());
            }
            if (updatedMaterial.getSupplierId() != null) {
                material.setSupplierId(updatedMaterial.getSupplierId());
            }
            if (updatedMaterial.getLabId() != null) {
                material.setLabId(updatedMaterial.getLabId());
            }
            if (updatedMaterial.getUnit() != null) {
                material.setUnit(updatedMaterial.getUnit());
            }
            if (updatedMaterial.getLocation() != null) {
                material.setLocation(updatedMaterial.getLocation());
            }
            if (updatedMaterial.getExpiryDate() != null) {
                material.setExpiryDate(updatedMaterial.getExpiryDate());
            }
            if (updatedMaterial.getCost() != null) {
                material.setCost(updatedMaterial.getCost());
            }
            if (updatedMaterial.getDescription() != null) {
                material.setDescription(updatedMaterial.getDescription());
            }
            if (updatedMaterial.getNotes() != null) {
                material.setNotes(updatedMaterial.getNotes());
            }
            if (updatedMaterial.getQuantityAvailable() != null) {
                material.setQuantityAvailable(updatedMaterial.getQuantityAvailable());
            }
            if (updatedMaterial.getReorderThreshold() != null) {
                material.setReorderThreshold(updatedMaterial.getReorderThreshold());
            }
            if (updatedMaterial.getMaxThreshold() != null) {
                material.setMaxThreshold(updatedMaterial.getMaxThreshold());
            }
            if (updatedMaterial.getTotalNoContainers() != null) {
                material.setTotalNoContainers(updatedMaterial.getTotalNoContainers());
            }
            if (updatedMaterial.getLotNo() != null) {
                material.setLotNo(updatedMaterial.getLotNo());
            }
            if (updatedMaterial.getQtyPerContainer() != null) {
                material.setQtyPerContainer(updatedMaterial.getQtyPerContainer());
            }
            return materialRepository.save(material);
        } else {
            throw new RuntimeException("Material not found with ID: " + id);
        }
    }


    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }
}
