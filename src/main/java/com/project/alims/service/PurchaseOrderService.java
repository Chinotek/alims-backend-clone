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

    public PurchaseOrder findById(Long poId) {
        return purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found with ID: " + poId));
    }

    // Create a new purchase order
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    // Update an existing purchase order by its ID
    public PurchaseOrder updatePurchaseOrder(Long poId, PurchaseOrder updatedPurchaseOrder) {
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found with ID: " + poId));

        // Update fields
        existingOrder.setItemCode(updatedPurchaseOrder.getItemCode());
        existingOrder.setQty(updatedPurchaseOrder.getQty());
        existingOrder.setDescription(updatedPurchaseOrder.getDescription());
        existingOrder.setUnitPrice(updatedPurchaseOrder.getUnitPrice());
        existingOrder.setShippingCost(updatedPurchaseOrder.getShippingCost());
        existingOrder.setTotalPrice(updatedPurchaseOrder.getTotalPrice());
        existingOrder.setStatus(updatedPurchaseOrder.getStatus());
        existingOrder.setLaboratory(updatedPurchaseOrder.getLaboratory());
        existingOrder.setUser(updatedPurchaseOrder.getUser());
        existingOrder.setSupplier(updatedPurchaseOrder.getSupplier());

        // updatedAt will be handled by @PreUpdate
        return purchaseOrderRepository.save(existingOrder);
    }

    // Delete a purchase order by its ID
    public void deletePurchaseOrder(Long poId) {
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found with ID: " + poId));
        purchaseOrderRepository.deleteById(poId);
    }
}
