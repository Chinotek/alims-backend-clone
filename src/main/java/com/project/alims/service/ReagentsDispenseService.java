package com.project.alims.service;

import com.project.alims.model.ReagentsDispense;
import com.project.alims.repository.ReagentsDispenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReagentsDispenseService {

    private final ReagentsDispenseRepository reagentsDispenseRepository;

    @Autowired
    public ReagentsDispenseService(ReagentsDispenseRepository reagentsDispenseRepository) {
        this.reagentsDispenseRepository = reagentsDispenseRepository;
    }

    public ReagentsDispense saveReagentsDispense(ReagentsDispense reagentsDispense) {
        return reagentsDispenseRepository.save(reagentsDispense);
    }

    public List<ReagentsDispense> getAllReagentsDispenses() {
        return reagentsDispenseRepository.findAll();
    }

    public ReagentsDispense findByDispenseId(Long id) {
        return reagentsDispenseRepository.findById(id).orElse(null);
    }

    public ReagentsDispense updateReagentsDispense(Long id, ReagentsDispense updatedReagentsDispense) {
        return reagentsDispenseRepository.findById(id).map(reagentsDispense -> {
            reagentsDispense.setName(updatedReagentsDispense.getName());
            reagentsDispense.setDate(updatedReagentsDispense.getDate());
            reagentsDispense.setTotalNoContainers(updatedReagentsDispense.getTotalNoContainers());
            reagentsDispense.setLotNo(updatedReagentsDispense.getLotNo());
            reagentsDispense.setQtyDispensed(updatedReagentsDispense.getQtyDispensed());
            reagentsDispense.setRemainingQty(updatedReagentsDispense.getRemainingQty());
            reagentsDispense.setRemarks(updatedReagentsDispense.getRemarks());
            reagentsDispense.setAnalyst(updatedReagentsDispense.getAnalyst());
            return reagentsDispenseRepository.save(reagentsDispense);
        }).orElseThrow(() -> new RuntimeException("ReagentsDispense not found with id " + id));
    }

    public void deleteReagentsDispense(Long id) {
        reagentsDispenseRepository.deleteById(id);
    }
}
