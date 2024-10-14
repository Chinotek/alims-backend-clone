package com.project.alims.controller;

import com.project.alims.model.Material;
import com.project.alims.model.Category;
import com.project.alims.model.Laboratory;
import com.project.alims.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/material")
public class MaterialController {

    private final MaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/create")
    public ResponseEntity<Material> createMaterial(@RequestBody Material material) {
        Material createdMaterial = materialService.createMaterial(material);
        return new ResponseEntity<>(createdMaterial, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = materialService.findAllMaterials();
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        Optional<Material> material = materialService.findById(id);
        return material.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Material>> getMaterialsByCategory(@PathVariable("categoryId") Category category) {
        List<Material> materials = materialService.findByCategory(category);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/lab/{labId}")
    public ResponseEntity<List<Material>> getMaterialsByLaboratory(@PathVariable("labId") Laboratory laboratory) {
        List<Material> materials = materialService.findByLaboratory(laboratory);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/find/{itemCode}")
    public ResponseEntity<Material> getMaterialByItemCode(@PathVariable String itemCode) {
        Optional<Material> material = materialService.findByItemCode(itemCode);
        return material.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable Long id, @RequestBody Material updatedMaterial) {
        try {
            Material material = materialService.updateMaterial(id, updatedMaterial);
            return new ResponseEntity<>(material, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
