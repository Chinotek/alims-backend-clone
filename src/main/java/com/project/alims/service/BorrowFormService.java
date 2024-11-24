package com.project.alims.service;

import com.project.alims.model.BorrowForm;
import com.project.alims.model.DisposalForm;
import com.project.alims.model.Material;
import com.project.alims.repository.BorrowFormRepository;
import com.project.alims.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowFormService {

    private final BorrowFormRepository borrowFormRepository;
    private final MaterialRepository materialRepository;


    @Autowired
    public BorrowFormService(BorrowFormRepository borrowFormRepository, MaterialRepository materialRepository) {
        this.borrowFormRepository = borrowFormRepository;
        this.materialRepository = materialRepository;
    }


    public List<BorrowForm> getAllBorrowForms() {
        return borrowFormRepository.findAll();
    }

    public Optional<BorrowForm> getBorrowFormById(Long id) {
        return borrowFormRepository.findById(id);
    }

    public Material DeductQuantitytoMaterial(Material existingBorrowFormMaterial, Integer deductedAmount) {
        if (existingBorrowFormMaterial != null) {
            Integer quantityAvailable = existingBorrowFormMaterial.getQuantityAvailable();
            quantityAvailable -= deductedAmount;
            existingBorrowFormMaterial.setQuantityAvailable(quantityAvailable);
            return materialRepository.save(existingBorrowFormMaterial);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public BorrowForm createBorrowForm(BorrowForm borrowForm) {
        LocalDate dateReturned = borrowForm.getDateReturned();
        if (dateReturned == null) {
            Integer deductedAmount = borrowForm.getQty();
            Long existingMaterialId = borrowForm.getMaterialId();
            Material existingMaterial = materialRepository.findById(existingMaterialId)
                    .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));
            DeductQuantitytoMaterial(existingMaterial, deductedAmount);
        }
        return borrowFormRepository.save(borrowForm);
    }

    public BorrowForm updateBorrowForm(Long borrowId, BorrowForm updatedBorrowForm) {
        BorrowForm existingBorrowForm = borrowFormRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Disposal Form not found with ID: " + borrowId));

        // assume that if updated is null, simply because no new Update to Date Returned
        if (updatedBorrowForm.getDateReturned() != null && updatedBorrowForm.getQty() != null) {
            LocalDate previousReturnState = existingBorrowForm.getDateReturned();
            LocalDate currentReturnState = updatedBorrowForm.getDateReturned();

            Integer previousQty = existingBorrowForm.getQty();
            Integer currentQty = updatedBorrowForm.getQty();
            Integer deductedAmount = 0;
            if(currentQty != null && previousQty != null) {
                deductedAmount = currentQty - previousQty;
            }

            Long existingMaterialId = updatedBorrowForm.getMaterialId();
            Material existingMaterial = materialRepository.findById(existingMaterialId)
                    .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));
            // if no longer borrowed, return deduction based on total quantity

            if(previousReturnState == null && currentReturnState != null && previousQty != null) {
                // if previousQty is null, do nothing - nothing to add back
                DeductQuantitytoMaterial(existingMaterial, -previousQty); // add back
                // regardless of changes to amount, return previously borrowed
                // from not returned to returned
            } else if (previousReturnState == null){ // && currentReturnState == null
                // therefore still borrowed, update amount
                DeductQuantitytoMaterial(existingMaterial, deductedAmount);
            }

            // previousReturnState != null && currentReturnState == null
            // DeductQuantitytoMaterial(existingMaterial, currentQty);
            // regardless of changes to amount, take what is currently borrowed
            // but returned to not returned - cannot un-return therefore removed this function

            // if previousReturnState != null && currentReturnState != null
            // do nothing, already borrowed, no need to update Material Qty
        }

        if(updatedBorrowForm.getUserId() != null) existingBorrowForm.setUserId(updatedBorrowForm.getUserId());
        if(updatedBorrowForm.getMaterialId() != null) existingBorrowForm.setMaterialId(updatedBorrowForm.getMaterialId());

        if(updatedBorrowForm.getDateBorrowed() != null) existingBorrowForm.setDateBorrowed(updatedBorrowForm.getDateBorrowed());
        if(updatedBorrowForm.getDetailsOfBorrowed() != null) existingBorrowForm.setDetailsOfBorrowed(updatedBorrowForm.getDetailsOfBorrowed());
        if(updatedBorrowForm.getEquipment() != null) existingBorrowForm.setEquipment(updatedBorrowForm.getEquipment());
        if(updatedBorrowForm.getQty() != null) existingBorrowForm.setQty(updatedBorrowForm.getQty());
        if(updatedBorrowForm.getUnit() != null) existingBorrowForm.setUnit(updatedBorrowForm.getUnit());
        if(updatedBorrowForm.getBorrowerDetail() != null) existingBorrowForm.setBorrowerDetail(updatedBorrowForm.getBorrowerDetail());
        if(updatedBorrowForm.getDepartment() != null) existingBorrowForm.setDepartment(updatedBorrowForm.getDepartment());
        if(updatedBorrowForm.getTimeBorrowed() != null) existingBorrowForm.setTimeBorrowed(updatedBorrowForm.getTimeBorrowed());
        if(updatedBorrowForm.getDateReturned() != null) existingBorrowForm.setDateReturned(updatedBorrowForm.getDateReturned());
        if(updatedBorrowForm.getTimeReturned() != null) existingBorrowForm.setTimeReturned(updatedBorrowForm.getTimeReturned());
        if(updatedBorrowForm.getRemarks() != null) existingBorrowForm.setRemarks(updatedBorrowForm.getRemarks());
        if(updatedBorrowForm.getDamageMaterials() != null) existingBorrowForm.setDamageMaterials(updatedBorrowForm.getDamageMaterials());

        return borrowFormRepository.save(existingBorrowForm);
    }

    public void deleteBorrowForm(Long id) {
        borrowFormRepository.deleteById(id);
    }
}
