package com.project.alims.service;

import com.project.alims.model.Incident;
import com.project.alims.model.Material;
import com.project.alims.repository.IncidentRepository;
import com.project.alims.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final MaterialRepository materialRepository;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository, MaterialRepository materialRepository) {
        this.incidentRepository = incidentRepository;
        this.materialRepository = materialRepository;
    }


    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident findByIncidentId(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    public Material DeductQuantitytoMaterial(Material existingIncidentMaterial, Integer deductedAmount) {
        if (existingIncidentMaterial != null) {
            Integer quantityAvailable = existingIncidentMaterial.getQuantityAvailable();
            quantityAvailable -= deductedAmount;
            existingIncidentMaterial.setQuantityAvailable(quantityAvailable);
            return materialRepository.save(existingIncidentMaterial);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public Incident createIncident(Incident incident) {
        Integer deductedAmount = incident.getQty();
        Long existingMaterialId = incident.getMaterialId();
        Material existingMaterial = materialRepository.findById(existingMaterialId)
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));

        DeductQuantitytoMaterial(existingMaterial, deductedAmount);
        return incidentRepository.save(incident);
    }

    public Incident updateIncident(Long id, Incident updatedIncident) {
        Incident existingIncident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with ID: " + id));

        Integer deductedAmount = updatedIncident.getQty() - existingIncident.getQty();
        Material existingDisposalFormMaterial = existingIncident.getMaterial();

        DeductQuantitytoMaterial(existingDisposalFormMaterial, deductedAmount);

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
