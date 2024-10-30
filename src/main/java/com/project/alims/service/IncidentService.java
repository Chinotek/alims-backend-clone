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

    public Optional<Incident> getIncidentById(Long id) {
        return incidentRepository.findById(id);
    }

    public Incident updateIncident(Long id, Incident updatedIncident) {
        return incidentRepository.findById(id).map(incident -> {
            incident.setDate(updatedIncident.getDate());
            incident.setTime(updatedIncident.getTime());
            incident.setNatureOfIncident(updatedIncident.getNatureOfIncident());
            incident.setMaterialsInvolved(updatedIncident.getMaterialsInvolved());
            incident.setQty(updatedIncident.getQty());
            incident.setBrand(updatedIncident.getBrand());
            incident.setRemarks(updatedIncident.getRemarks());
            incident.setInvolvedIndividuals(updatedIncident.getInvolvedIndividuals());
            incident.setAttachments(updatedIncident.getAttachments());
            return incidentRepository.save(incident);
        }).orElseThrow(() -> new RuntimeException("Incident not found with id " + id));
    }

    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }
}
