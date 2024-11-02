package com.project.alims.service;

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
        return incidentFormRepository.findById(id).map(incidentForm -> {
            incidentForm.setDate(updatedIncidentForm.getDate());
            incidentForm.setTime(updatedIncidentForm.getTime());
            incidentForm.setMaterialsInvolved(updatedIncidentForm.getMaterialsInvolved());
            incidentForm.setInvolvedIndividuals(updatedIncidentForm.getInvolvedIndividuals());
            incidentForm.setAttachments(updatedIncidentForm.getAttachments());
            return incidentFormRepository.save(incidentForm);
        }).orElseThrow(() -> new RuntimeException("IncidentForm not found with id " + id));
    }

    public void deleteIncidentForm(Long id) {
        incidentFormRepository.deleteById(id);
    }
}
