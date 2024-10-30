package com.project.alims.service;

import com.project.alims.model.Reagents;
import com.project.alims.repository.ReagentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReagentService {

    private final ReagentRepository reagentRepository;

    @Autowired
    public ReagentService(ReagentRepository reagentRepository) {
        this.reagentRepository = reagentRepository;
    }

    public Reagents createReagent(Reagents reagent) {
        reagent.setCreatedAt(LocalDateTime.now());
        reagent.setUpdatedAt(LocalDateTime.now());
        return reagentRepository.save(reagent);
    }

    public Optional<Reagents> findById(Long id) {
        return reagentRepository.findById(id);
    }

    public List<Reagents> findAllReagents() {
        return reagentRepository.findAll();
    }

    public Reagents updateReagent(Long id, Reagents updatedReagent) {
        return reagentRepository.findById(id).map(reagent -> {
            reagent.setMaterial(updatedReagent.getMaterial());
            reagent.setQty(updatedReagent.getQty());
            reagent.setUnit(updatedReagent.getUnit());
            reagent.setLocation(updatedReagent.getLocation());
            reagent.setExpiryDate(updatedReagent.getExpiryDate());
            reagent.setCost(updatedReagent.getCost());
            reagent.setUpdatedAt(LocalDateTime.now());
            return reagentRepository.save(reagent);
        }).orElseThrow(() -> new RuntimeException("Reagent not found with ID: " + id));
    }

    public void deleteReagent(Long id) {
        reagentRepository.deleteById(id);
    }
}
