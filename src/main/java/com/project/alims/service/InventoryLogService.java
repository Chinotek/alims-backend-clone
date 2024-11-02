package com.project.alims.service;

import com.project.alims.model.InventoryLog;
import com.project.alims.repository.InventoryLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryLogService {

    private final InventoryLogRepository inventoryLogRepository;

    @Autowired
    public InventoryLogService(InventoryLogRepository inventoryLogRepository) {
        this.inventoryLogRepository = inventoryLogRepository;
    }

    public List<InventoryLog> getAllInventoryLogs() {
        return inventoryLogRepository.findAll();
    }

    public InventoryLog findByInventoryLogId(Long id) {
        return inventoryLogRepository.findById(id).orElse(null);
    }

    public InventoryLog createInventoryLog(InventoryLog inventoryLog) {
        return inventoryLogRepository.save(inventoryLog);
    }

    public InventoryLog updateInventoryLog(Long id, InventoryLog updatedInventoryLog) {
        return inventoryLogRepository.findById(id)
                .map(existingLog -> {
                    existingLog.setDate(updatedInventoryLog.getDate());
                    existingLog.setRemarks(updatedInventoryLog.getRemarks());
                    existingLog.setUser(updatedInventoryLog.getUser());
                    return inventoryLogRepository.save(existingLog);
                }).orElseThrow(() -> new RuntimeException("InventoryLog not found"));
    }

    public void deleteInventoryLog(Long id) {
        inventoryLogRepository.deleteById(id);
    }
}
