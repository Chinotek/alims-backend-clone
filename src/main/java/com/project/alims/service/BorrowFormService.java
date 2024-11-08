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

        LocalDate previousReturnState = existingBorrowForm.getDateReturned();
        LocalDate currentReturnState = updatedBorrowForm.getDateReturned();

        Integer previousQty = existingBorrowForm.getQty();
        Integer currentQty = updatedBorrowForm.getQty();
        Integer deductedAmount = currentQty - previousQty;

        Long existingMaterialId = updatedBorrowForm.getMaterialId();
        Material existingMaterial = materialRepository.findById(existingMaterialId)
                .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));
        // if no longer borrowed, return deduction based on total quantity

        if(previousReturnState == null && currentReturnState != null) {
            DeductQuantitytoMaterial(existingMaterial, -previousQty); // add back
            // regardless of changes to amount, return previously borrowed
        } else if (previousReturnState != null && currentReturnState == null) {
            DeductQuantitytoMaterial(existingMaterial, currentQty);
            // regardless of changes to amount, take what is currently borrowed
        } else if (previousReturnState == null){ // && currentReturnState == null
            // therefore still borrowed, update amount
            DeductQuantitytoMaterial(existingMaterial, deductedAmount);
        }
        // if previousReturnState != null && currentReturnState != null
        // do nothing, already borrowed, no need to update Material Qty

        existingBorrowForm.setUserId(updatedBorrowForm.getUserId());
        existingBorrowForm.setMaterialId(updatedBorrowForm.getMaterialId());
        existingBorrowForm.setUser(updatedBorrowForm.getUser());
        existingBorrowForm.setMaterial(updatedBorrowForm.getMaterial());

        existingBorrowForm.setDateBorrowed(updatedBorrowForm.getDateBorrowed());
        existingBorrowForm.setDetailsOfBorrowed(updatedBorrowForm.getDetailsOfBorrowed());
        existingBorrowForm.setEquipment(updatedBorrowForm.getEquipment());
        existingBorrowForm.setQty(updatedBorrowForm.getQty());
        existingBorrowForm.setUnit(updatedBorrowForm.getUnit());
        existingBorrowForm.setBorrowerDetail(updatedBorrowForm.getBorrowerDetail());
        existingBorrowForm.setDepartment(updatedBorrowForm.getDepartment());
        existingBorrowForm.setTimeBorrowed(updatedBorrowForm.getTimeBorrowed());
        existingBorrowForm.setDateReturned(updatedBorrowForm.getDateReturned());
        existingBorrowForm.setTimeReturned(updatedBorrowForm.getTimeReturned());
        existingBorrowForm.setRemarks(updatedBorrowForm.getRemarks());
        existingBorrowForm.setDamageMaterials(updatedBorrowForm.getDamageMaterials());

        return borrowFormRepository.save(existingBorrowForm);
    }

    public void deleteBorrowForm(Long id) {
        borrowFormRepository.deleteById(id);
    }
}
