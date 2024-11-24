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

        Integer deductedAmount = 0;
        if(updatedIncident.getQty() != null && existingIncident.getQty() != null) {
            deductedAmount = updatedIncident.getQty() - existingIncident.getQty();
        } else if (updatedIncident.getQty() != null) {
            deductedAmount = updatedIncident.getQty();
        }
        Long existingMaterialId = updatedIncident.getMaterialId();
        Material existingMaterial = materialRepository.findById(existingMaterialId)
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));

       if(deductedAmount != 0) DeductQuantitytoMaterial(existingMaterial, deductedAmount);

        if(updatedIncident.getMaterialId() != null) existingIncident.setMaterialId(updatedIncident.getMaterialId());
        if(updatedIncident.getFormId() != null) existingIncident.setFormId(updatedIncident.getFormId());

        if(updatedIncident.getQty() != null) existingIncident.setQty(updatedIncident.getQty());
        if(updatedIncident.getBrand() != null) existingIncident.setBrand(updatedIncident.getBrand());
        if(updatedIncident.getRemarks() != null) existingIncident.setRemarks(updatedIncident.getRemarks());
        return incidentRepository.save(existingIncident);
    }

    public void deleteIncident(Long id) {
        Incident existingIncident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with ID: " + id));
        incidentRepository.deleteById(id);
    }
}
