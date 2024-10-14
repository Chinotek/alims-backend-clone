package com.project.alims.service;

import com.project.alims.model.DisposalForm;
import com.project.alims.repository.DisposalFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisposalFormService {
    private final DisposalFormRepository disposalFormRepository;

    @Autowired
    public DisposalFormService(DisposalFormRepository disposalFormRepository) {
        this.disposalFormRepository = disposalFormRepository;
    }

    public DisposalForm createDisposalForm(DisposalForm disposalForm) {
        return disposalFormRepository.save(disposalForm);
    }

    public List<DisposalForm> findAllDisposalForms() {
        return disposalFormRepository.findAll();
    }

    public DisposalForm findDisposalFormById(Long incidentId) {
        Optional<DisposalForm> disposalForm = disposalFormRepository.findById(incidentId);
        return disposalForm.orElse(null);
    }

    public List<DisposalForm> findDisposalFormsByUserId(Long userId) {
        return disposalFormRepository.findByUserId(userId);
    }

    public List<DisposalForm> findDisposalFormsByItemCode(String itemCode) {
        return disposalFormRepository.findByMaterialItemCode(itemCode);
    }

    public DisposalForm updateDisposalForm(Long incidentId, DisposalForm updatedDisposalForm) {
        DisposalForm existingDisposalForm = findDisposalFormById(incidentId);
        if (existingDisposalForm != null) {
            existingDisposalForm.setItemDescription(updatedDisposalForm.getItemDescription());
            existingDisposalForm.setQty(updatedDisposalForm.getQty());
            existingDisposalForm.setReasonForDisposal(updatedDisposalForm.getReasonForDisposal());
            existingDisposalForm.setDisposalMethod(updatedDisposalForm.getDisposalMethod());
            existingDisposalForm.setDisposedBy(updatedDisposalForm.getDisposedBy());
            existingDisposalForm.setComments(updatedDisposalForm.getComments());
            // dateUpdated is handled by @PreUpdate in the entity class
            return disposalFormRepository.save(existingDisposalForm);
        } else {
            throw new RuntimeException("DisposalForm not found with ID: " + incidentId);
        }
    }

    public void deleteDisposalForm(Long incidentId) {
        DisposalForm disposalForm = findDisposalFormById(incidentId);
        if (disposalForm != null) {
            disposalFormRepository.deleteById(incidentId);
        } else {
            throw new RuntimeException("DisposalForm not found with ID: " + incidentId);
        }
    }
}
