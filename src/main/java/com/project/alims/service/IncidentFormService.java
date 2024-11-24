package com.project.alims.service;

import com.project.alims.model.IncidentForm;
import com.project.alims.repository.IncidentFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentFormService {

    private final IncidentFormRepository incidentFormRepository;

    @Autowired
    public IncidentFormService(IncidentFormRepository incidentFormRepository) {
        this.incidentFormRepository = incidentFormRepository;
    }

    public IncidentForm saveIncidentForm(IncidentForm incidentForm, MultipartFile file) throws IOException {
        if (file.getSize() > 0) {
            incidentForm.setFile(file.getBytes());
            incidentForm.setAttachments(file.getOriginalFilename());
            incidentForm.setFileType(file.getContentType());
        }
        return incidentFormRepository.save(incidentForm);
    }

    public List<IncidentForm> getAllIncidentForms() {
        return incidentFormRepository.findAll();
    }

    public Optional<IncidentForm> getIncidentFormById(Long id) {
        return incidentFormRepository.findById(id);
    }

    public IncidentForm updateIncidentForm(Long id, IncidentForm updatedIncidentForm, MultipartFile file) throws IOException {
        IncidentForm existingIncidentForm = incidentFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident Form not found with ID: " + id));

        if(updatedIncidentForm.getUserId() != null) existingIncidentForm.setUserId(updatedIncidentForm.getUserId());

        if(updatedIncidentForm.getDate() != null) existingIncidentForm.setDate(updatedIncidentForm.getDate());
        if(updatedIncidentForm.getTime() != null) existingIncidentForm.setTime(updatedIncidentForm.getTime());
        if(updatedIncidentForm.getMaterialsInvolved() != null) existingIncidentForm.setMaterialsInvolved(updatedIncidentForm.getMaterialsInvolved());
        if(updatedIncidentForm.getInvolvedIndividuals() != null) existingIncidentForm.setInvolvedIndividuals(updatedIncidentForm.getInvolvedIndividuals());

        if (file.getSize() > 0) {
            existingIncidentForm.setFile(file.getBytes());
            existingIncidentForm.setAttachments(file.getOriginalFilename());
            existingIncidentForm.setFileType(file.getContentType());
        }

        return incidentFormRepository.save(existingIncidentForm);
    }

    public void deleteIncidentForm(Long id) {
        IncidentForm existingIncidentForm = incidentFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident Form not found with ID: " + id));
        incidentFormRepository.deleteById(id);
    }
}
