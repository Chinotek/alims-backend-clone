package com.project.alims.controller;


import com.project.alims.model.Laboratory;
import com.project.alims.model.Supplier;
import com.project.alims.service.LaboratoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"https://alims-pgh.vercel.app", "http://alims-backend-production.up.railway.app"})
@RequestMapping("/lab")
public class LaboratoryController {
    private final LaboratoryService laboratoryService;

    @Autowired
    public LaboratoryController(LaboratoryService laboratoryService){
        this.laboratoryService = laboratoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<Laboratory> createLaboratory(@RequestBody Laboratory laboratory) {
        Laboratory createdLab = laboratoryService.createLaboratory(laboratory);
        return ResponseEntity.ok(createdLab);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laboratory> getLaboratoryById(@PathVariable Long id) {
        Laboratory laboratory = laboratoryService.findById(id);
        if (laboratory != null) {
            return ResponseEntity.ok(laboratory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Laboratory>> getAllLaboratories() {
        List<Laboratory> labs = laboratoryService.getAllLaboratories();
        return ResponseEntity.ok(labs);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Laboratory> updateLaboratory(
            @PathVariable Long id,
            @RequestBody Laboratory updatedLaboratory) {
        try {
            Laboratory laboratory = laboratoryService.updateLaboratory(id, updatedLaboratory);
            return ResponseEntity.ok(laboratory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete Laboratory
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLaboratory(@PathVariable Long id) {
        try {
            laboratoryService.deleteLaboratory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }




}
