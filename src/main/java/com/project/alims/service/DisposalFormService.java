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

        Integer deductedAmount = 0;
        if(updatedDisposalForm.getQty() != null && existingDisposalForm.getQty() != null) {
            deductedAmount = updatedDisposalForm.getQty() - existingDisposalForm.getQty();
        }
        Long existingMaterialId = updatedDisposalForm.getMaterialId();
        Material existingMaterial = materialRepository.findById(existingMaterialId)
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));

        if(deductedAmount != 0) DeductQuantitytoMaterial(existingMaterial, deductedAmount);

        if(updatedDisposalForm.getUserId() != null) existingDisposalForm.setUserId(updatedDisposalForm.getUserId());
        if(updatedDisposalForm.getMaterialId() != null) existingDisposalForm.setMaterialId(updatedDisposalForm.getMaterialId());

        if(updatedDisposalForm.getItemDescription() != null) existingDisposalForm.setItemDescription(updatedDisposalForm.getItemDescription());
        if(updatedDisposalForm.getQty() != null) existingDisposalForm.setQty(updatedDisposalForm.getQty());
        if(updatedDisposalForm.getReasonForDisposal() != null) existingDisposalForm.setReasonForDisposal(updatedDisposalForm.getReasonForDisposal());
        if(updatedDisposalForm.getDisposalMethod() != null) existingDisposalForm.setDisposalMethod(updatedDisposalForm.getDisposalMethod());
        if(updatedDisposalForm.getDisposedBy() != null) existingDisposalForm.setDisposedBy(updatedDisposalForm.getDisposedBy());
        if(updatedDisposalForm.getComments() != null) existingDisposalForm.setComments(updatedDisposalForm.getComments());
        if(updatedDisposalForm.getDateDisposed() != null) existingDisposalForm.setDateDisposed(updatedDisposalForm.getDateDisposed());
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
