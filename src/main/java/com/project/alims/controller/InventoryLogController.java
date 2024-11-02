package com.project.alims.controller;

import com.project.alims.model.InventoryLog;
import com.project.alims.service.InventoryLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/inventory_log")
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;

    @Autowired
    public InventoryLogController(InventoryLogService inventoryLogService) {
        this.inventoryLogService = inventoryLogService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryLog>> getAllInventoryLogs() {
        List<InventoryLog> inventoryLogs = inventoryLogService.getAllInventoryLogs();
        return ResponseEntity.ok(inventoryLogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryLog> getInventoryLogById(@PathVariable Long id) {
        Optional<InventoryLog> inventoryLog = inventoryLogService.getInventoryLogById(id);
        return inventoryLog.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InventoryLog> createInventoryLog(@RequestBody InventoryLog inventoryLog) {
        InventoryLog createdInventoryLog = inventoryLogService.createInventoryLog(inventoryLog);
        return ResponseEntity.ok(createdInventoryLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryLog> updateInventoryLog(@PathVariable Long id, @RequestBody InventoryLog updatedInventoryLog) {
        try {
            InventoryLog inventoryLog = inventoryLogService.updateInventoryLog(id, updatedInventoryLog);
            return ResponseEntity.ok(inventoryLog);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryLog(@PathVariable Long id) {
        inventoryLogService.deleteInventoryLog(id);
        return ResponseEntity.noContent().build();
    }
}
