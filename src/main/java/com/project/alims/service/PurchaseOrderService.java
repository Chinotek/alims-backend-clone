package com.project.alims.service;

import com.project.alims.model.PurchaseOrder;
import com.project.alims.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder findByPurchaseOrderId(Long PurchaseOrderId) {
        return purchaseOrderRepository.findById(PurchaseOrderId).orElse(null);
    }

    // Create a new purchase order
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    // Update an existing purchase order by its ID
    public PurchaseOrder updatePurchaseOrder(Long PurchaseOrderId, PurchaseOrder updatedPurchaseOrder) {
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(PurchaseOrderId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found with ID: " + PurchaseOrderId));

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

        // updatedAt will be handled by @PreUpdate
        return purchaseOrderRepository.save(existingOrder);
    }

    // Delete a purchase order by its ID
    public void deletePurchaseOrder(Long PurchaseOrderId) {
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(PurchaseOrderId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found with ID: " + PurchaseOrderId));
        purchaseOrderRepository.deleteById(PurchaseOrderId);
    }
}
