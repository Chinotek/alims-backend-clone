package com.project.alims.service;

import com.project.alims.model.Laboratory;
import com.project.alims.repository.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaboratoryService {
    private final LaboratoryRepository laboratoryRepository;

    @Autowired
    public LaboratoryService(LaboratoryRepository laboratoryRepository){
        this.laboratoryRepository = laboratoryRepository;
    }

    public Laboratory findById(Long labId){
        return laboratoryRepository.findByLabId(labId);
    }

    public Laboratory createLaboratory(Laboratory laboratory) {
        return laboratoryRepository.save(laboratory);
    }

    public Laboratory updateLaboratory(Long labId, Laboratory updatedLaboratory) {
        Laboratory lab = laboratoryRepository.findByLabId(labId);
        if (lab != null) {
            if(updatedLaboratory.getLabName() != null) lab.setLabName(updatedLaboratory.getLabName());
            if(updatedLaboratory.getLocation() != null) lab.setLocation(updatedLaboratory.getLocation());
            return laboratoryRepository.save(lab);
        } else {
            throw new RuntimeException("Laboratory not found with ID: " + labId);
        }
    }

    public void deleteLaboratory(Long labId) {
        Laboratory lab = laboratoryRepository.findByLabId(labId);
        if (lab != null) {
            laboratoryRepository.deleteById(labId);
        } else {
            throw new RuntimeException("Laboratory not found with ID: " + labId);
        }
    }
}
