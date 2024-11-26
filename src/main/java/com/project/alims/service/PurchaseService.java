package com.project.alims.service;

import com.project.alims.model.InventoryLog;
import com.project.alims.model.Material;
import com.project.alims.model.Purchase;
import com.project.alims.model.PurchaseOrder;
import com.project.alims.repository.PurchaseOrderRepository;
import com.project.alims.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InventoryLogService inventoryLogService;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseOrderRepository purchaseOrderRepository, InventoryLogService inventoryLogService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.inventoryLogService = inventoryLogService;
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase findByPurchaseId(Long purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + purchaseId));
    }

    public List<Purchase> getAllPurchasesByPurchaseOrderId(Long purchaseOrderId) {
        return purchaseRepository.findByPurchaseOrderId(purchaseOrderId);
    }

    // Create a new purchase order
    public Purchase createPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public InventoryLog addQuantityToMaterial(Purchase purchase, PurchaseOrder purchaseOrder, Integer addedQuantity) {
        Material material = purchase.getMaterial();
        InventoryLog inventoryLog = new InventoryLog(
                purchaseOrder.getUserId(),
                purchase.getMaterialId(),
                LocalDate.now(),
                addedQuantity,
                "Purchase Order: " + purchaseOrder.getPurchaseOrderNumber() + " Purchase : " + purchase.getPurchaseId(),
                "Purchase Edited: Added " + addedQuantity + " " + material.getUnit() + " of " +
                        material.getItemName() + " on " + LocalDate.now()
        );
        return inventoryLogService.createInventoryLog(inventoryLog);
    }


    // Update an existing purchase order by its ID
    public Purchase updatePurchase(Long PurchaseId, Purchase updatedPurchase) {
        Purchase existingPurchase = purchaseRepository.findById(PurchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + PurchaseId));
        PurchaseOrder purchaseOrder = existingPurchase.getPurchaseOrder();
        if(updatedPurchase.getPurchaseOrderId() != null) {
            purchaseOrder = purchaseOrderRepository.findById(updatedPurchase.getPurchaseOrderId())
                    .orElseThrow(() -> new RuntimeException("Purchase Order not found with ID: " + updatedPurchase.getPurchaseOrderId()));
        }
        Integer previousQty = 0;
        if(updatedPurchase.getQty() != null) previousQty = existingPurchase.getQty();

        // Update fields
        if(updatedPurchase.getPurchaseOrderId() != null) existingPurchase.setPurchaseOrderId(updatedPurchase.getPurchaseOrderId());
        if(updatedPurchase.getMaterialId() != null) existingPurchase.setMaterialId(updatedPurchase.getMaterialId());

        if(updatedPurchase.getQty() != null) existingPurchase.setQty(updatedPurchase.getQty());
        if(updatedPurchase.getDescription() != null) existingPurchase.setDescription(updatedPurchase.getDescription());
        if(updatedPurchase.getUnitPrice() != null) existingPurchase.setUnitPrice(updatedPurchase.getUnitPrice());

        Purchase purchase = purchaseRepository.save(existingPurchase);
        if("Completed".equalsIgnoreCase(purchaseOrder.getStatus()) && updatedPurchase.getQty() != null) {
            // handle mo, make log based on
            Integer addedQuantity = updatedPurchase.getQty() - previousQty;
            addQuantityToMaterial(purchase, purchaseOrder, addedQuantity);
        }
        return purchase;
    }

    // Delete a purchase order by its ID
    public void deletePurchase(Long PurchaseId) {
        Purchase existingOrder = purchaseRepository.findById(PurchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + PurchaseId));
        purchaseRepository.deleteById(PurchaseId);
    }
}
