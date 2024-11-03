package com.project.alims.service;

import com.project.alims.model.Incident;
import com.project.alims.model.IncidentForm;
import com.project.alims.repository.IncidentFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentFormService {

    private final IncidentFormRepository incidentFormRepository;

    @Autowired
    public IncidentFormService(IncidentFormRepository incidentFormRepository) {
        this.incidentFormRepository = incidentFormRepository;
    }

    public IncidentForm saveIncidentForm(IncidentForm incidentForm) {
        return incidentFormRepository.save(incidentForm);
    }

    public List<IncidentForm> getAllIncidentForms() {
        return incidentFormRepository.findAll();
    }

    public Optional<IncidentForm> getIncidentFormById(Long id) {
        return incidentFormRepository.findById(id);
    }

    public IncidentForm updateIncidentForm(Long id, IncidentForm updatedIncidentForm) {
        IncidentForm existingIncidentForm = incidentFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident Form not found with ID: " + id));

        existingIncidentForm.setUserId(updatedIncidentForm.getUserId());
        existingIncidentForm.setUser(updatedIncidentForm.getUser());

        existingIncidentForm.setDate(updatedIncidentForm.getDate());
        existingIncidentForm.setTime(updatedIncidentForm.getTime());
        existingIncidentForm.setMaterialsInvolved(updatedIncidentForm.getMaterialsInvolved());
        existingIncidentForm.setInvolvedIndividuals(updatedIncidentForm.getInvolvedIndividuals());
        existingIncidentForm.setAttachments(updatedIncidentForm.getAttachments());
        return incidentFormRepository.save(existingIncidentForm);
    }

    public void deleteIncidentForm(Long id) {
        IncidentForm existingIncidentForm = incidentFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident Form not found with ID: " + id));
        incidentFormRepository.deleteById(id);
    }
}
