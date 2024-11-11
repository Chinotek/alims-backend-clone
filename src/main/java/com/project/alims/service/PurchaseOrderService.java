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
        existingOrder.setShippingCost(updatedPurchaseOrder.getShippingCost());
        existingOrder.setTotalPrice(updatedPurchaseOrder.getTotalPrice());
        existingOrder.setStatus(updatedPurchaseOrder.getStatus());
        existingOrder.setDate(updatedPurchaseOrder.getDate());
        existingOrder.setPurchaseOrderNumber(updatedPurchaseOrder.getPurchaseOrderNumber());

        existingOrder.setLabId(updatedPurchaseOrder.getLabId());
        existingOrder.setUser(updatedPurchaseOrder.getUser());
        existingOrder.setSupplierId(updatedPurchaseOrder.getSupplierId());

        existingOrder.setLaboratory(updatedPurchaseOrder.getLaboratory());
        existingOrder.setUser(updatedPurchaseOrder.getUser());
        existingOrder.setSupplier(updatedPurchaseOrder.getSupplier());

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
