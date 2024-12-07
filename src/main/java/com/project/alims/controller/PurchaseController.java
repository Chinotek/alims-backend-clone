package com.project.alims.controller;

import com.project.alims.model.Purchase;
import com.project.alims.service.PurchaseService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://alims-pgh.vercel.app")
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    // Create a new purchase order
    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) {
        Purchase createdOrder = purchaseService.createPurchase(purchase);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        List<Purchase> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/order/{purchaseOrderId}")
    public ResponseEntity<List<Purchase>> getAllPurchasesByPurchaseOrder(@PathVariable Long purchaseOrderId) {
        List<Purchase> purchases = purchaseService.getAllPurchasesByPurchaseOrderId(purchaseOrderId);
        return ResponseEntity.ok(purchases);
    }

    // Get purchase order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        Purchase purchase = purchaseService.findByPurchaseId(id);
        if (purchase != null) {
            return ResponseEntity.ok(purchase);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update purchase order by ID
    @PutMapping("/{id}")
    public ResponseEntity<Purchase> updatePurchase(
            @PathVariable Long id,
            @RequestBody Purchase updatedPurchase) {
        try {
            Purchase purchase = purchaseService.updatePurchase(id, updatedPurchase);
            return ResponseEntity.ok(purchase);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete purchase order by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        try {
            purchaseService.deletePurchase(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
