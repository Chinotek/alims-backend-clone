package com.project.alims.controller;

import com.project.alims.model.Material;
import com.project.alims.model.Category;
import com.project.alims.model.Supplier;
import com.project.alims.service.MaterialService;
import com.project.alims.service.CategoryService;
import com.project.alims.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;


    @PostMapping("/create")
    public ResponseEntity<Material> createMaterial(@RequestBody Material material) {
        Material createdMaterial = materialService.createMaterial(material);
        return new ResponseEntity<>(createdMaterial, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = materialService.findAllMaterials();
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        Material material = materialService.findById(id);
        if (material != null) {
            return new ResponseEntity<>(material, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Material>> getMaterialsByCategory(@PathVariable Long categoryId) {
        Optional<Category> category = categoryService.findById(categoryId);  // Find Category object
        if (category.isPresent()) {
            List<Material> materials = materialService.findByCategory(category.get());
            return new ResponseEntity<>(materials, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<Material>> getMaterialsBySupplier(@PathVariable Long supplierId) {
        Optional<Supplier> supplier = supplierService.getSupplierById(supplierId);
        if (supplier.isPresent()) {
            List<Material> materials = materialService.findBySupplier(supplier.get());
            return new ResponseEntity<>(materials, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
