package com.project.alims.service;

import com.project.alims.model.Material;
import com.project.alims.model.ReagentDispense;
import com.project.alims.repository.MaterialRepository;
import com.project.alims.repository.ReagentsDispenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReagentsDispenseService {

    private final ReagentsDispenseRepository reagentsDispenseRepository;
    private final MaterialRepository materialRepository;

    @Autowired
    public ReagentsDispenseService(ReagentsDispenseRepository reagentsDispenseRepository, MaterialRepository materialRepository) {
        this.reagentsDispenseRepository = reagentsDispenseRepository;
        this.materialRepository = materialRepository;
    }

    public List<ReagentDispense> getAllReagentsDispenses() {
        return reagentsDispenseRepository.findAll();
    }

    public ReagentDispense findByDispenseId(Long id) {
        return reagentsDispenseRepository.findById(id).orElse(null);
    }

    public Material DeductQuantitytoMaterial(Material existingReagentMaterial, Integer deductedAmount, Integer deductedContainers) {
        if (existingReagentMaterial != null) {
            Integer quantityAvailable = existingReagentMaterial.getQuantityAvailable();
            quantityAvailable -= deductedAmount;
            existingReagentMaterial.setQuantityAvailable(quantityAvailable);

            Integer containers = existingReagentMaterial.getTotalNoContainers();
            if (containers != null) {
                Integer containersRemaining = containers - deductedContainers;
                existingReagentMaterial.setTotalNoContainers(containersRemaining);
            }
            return materialRepository.save(existingReagentMaterial);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public ReagentDispense createReagentsDispense(ReagentDispense reagentDispense) {
        Integer deductedAmount = reagentDispense.getQtyDispensed();
        Integer deductedContainers = 0;
        if (reagentDispense.getTotalNoContainers() != null) deductedContainers = reagentDispense.getTotalNoContainers();
        Long reagentId = reagentDispense.getReagentId();
        Material existingReagent = materialRepository.findById(reagentId)
                .orElseThrow(() -> new RuntimeException("Reagent not found with ID: " + reagentId));
        DeductQuantitytoMaterial(existingReagent, deductedAmount, deductedContainers);

        return reagentsDispenseRepository.save(reagentDispense);
    }

    public ReagentDispense updateReagentsDispense(Long reagentDispenseId, ReagentDispense updatedReagentDispense) {
        ReagentDispense existingReagentDispense = reagentsDispenseRepository.findById(reagentDispenseId)
                .orElseThrow(() -> new RuntimeException("ReagentsDispense not found with id " + reagentDispenseId));

        Integer deductedAmount = 0; // by default do nothing
        if (updatedReagentDispense.getQtyDispensed() != null && existingReagentDispense.getQtyDispensed() != null) {
            deductedAmount = updatedReagentDispense.getQtyDispensed() - existingReagentDispense.getQtyDispensed();
        } else if (updatedReagentDispense.getQtyDispensed() != null) {
            deductedAmount = updatedReagentDispense.getQtyDispensed();
        }

        Integer deductedContainers = 0;
        if(updatedReagentDispense.getTotalNoContainers() != null && existingReagentDispense.getTotalNoContainers() != null) {
            deductedContainers = updatedReagentDispense.getTotalNoContainers() - existingReagentDispense.getTotalNoContainers();
        } else if (updatedReagentDispense.getTotalNoContainers() != null) {
            deductedContainers = updatedReagentDispense.getTotalNoContainers();
        }
        Long reagentId = updatedReagentDispense.getReagentId();
        Material existingReagent = materialRepository.findById(reagentId)
                .orElseThrow(() -> new RuntimeException("Reagent not found with ID: " + reagentId));
        if (deductedAmount != 0 && deductedContainers != 0) DeductQuantitytoMaterial(existingReagent, deductedAmount, deductedContainers);

        if(updatedReagentDispense.getName() != null) existingReagentDispense.setName(updatedReagentDispense.getName());
        if(updatedReagentDispense.getDate() != null) existingReagentDispense.setDate(updatedReagentDispense.getDate());
        if(updatedReagentDispense.getTotalNoContainers() != null) existingReagentDispense.setTotalNoContainers(updatedReagentDispense.getTotalNoContainers());
        if(updatedReagentDispense.getLotNo() != null) existingReagentDispense.setLotNo(updatedReagentDispense.getLotNo());
        if(updatedReagentDispense.getQtyDispensed() != null) existingReagentDispense.setQtyDispensed(updatedReagentDispense.getQtyDispensed());
        if(updatedReagentDispense.getRemarks() != null) existingReagentDispense.setRemarks(updatedReagentDispense.getRemarks());
        if(updatedReagentDispense.getAnalyst() != null) existingReagentDispense.setAnalyst(updatedReagentDispense.getAnalyst());

        if(updatedReagentDispense.getReagentId() != null) existingReagentDispense.setReagentId(updatedReagentDispense.getReagentId());
        if(updatedReagentDispense.getUserId() != null) existingReagentDispense.setUserId(updatedReagentDispense.getUserId());

        return reagentsDispenseRepository.save(existingReagentDispense);
    }

    public void deleteReagentsDispense(Long id) {
        reagentsDispenseRepository.deleteById(id);
    }
}
