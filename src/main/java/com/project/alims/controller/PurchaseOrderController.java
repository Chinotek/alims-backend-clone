package com.project.alims.controller;

import com.project.alims.model.PurchaseOrder;
import com.project.alims.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://alims-pgh.vercel.app")
@RequestMapping("/purchase-order")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    // Create a new purchase order
    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {
        PurchaseOrder createdOrder = purchaseOrderService.createPurchaseOrder(purchaseOrder);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        return ResponseEntity.ok(purchaseOrders);
    }

    // Get purchase order by ID
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.findByPurchaseOrderId(id);
        if (purchaseOrder != null) {
            return ResponseEntity.ok(purchaseOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update purchase order by ID
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(
            @PathVariable Long id,
            @RequestBody PurchaseOrder updatedPurchaseOrder) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderService.updatePurchaseOrder(id, updatedPurchaseOrder);
            return ResponseEntity.ok(purchaseOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete purchase order by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchaseOrder(@PathVariable Long id) {
        try {
            purchaseOrderService.deletePurchaseOrder(id);
            return ResponseEntity.ok("Purchase Order deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
