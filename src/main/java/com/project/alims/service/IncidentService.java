package com.project.alims.service;

import com.project.alims.model.Incident;
import com.project.alims.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public Incident saveIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident findByIncidentId(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    public Incident updateIncident(Long id, Incident updatedIncident) {
        Incident existingIncident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with ID: " + id));

        existingIncident.setMaterialId(updatedIncident.getMaterialId());
        existingIncident.setMaterial(updatedIncident.getMaterial());
        existingIncident.setFormId(updatedIncident.getFormId());
        existingIncident.setIncidentForm(updatedIncident.getIncidentForm());

        existingIncident.setQty(updatedIncident.getQty());

        existingIncident.setQty(updatedIncident.getQty());
        existingIncident.setBrand(updatedIncident.getBrand());
        existingIncident.setRemarks(updatedIncident.getRemarks());
        return incidentRepository.save(existingIncident);
    }

    public void deleteIncident(Long id) {
        Incident existingIncident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with ID: " + id));
        incidentRepository.deleteById(id);
    }
}
