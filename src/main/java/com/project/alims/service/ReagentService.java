package com.project.alims.service;

import com.project.alims.model.Reagent;
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

    public Reagent createReagent(Reagent reagent) {
        return reagentRepository.save(reagent);
    }

    public Optional<Reagent> findById(Long id) {
        return reagentRepository.findById(id);
    }

    public List<Reagent> findAllReagents() {
        return reagentRepository.findAll();
    }

    public Reagent updateReagent(Long id, Reagent updatedReagent) {
        return reagentRepository.findById(id).map(reagent -> {
            reagent.setMaterialId(updatedReagent.getMaterialId());
            reagent.setMaterial(updatedReagent.getMaterial());
            return reagentRepository.save(reagent);
        }).orElseThrow(() -> new RuntimeException("Reagent not found with ID: " + id));
    }

    public void deleteReagent(Long id) {
        reagentRepository.deleteById(id);
    }
}
