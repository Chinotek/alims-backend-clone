package com.project.alims.service;

import com.project.alims.model.InventoryLog;
import com.project.alims.model.Material;
import com.project.alims.model.ReagentDispense;
import com.project.alims.repository.MaterialRepository;
import com.project.alims.repository.ReagentsDispenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Math.abs;

@Service
public class ReagentsDispenseService {

    private final ReagentsDispenseRepository reagentsDispenseRepository;
    private final MaterialRepository materialRepository;
    private final InventoryLogService inventoryLogService;

    @Autowired
    public ReagentsDispenseService(ReagentsDispenseRepository reagentsDispenseRepository, MaterialRepository materialRepository, InventoryLogService inventoryLogService) {
        this.reagentsDispenseRepository = reagentsDispenseRepository;
        this.materialRepository = materialRepository;
        this.inventoryLogService = inventoryLogService;
    }

    public List<ReagentDispense> getAllReagentsDispenses() {
        return reagentsDispenseRepository.findAll();
    }

    public ReagentDispense findByDispenseId(Long id) {
        return reagentsDispenseRepository.findById(id).orElse(null);
    }

    public InventoryLog DeductQuantitytoMaterial(Material existingReagentMaterial, ReagentDispense reagentDispense, Integer deductedAmount) {
        if (existingReagentMaterial != null) {

            Integer deductedContainers = (int) Math.ceil((double) deductedAmount / existingReagentMaterial.getQtyPerContainer());

            InventoryLog inventoryLog = new InventoryLog(
                    reagentDispense.getUserId(),
                    existingReagentMaterial.getMaterialId(),
                    LocalDate.now(),
                    -deductedAmount,
                    "ReagentDispense: " + reagentDispense.getDispenseId(),
                    "Dispensed " + deductedAmount + " " + existingReagentMaterial.getUnit() + " of " +
                            existingReagentMaterial.getItemName() + " and " + deductedContainers + " containers on " + reagentDispense.getDate()
            );

            // containers handled by Inventory Log
            return inventoryLogService.createInventoryLog(inventoryLog);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public ReagentDispense createReagentsDispense(ReagentDispense reagentDispense) {
        Integer deductedAmount = reagentDispense.getQtyDispensed();

        Long reagentId = reagentDispense.getReagentId();
        Material existingReagent = materialRepository.findById(reagentId)
                .orElseThrow(() -> new RuntimeException("Reagent not found with ID: " + reagentId));

        if (existingReagent.getQtyPerContainer() != null
                && existingReagent.getTotalNoContainers() != null) {
            // never negative, should be fine
            Integer deductedContainers = (int) Math.ceil((double) deductedAmount / existingReagent.getQtyPerContainer());
            // get containers to add/reduce based on amt
            reagentDispense.setTotalNoContainers(deductedContainers);
        }

        ReagentDispense createdReagentDispense = reagentsDispenseRepository.save(reagentDispense);
        DeductQuantitytoMaterial(existingReagent, reagentDispense, deductedAmount);
        return createdReagentDispense;
    }

    public ReagentDispense updateReagentsDispense(Long reagentDispenseId, ReagentDispense updatedReagentDispense) {
        ReagentDispense existingReagentDispense = reagentsDispenseRepository.findById(reagentDispenseId)
                .orElseThrow(() -> new RuntimeException("ReagentsDispense not found with id " + reagentDispenseId));

        Long reagentId = existingReagentDispense.getReagentId();
        if (updatedReagentDispense.getReagentId() != null) reagentId = updatedReagentDispense.getReagentId();
        Material existingReagent = materialRepository.findById(reagentId)
                .orElseThrow(() -> new RuntimeException("Reagent not found"));

        Integer deductedAmount = 0; // by default do nothing : updatedReagent.qty = null
        Integer deductedContainers = 0;
        if (updatedReagentDispense.getQtyDispensed() != null && existingReagentDispense.getQtyDispensed() != null) {
            deductedAmount = updatedReagentDispense.getQtyDispensed() - existingReagentDispense.getQtyDispensed();
            deductedContainers = (int) Math.ceil((double) deductedAmount / existingReagent.getQtyPerContainer());
        } else if (updatedReagentDispense.getQtyDispensed() != null) {
            deductedAmount = updatedReagentDispense.getQtyDispensed();
            deductedContainers = (int) Math.ceil((double) deductedAmount / existingReagent.getQtyPerContainer());
        }

        if(updatedReagentDispense.getName() != null) existingReagentDispense.setName(updatedReagentDispense.getName());
        if(updatedReagentDispense.getDate() != null) existingReagentDispense.setDate(updatedReagentDispense.getDate());
        if(updatedReagentDispense.getLotNo() != null) existingReagentDispense.setLotNo(updatedReagentDispense.getLotNo());
        if(updatedReagentDispense.getRemarks() != null) existingReagentDispense.setRemarks(updatedReagentDispense.getRemarks());
        if(updatedReagentDispense.getAnalyst() != null) existingReagentDispense.setAnalyst(updatedReagentDispense.getAnalyst());

        if(updatedReagentDispense.getQtyDispensed() != null) {
            existingReagentDispense.setQtyDispensed(updatedReagentDispense.getQtyDispensed());
            existingReagentDispense.setTotalNoContainers(deductedContainers + existingReagentDispense.getTotalNoContainers());
        }

        if(updatedReagentDispense.getReagentId() != null) existingReagentDispense.setReagentId(updatedReagentDispense.getReagentId());
        if(updatedReagentDispense.getUserId() != null) existingReagentDispense.setUserId(updatedReagentDispense.getUserId());

        ReagentDispense reagentDispense = reagentsDispenseRepository.save(existingReagentDispense);
        if (deductedAmount != 0) DeductQuantitytoMaterial(existingReagent, reagentDispense, deductedAmount);
        return reagentDispense;
    }

    public void deleteReagentsDispense(Long id) {
        reagentsDispenseRepository.deleteById(id);
    }
}
