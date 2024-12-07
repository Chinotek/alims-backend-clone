package com.project.alims.controller;

import com.project.alims.model.ReagentDispense;
import com.project.alims.service.ReagentsDispenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://alims-pgh.vercel.app")
@RequestMapping("/reagents-dispense")
public class ReagentsDispenseController {

    private final ReagentsDispenseService reagentsDispenseService;

    @Autowired
    public ReagentsDispenseController(ReagentsDispenseService reagentsDispenseService) {
        this.reagentsDispenseService = reagentsDispenseService;
    }

    @PostMapping
    public ResponseEntity<ReagentDispense> createReagentsDispense(@RequestBody ReagentDispense reagentDispense) {
        ReagentDispense savedReagentDispense = reagentsDispenseService.createReagentsDispense(reagentDispense);
        return ResponseEntity.ok(savedReagentDispense);
    }

    @GetMapping
    public ResponseEntity<List<ReagentDispense>> getAllReagentsDispenses() {
        List<ReagentDispense> reagentDispense = reagentsDispenseService.getAllReagentsDispenses();
        return ResponseEntity.ok(reagentDispense);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReagentDispense> getReagentsDispenseById(@PathVariable Long id) {
        ReagentDispense reagentDispense = reagentsDispenseService.findByDispenseId(id);
        if (reagentDispense != null) {
            return ResponseEntity.ok(reagentDispense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReagentDispense> updateReagentsDispense(@PathVariable Long id, @RequestBody ReagentDispense updatedreagentsDispense) {
        try {
            ReagentDispense reagentDispense = reagentsDispenseService.updateReagentsDispense(id, updatedreagentsDispense);
            return ResponseEntity.ok(reagentDispense);
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
