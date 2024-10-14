package com.project.alims.controller;

import com.project.alims.model.DisposalForm;
import com.project.alims.service.DisposalFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/disposal-form")
public class DisposalFormController {

    private final DisposalFormService disposalFormService;

    @Autowired
    public DisposalFormController(DisposalFormService disposalFormService) {
        this.disposalFormService = disposalFormService;
    }

    @PostMapping("/create")
    public ResponseEntity<DisposalForm> createDisposalForm(@RequestBody DisposalForm disposalForm) {
        DisposalForm createdForm = disposalFormService.createDisposalForm(disposalForm);
        return ResponseEntity.ok(createdForm);
    }

    // Get all DisposalForms
    @GetMapping("/all")
    public ResponseEntity<List<DisposalForm>> getAllDisposalForms() {
        List<DisposalForm> forms = disposalFormService.findAllDisposalForms();
        return ResponseEntity.ok(forms);
    }

    // Get DisposalForm by incidentId
    @GetMapping("/{id}")
    public ResponseEntity<DisposalForm> getDisposalFormById(@PathVariable Long id) {
        DisposalForm form = disposalFormService.findDisposalFormById(id);
        if (form != null) {
            return ResponseEntity.ok(form);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get DisposalForms by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DisposalForm>> getDisposalFormsByUserId(@PathVariable Long userId) {
        List<DisposalForm> forms = disposalFormService.findDisposalFormsByUserId(userId);
        return ResponseEntity.ok(forms);
    }

    // Get DisposalForms by itemCode (Material)
    @GetMapping("/material/{itemCode}")
    public ResponseEntity<List<DisposalForm>> getDisposalFormsByItemCode(@PathVariable String itemCode) {
        List<DisposalForm> forms = disposalFormService.findDisposalFormsByItemCode(itemCode);
        return ResponseEntity.ok(forms);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DisposalForm> updateDisposalForm(@PathVariable Long id, @RequestBody DisposalForm updatedDisposalForm) {
        try {
            DisposalForm disposalForm = disposalFormService.updateDisposalForm(id, updatedDisposalForm);
            return ResponseEntity.ok(disposalForm);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDisposalForm(@PathVariable Long id) {
        try {
            disposalFormService.deleteDisposalForm(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}