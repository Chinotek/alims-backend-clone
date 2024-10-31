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

    public Purchase findById(Long poId) {
        return purchaseRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + poId));
    }

    // Create a new purchase order
    public Purchase createPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    // Update an existing purchase order by its ID
    public Purchase updatePurchase(Long poId, Purchase updatedPurchase) {
        Purchase existingOrder = purchaseRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + poId));

        // Update fields
        existingOrder.setMaterial(updatedPurchase.getMaterial());
        existingOrder.setQty(updatedPurchase.getQty());
        existingOrder.setDescription(updatedPurchase.getDescription());
        existingOrder.setUnitPrice(updatedPurchase.getUnitPrice());

        // updatedAt will be handled by @PreUpdate
        return purchaseRepository.save(existingOrder);
    }

    // Delete a purchase order by its ID
    public void deletePurchase(Long poId) {
        Purchase existingOrder = purchaseRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("Purchase not found with ID: " + poId));
        purchaseRepository.deleteById(poId);
    }
}
