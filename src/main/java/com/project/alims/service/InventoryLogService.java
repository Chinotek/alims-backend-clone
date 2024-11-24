package com.project.alims.service;

import com.project.alims.model.InventoryLog;
import com.project.alims.model.Material;
import com.project.alims.repository.InventoryLogRepository;
import com.project.alims.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryLogService {

    private final InventoryLogRepository inventoryLogRepository;
    private final MaterialRepository materialRepository;

    @Autowired
    public InventoryLogService(InventoryLogRepository inventoryLogRepository, MaterialRepository materialRepository) {
        this.inventoryLogRepository = inventoryLogRepository;
        this.materialRepository = materialRepository;
    }

    public List<InventoryLog> getAllInventoryLogs() {
        return inventoryLogRepository.findAll();
    }

    public List<InventoryLog> getInventoryLogsByMaterialId(Long materialId) {
        return inventoryLogRepository.findByMaterialId(materialId);
    }

    public InventoryLog findByInventoryLogId(Long id) {
        return inventoryLogRepository.findById(id).orElse(null);
    }

    public Material AddQuantitytoMaterial(Material existingLogMaterial, Integer addedAmount) {
        if (existingLogMaterial != null) {
            Integer quantityAvailable = existingLogMaterial.getQuantityAvailable();
            quantityAvailable += addedAmount;
            existingLogMaterial.setQuantityAvailable(quantityAvailable);
            return materialRepository.save(existingLogMaterial);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public InventoryLog createInventoryLog(InventoryLog inventoryLog) {
        Integer addedAmount = inventoryLog.getQuantity();
        Long existingMaterialId = inventoryLog.getMaterialId();

        Material existingMaterial = materialRepository.findById(existingMaterialId)
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));
        AddQuantitytoMaterial(existingMaterial, addedAmount);
        return inventoryLogRepository.save(inventoryLog);
    }

    public InventoryLog updateInventoryLog(Long id, InventoryLog updatedInventoryLog) {
        InventoryLog existingLog = inventoryLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InventoryLog not found with ID: " + id));

        Integer addedAmount = 0;
        if(updatedInventoryLog.getQuantity() != null && existingLog.getQuantity() != null) {
            addedAmount = updatedInventoryLog.getQuantity() - existingLog.getQuantity();
        }
        Long existingMaterialId = updatedInventoryLog.getMaterialId();
        Material existingMaterial = materialRepository.findById(existingMaterialId)
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));

        if(addedAmount != 0) AddQuantitytoMaterial(existingMaterial, addedAmount);

        if(updatedInventoryLog.getDate() != null) existingLog.setDate(updatedInventoryLog.getDate());
        if(updatedInventoryLog.getDate() != null) existingLog.setSource(updatedInventoryLog.getSource());
        if(updatedInventoryLog.getDate() != null) existingLog.setRemarks(updatedInventoryLog.getRemarks());

        if(updatedInventoryLog.getDate() != null) existingLog.setQuantity(updatedInventoryLog.getQuantity());

        if(updatedInventoryLog.getDate() != null) existingLog.setMaterialId(updatedInventoryLog.getMaterialId());
        if(updatedInventoryLog.getDate() != null) existingLog.setUserId(updatedInventoryLog.getUserId());

        return inventoryLogRepository.save(existingLog);
    }

    public void deleteInventoryLog(Long id) {
        inventoryLogRepository.deleteById(id);
    }
}
