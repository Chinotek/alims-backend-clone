package com.project.alims.service;

import com.project.alims.model.Incident;
import com.project.alims.model.IncidentForm;
import com.project.alims.model.InventoryLog;
import com.project.alims.model.Material;
import com.project.alims.repository.IncidentFormRepository;
import com.project.alims.repository.IncidentRepository;
import com.project.alims.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentFormRepository incidentFormRepository;
    private final MaterialRepository materialRepository;
    private final InventoryLogService inventoryLogService;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository, IncidentFormRepository incidentFormRepository, MaterialRepository materialRepository, InventoryLogService inventoryLogService) {
        this.incidentRepository = incidentRepository;
        this.incidentFormRepository = incidentFormRepository;
        this.materialRepository = materialRepository;
        this.inventoryLogService = inventoryLogService;
    }


    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident findByIncidentId(Long id) {
        return incidentRepository.findById(id).orElse(null);
    }

    public InventoryLog DeductQuantitytoMaterial(Material existingIncidentMaterial, Incident incident, IncidentForm incidentForm, Integer deductedAmount) {
        if (existingIncidentMaterial != null) {
            InventoryLog inventoryLog = new InventoryLog(
                    incidentForm.getUserId(),
                    existingIncidentMaterial.getMaterialId(),
                    LocalDate.now(),
                    -deductedAmount,
                    "IncidentForm: " + incidentForm.getIncidentFormId() + " Incident: "  + incident.getIncidentId(),
                    "Incident: " + deductedAmount + " " + existingIncidentMaterial.getUnit() + " of " +
                            existingIncidentMaterial.getItemName() + " on " + incidentForm.getDate()
            );
            return inventoryLogService.createInventoryLog(inventoryLog);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public Incident createIncident(Incident incident) {
        Integer deductedAmount = incident.getQty();
        Long existingMaterialId = incident.getMaterialId();
        Material existingMaterial = materialRepository.findById(existingMaterialId)
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));
        Long existingIncidentFormId = incident.getFormId();
        IncidentForm existingIncidentForm = incidentFormRepository.findById(existingIncidentFormId)
                .orElseThrow(() -> new RuntimeException("Incident Form not found with ID: " + existingIncidentFormId));

        Incident createdIncident = incidentRepository.save(incident);
        DeductQuantitytoMaterial(existingMaterial, incident, existingIncidentForm, deductedAmount);
        return createdIncident;
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
        // qty not updated - updated qty not null - deducted amount = 0

        Long existingMaterialId = existingIncident.getMaterialId();
        if(updatedIncident.getMaterialId() != null) existingMaterialId = updatedIncident.getMaterialId();
        Material existingMaterial = materialRepository.findById(existingMaterialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));
        Long existingIncidentFormId = existingIncident.getFormId();
        if(updatedIncident.getFormId() != null) existingIncidentFormId = updatedIncident.getFormId();
        IncidentForm existingIncidentForm = incidentFormRepository.findById(existingIncidentFormId)
                .orElseThrow(() -> new RuntimeException("Incident Form not found"));

       if(deductedAmount != 0) DeductQuantitytoMaterial(existingMaterial, existingIncident, existingIncidentForm, deductedAmount);

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
