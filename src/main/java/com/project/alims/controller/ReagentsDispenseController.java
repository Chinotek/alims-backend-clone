package com.project.alims.controller;

import com.project.alims.model.PurchaseOrder;
import com.project.alims.model.ReagentsDispense;
import com.project.alims.service.ReagentsDispenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/reagents_dispense")
public class ReagentsDispenseController {

    private final ReagentsDispenseService reagentsDispenseService;

    @Autowired
    public ReagentsDispenseController(ReagentsDispenseService reagentsDispenseService) {
        this.reagentsDispenseService = reagentsDispenseService;
    }

    @PostMapping
    public ResponseEntity<ReagentsDispense> createReagentsDispense(@RequestBody ReagentsDispense reagentsDispense) {
        ReagentsDispense savedReagentsDispense = reagentsDispenseService.saveReagentsDispense(reagentsDispense);
        return ResponseEntity.ok(savedReagentsDispense);
    }

    @GetMapping
    public ResponseEntity<List<ReagentsDispense>> getAllReagentsDispenses() {
        List<ReagentsDispense> reagentsDispenses = reagentsDispenseService.getAllReagentsDispenses();
        return ResponseEntity.ok(reagentsDispenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReagentsDispense> getReagentsDispenseById(@PathVariable Long id) {
        ReagentsDispense reagentsDispense = reagentsDispenseService.findByDispenseId(id);
        if (reagentsDispense != null) {
            return ResponseEntity.ok(reagentsDispense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReagentsDispense> updateReagentsDispense(@PathVariable Long id, @RequestBody ReagentsDispense updatedreagentsDispense) {
        try {
            ReagentsDispense reagentsDispense = reagentsDispenseService.updateReagentsDispense(id, updatedreagentsDispense);
            return ResponseEntity.ok(reagentsDispense);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReagentsDispense(@PathVariable Long id) {
        try {
            reagentsDispenseService.deleteReagentsDispense(id);
            return ResponseEntity.ok("Reagent Dispense deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
