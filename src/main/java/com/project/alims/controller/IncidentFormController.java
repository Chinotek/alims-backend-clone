package com.project.alims.controller;

import com.project.alims.model.CalibrationLog;
import com.project.alims.model.IncidentForm;
import com.project.alims.service.IncidentFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://alims-pgh.vercel.app")
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

    @GetMapping("/file/{id}/{fileIndex}")
    public ResponseEntity<Resource> downloadIncidentFormFile(@PathVariable Long id, @PathVariable int fileIndex) {
        // Fetch the incident form from the repository
        IncidentForm incidentForm = incidentFormService.getIncidentFormById(id)
                .orElseThrow(() -> new RuntimeException("Incident Form not found"));

        // Get the list of files (bytes) stored in the incident form
        List<byte[]> files = incidentForm.getFiles();
        if (files == null || files.isEmpty()) {
            throw new RuntimeException("No files associated with this incident form.");
        }

        // Ensure the requested file index is valid
        if (fileIndex < 0 || fileIndex >= files.size()) {
            throw new RuntimeException("Invalid file index.");
        }

        // Get the file bytes based on the index
        byte[] fileBytes = files.get(fileIndex);

        // Get the corresponding file name
        String[] filenames = incidentForm.getAttachments().split(","); // assuming file names are stored in a comma-separated list
        if (filenames.length <= fileIndex) {
            throw new RuntimeException("File name for the selected index not found.");
        }

        String fileName = filenames[fileIndex];

        // Create a resource from the file bytes
        ByteArrayResource resource = new ByteArrayResource(fileBytes);

        // Return the file as a response entity with proper headers for download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(fileName).build().toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping
    public ResponseEntity<IncidentForm> createIncidentForm(
            @RequestPart("body") IncidentForm incidentForm,
            @RequestPart("files") List<MultipartFile> files) throws IOException {
        IncidentForm savedIncidentForm = incidentFormService.saveIncidentForm(incidentForm, files);
        return ResponseEntity.ok(savedIncidentForm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentForm> updateIncidentForm(@PathVariable Long id,
                                                           @RequestPart("body") IncidentForm incidentForm,
                                                           @RequestPart("files") List<MultipartFile> files) throws IOException {
        IncidentForm updatedIncidentForm = incidentFormService.updateIncidentForm(id, incidentForm, files);
        return ResponseEntity.ok(updatedIncidentForm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncidentForm(@PathVariable Long id) {
        incidentFormService.deleteIncidentForm(id);
        return ResponseEntity.noContent().build();
    }
}
