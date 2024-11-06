package com.project.alims.service;

import com.project.alims.model.DisposalForm;
import com.project.alims.model.InventoryLog;
import com.project.alims.model.Material;
import com.project.alims.repository.DisposalFormRepository;
import com.project.alims.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisposalFormService {
    private final DisposalFormRepository disposalFormRepository;
    private final MaterialRepository materialRepository;

    @Autowired
    public DisposalFormService(DisposalFormRepository disposalFormRepository, MaterialRepository materialRepository) {
        this.disposalFormRepository = disposalFormRepository;
        this.materialRepository = materialRepository;
    }

    public List<DisposalForm> findAllDisposalForms() {
        return disposalFormRepository.findAll();
    }

    public DisposalForm findDisposalFormById(Long disposalId) {
        Optional<DisposalForm> disposalForm = disposalFormRepository.findById(disposalId);
        return disposalForm.orElse(null);
    }

    public List<DisposalForm> findDisposalFormsByUserId(Long userId) {
        return disposalFormRepository.findByUserId(userId);
    }

    public List<DisposalForm> findDisposalFormsByMaterialId(Long itemCode) {
        return disposalFormRepository.findByMaterialId(itemCode);
    }

    public Material DeductQuantitytoMaterial(Material existingDisposalFormMaterial, Integer deductedAmount) {
        if (existingDisposalFormMaterial != null) {
            Integer quantityAvailable = existingDisposalFormMaterial.getQuantityAvailable();
            quantityAvailable -= deductedAmount;
            existingDisposalFormMaterial.setQuantityAvailable(quantityAvailable);
            return materialRepository.save(existingDisposalFormMaterial);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public DisposalForm createDisposalForm(DisposalForm disposalForm) {
        Integer deductedAmount = disposalForm.getQty();
        Long existingMaterialId = disposalForm.getMaterialId();
        Material existingMaterial = materialRepository.findById(existingMaterialId)
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));

        DeductQuantitytoMaterial(existingMaterial, deductedAmount);
        return disposalFormRepository.save(disposalForm);
    }

    public DisposalForm updateDisposalForm(Long disposalId, DisposalForm updatedDisposalForm) {
        DisposalForm existingDisposalForm = disposalFormRepository.findById(disposalId)
                .orElseThrow(() -> new RuntimeException("Disposal Form not found with ID: " + disposalId));

        Integer deductedAmount = updatedDisposalForm.getQty() - existingDisposalForm.getQty();
        Material existingDisposalFormMaterial = existingDisposalForm.getMaterial();

        DeductQuantitytoMaterial(existingDisposalFormMaterial, deductedAmount);

        existingDisposalForm.setUserId(updatedDisposalForm.getUserId());
        existingDisposalForm.setMaterialId(updatedDisposalForm.getMaterialId());
        existingDisposalForm.setUser(updatedDisposalForm.getUser());
        existingDisposalForm.setMaterial(updatedDisposalForm.getMaterial());

        existingDisposalForm.setItemDescription(updatedDisposalForm.getItemDescription());
        existingDisposalForm.setQty(updatedDisposalForm.getQty());
        existingDisposalForm.setReasonForDisposal(updatedDisposalForm.getReasonForDisposal());
        existingDisposalForm.setDisposalMethod(updatedDisposalForm.getDisposalMethod());
        existingDisposalForm.setDisposedBy(updatedDisposalForm.getDisposedBy());
        existingDisposalForm.setComments(updatedDisposalForm.getComments());
        // dateUpdated is handled by @PreUpdate in the entity class
        return disposalFormRepository.save(existingDisposalForm);
    }

    public void deleteDisposalForm(Long disposalId) {
        DisposalForm disposalForm = findDisposalFormById(disposalId);
        if (disposalForm != null) {
            disposalFormRepository.deleteById(disposalId);
        } else {
            throw new RuntimeException("DisposalForm not found with ID: " + disposalId);
        }
    }
}
