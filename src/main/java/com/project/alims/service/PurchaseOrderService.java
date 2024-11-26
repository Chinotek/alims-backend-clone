package com.project.alims.service;

import com.project.alims.model.*;
import com.project.alims.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InventoryLogService inventoryLogService;
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, InventoryLogService inventoryLogService, PurchaseService purchaseService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.inventoryLogService = inventoryLogService;
        this.purchaseService = purchaseService;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder findByPurchaseOrderId(Long PurchaseOrderId) {
        return purchaseOrderRepository.findById(PurchaseOrderId).orElse(null);
    }

    // make an inventory Log for each Purchase attached
    public void makeLogs(PurchaseOrder purchaseOrder) {
        List<Purchase> purchases = purchaseService.getAllPurchasesByPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
        for (Purchase purchase : purchases) {
            addQuantityToMaterial(purchase, purchaseOrder);
        }
    }
    public InventoryLog addQuantityToMaterial(Purchase purchase, PurchaseOrder purchaseOrder) {
        Material material = purchase.getMaterial();
        InventoryLog inventoryLog = new InventoryLog(
                purchaseOrder.getUserId(),
                purchase.getMaterialId(),
                LocalDate.now(),
                purchase.getQty(),
                "Purchase Order: " + purchaseOrder.getPurchaseOrderNumber() + " Purchase : " + purchase.getPurchaseId(),
                "Purchased " + purchase.getQty() + " " + material.getUnit() + " of " +
                        material.getItemName() + " on " + LocalDate.now()
        );
        return inventoryLogService.createInventoryLog(inventoryLog);
    }

    // Create a new purchase order
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    // Update an existing purchase order by its ID
    public PurchaseOrder updatePurchaseOrder(Long PurchaseOrderId, PurchaseOrder updatedPurchaseOrder) {
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(PurchaseOrderId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found with ID: " + PurchaseOrderId));

        String previousStatus = existingOrder.getStatus();
        String currentStatus = updatedPurchaseOrder.getStatus();

        // Update fields
        if(updatedPurchaseOrder.getShippingCost() != null) existingOrder.setShippingCost(updatedPurchaseOrder.getShippingCost());
        if(updatedPurchaseOrder.getTotalPrice() != null) existingOrder.setTotalPrice(updatedPurchaseOrder.getTotalPrice());
        if(updatedPurchaseOrder.getStatus() != null) existingOrder.setStatus(updatedPurchaseOrder.getStatus());
        if(updatedPurchaseOrder.getDate() != null)existingOrder.setDate(updatedPurchaseOrder.getDate());
        if(updatedPurchaseOrder.getPurchaseOrderNumber() != null) existingOrder.setPurchaseOrderNumber(updatedPurchaseOrder.getPurchaseOrderNumber());
        if(updatedPurchaseOrder.getTax() != null) existingOrder.setTax(updatedPurchaseOrder.getTax());

        if(updatedPurchaseOrder.getLabId() != null) existingOrder.setLabId(updatedPurchaseOrder.getLabId());
        if(updatedPurchaseOrder.getUserId() != null) existingOrder.setUserId(updatedPurchaseOrder.getUserId());
        if(updatedPurchaseOrder.getSupplierId() != null) existingOrder.setSupplierId(updatedPurchaseOrder.getSupplierId());

        PurchaseOrder purchaseOrder = purchaseOrderRepository.save(existingOrder);
        if("Completed".equalsIgnoreCase(currentStatus)
                && !"Completed".equalsIgnoreCase(previousStatus)) {
            makeLogs(purchaseOrder);
        }
        return purchaseOrder;
    }

    // Delete a purchase order by its ID
    public void deletePurchaseOrder(Long PurchaseOrderId) {
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(PurchaseOrderId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found with ID: " + PurchaseOrderId));
        purchaseOrderRepository.deleteById(PurchaseOrderId);
    }
}
