package com.project.alims.controller;

import com.project.alims.model.IncidentForm;
import com.project.alims.service.IncidentFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/incident-forms")
public class IncidentFormController {
    private final IncidentFormService incidentFormService;

    @Autowired
    public IncidentFormController(IncidentFormService incidentFormService) {
        this.incidentFormService = incidentFormService;
    }

    @PostMapping
    public ResponseEntity<IncidentForm> createIncidentForm(@RequestBody IncidentForm incidentForm) {
        IncidentForm savedIncidentForm = incidentFormService.saveIncidentForm(incidentForm);
        return ResponseEntity.ok(savedIncidentForm);
    }

    @GetMapping
    public ResponseEntity<List<IncidentForm>> getAllIncidentForms() {
        List<IncidentForm> incidentForms = incidentFormService.getAllIncidentForms();
        return ResponseEntity.ok(incidentForms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentForm> getIncidentFormById(@PathVariable Long id) {
        return incidentFormService.getIncidentFormById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentForm> updateIncidentForm(@PathVariable Long id, @RequestBody IncidentForm incidentForm) {
        IncidentForm updatedIncidentForm = incidentFormService.updateIncidentForm(id, incidentForm);
        return ResponseEntity.ok(updatedIncidentForm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncidentForm(@PathVariable Long id) {
        incidentFormService.deleteIncidentForm(id);
        return ResponseEntity.noContent().build();
    }
}
