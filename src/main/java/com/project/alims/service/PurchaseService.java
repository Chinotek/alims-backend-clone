package com.project.alims.service;

import com.project.alims.model.Purchase;
import com.project.alims.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase findByPurchaseId(Long PurchaseId) {
        return purchaseRepository.findById(PurchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + PurchaseId));
    }

    // Create a new purchase order
    public Purchase createPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    // Update an existing purchase order by its ID
    public Purchase updatePurchase(Long PurchaseId, Purchase updatedPurchase) {
        Purchase existingPurchase = purchaseRepository.findById(PurchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + PurchaseId));

        // Update fields
        if(updatedPurchase.getPurchaseOrderId() != null) existingPurchase.setPurchaseOrderId(updatedPurchase.getPurchaseOrderId());
        if(updatedPurchase.getMaterialId() != null) existingPurchase.setMaterialId(updatedPurchase.getMaterialId());

        if(updatedPurchase.getQty() != null) existingPurchase.setQty(updatedPurchase.getQty());
        if(updatedPurchase.getDescription() != null) existingPurchase.setDescription(updatedPurchase.getDescription());
        if(updatedPurchase.getUnitPrice() != null) existingPurchase.setUnitPrice(updatedPurchase.getUnitPrice());

        // updatedAt will be handled by @PreUpdate
        return purchaseRepository.save(existingPurchase);
    }

    // Delete a purchase order by its ID
    public void deletePurchase(Long PurchaseId) {
        Purchase existingOrder = purchaseRepository.findById(PurchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + PurchaseId));
        purchaseRepository.deleteById(PurchaseId);
    }
}
