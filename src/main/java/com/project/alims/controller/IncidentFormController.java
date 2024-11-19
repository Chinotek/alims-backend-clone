package com.project.alims.controller;

import com.project.alims.model.CalibrationLog;
import com.project.alims.model.IncidentForm;
import com.project.alims.service.IncidentFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    // Download
    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> downloadIncidentFormFile(@PathVariable Long id) {
        IncidentForm incidentForm = incidentFormService.getIncidentFormById(id).orElse(null);
        if (incidentForm == null) throw new RuntimeException("Incident Form not found");
        byte[] bytes = incidentForm.getFile();
        if (bytes == null) throw new RuntimeException("File not found");
        ByteArrayResource file = new ByteArrayResource(bytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(incidentForm.getAttachments()).build().toString())
                .body(file);
    }

    @PostMapping
    public ResponseEntity<IncidentForm> createIncidentForm(@RequestPart("body") IncidentForm incidentForm,
                                                           @RequestPart("file") MultipartFile file) throws IOException {
        IncidentForm savedIncidentForm = incidentFormService.saveIncidentForm(incidentForm, file);
        return ResponseEntity.ok(savedIncidentForm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentForm> updateIncidentForm(@PathVariable Long id,
                                                           @RequestPart("body") IncidentForm incidentForm,
                                                           @RequestPart("file") MultipartFile file) throws IOException {
        IncidentForm updatedIncidentForm = incidentFormService.updateIncidentForm(id, incidentForm, file);
        return ResponseEntity.ok(updatedIncidentForm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncidentForm(@PathVariable Long id) {
        incidentFormService.deleteIncidentForm(id);
        return ResponseEntity.noContent().build();
    }
}
