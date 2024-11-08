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

    public Material DeductQuantitytoMaterial(Material existingReagentMaterial, Integer deductedAmount) {
        if (existingReagentMaterial != null) {
            Integer quantityAvailable = existingReagentMaterial.getQuantityAvailable();
            quantityAvailable -= deductedAmount;
            existingReagentMaterial.setQuantityAvailable(quantityAvailable);
            return materialRepository.save(existingReagentMaterial);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public ReagentDispense createReagentsDispense(ReagentDispense reagentDispense) {
        Integer deductedAmount = reagentDispense.getQtyDispensed();
        Long reagentId = reagentDispense.getReagentId();
        Material existingReagent = materialRepository.findById(reagentId)
                .orElseThrow(() -> new RuntimeException("Reagent not found with ID: " + reagentId));
        DeductQuantitytoMaterial(existingReagent, deductedAmount);

        return reagentsDispenseRepository.save(reagentDispense);
    }

    public ReagentDispense updateReagentsDispense(Long reagentDispenseId, ReagentDispense updatedReagentDispense) {
        ReagentDispense existingReagentDispense = reagentsDispenseRepository.findById(reagentDispenseId)
                .orElseThrow(() -> new RuntimeException("ReagentsDispense not found with id " + reagentDispenseId));

        Integer deductedAmount = updatedReagentDispense.getQtyDispensed() - existingReagentDispense.getQtyDispensed();
        Long reagentId = updatedReagentDispense.getReagentId();
        Material existingReagent = materialRepository.findById(reagentId)
                .orElseThrow(() -> new RuntimeException("Reagent not found with ID: " + reagentId));
        DeductQuantitytoMaterial(existingReagent, deductedAmount);

        existingReagentDispense.setName(updatedReagentDispense.getName());
        existingReagentDispense.setDate(updatedReagentDispense.getDate());
        existingReagentDispense.setTotalNoContainers(updatedReagentDispense.getTotalNoContainers());
        existingReagentDispense.setLotNo(updatedReagentDispense.getLotNo());
        existingReagentDispense.setQtyDispensed(updatedReagentDispense.getQtyDispensed());
        existingReagentDispense.setRemarks(updatedReagentDispense.getRemarks());
        existingReagentDispense.setAnalyst(updatedReagentDispense.getAnalyst());

        existingReagentDispense.setReagentId(updatedReagentDispense.getReagentId());
        existingReagentDispense.setReagent(updatedReagentDispense.getReagent());

        existingReagentDispense.setUserId(updatedReagentDispense.getUserId());
        existingReagentDispense.setUser(updatedReagentDispense.getUser());

        return reagentsDispenseRepository.save(existingReagentDispense);
    }

    public void deleteReagentsDispense(Long id) {
        reagentsDispenseRepository.deleteById(id);
    }
}
