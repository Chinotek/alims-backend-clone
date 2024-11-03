package com.project.alims.service;

import com.project.alims.model.Inventory;
import com.project.alims.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public List<Inventory> getAllInventoryItems(Long inventoryLogId) {
        return inventoryRepository.findAllByInventoryLogId(inventoryLogId);
    }

    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory updatedInventory) {
        return inventoryRepository.findById(id)
                .map(existingInventory -> {
                    existingInventory.setInventoryLogId(updatedInventory.getInventoryLogId());
                    existingInventory.setMaterialId(updatedInventory.getMaterialId());
                    existingInventory.setMaterial(updatedInventory.getMaterial());
                    existingInventory.setInventoryLog(updatedInventory.getInventoryLog());
                    existingInventory.setQty(updatedInventory.getQty());
                    existingInventory.setUnit(updatedInventory.getUnit());
                    return inventoryRepository.save(existingInventory);
                }).orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}
