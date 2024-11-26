package com.project.alims.service;

import com.project.alims.model.BorrowForm;
import com.project.alims.model.DisposalForm;
import com.project.alims.model.InventoryLog;
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
    private final InventoryLogService inventoryLogService;


    @Autowired
    public BorrowFormService(BorrowFormRepository borrowFormRepository, MaterialRepository materialRepository, InventoryLogService inventoryLogService) {
        this.borrowFormRepository = borrowFormRepository;
        this.materialRepository = materialRepository;
        this.inventoryLogService = inventoryLogService;
    }


    public List<BorrowForm> getAllBorrowForms() {
        return borrowFormRepository.findAll();
    }

    public Optional<BorrowForm> getBorrowFormById(Long id) {
        return borrowFormRepository.findById(id);
    }

    public InventoryLog DeductQuantitytoMaterial(Material existingBorrowFormMaterial, BorrowForm borrowForm, Integer deductedAmount) {

        if (existingBorrowFormMaterial != null) {
            String remark;
            // If return (negative deduction = add back)
            if (deductedAmount < 0) {
                remark = "Returned " + -deductedAmount + " " + existingBorrowFormMaterial.getUnit() + " of " +
                        existingBorrowFormMaterial.getItemName() + " on " + LocalDate.now();
            } else {
                remark = "Borrowed " + deductedAmount + " " + existingBorrowFormMaterial.getUnit() + " of " +
                        existingBorrowFormMaterial.getItemName() + " on " + borrowForm.getDateBorrowed();
            }

            InventoryLog inventoryLog = new InventoryLog(
                    borrowForm.getUserId(),
                    existingBorrowFormMaterial.getMaterialId(),
                    LocalDate.now(),
                    -deductedAmount,
                    "Borrow: " + borrowForm.getBorrowId(),
                    remark
            );
            return inventoryLogService.createInventoryLog(inventoryLog);
        } else {
            throw new RuntimeException("Material not found");
        }
    }

    public BorrowForm createBorrowForm(BorrowForm borrowForm) {
        LocalDate dateReturned = borrowForm.getDateReturned();

        // if returned, no need to do anything, nothing was reduced in the first place
        if ("Borrowed".equalsIgnoreCase(borrowForm.getStatus())) {
            Integer deductedAmount = borrowForm.getQty();
            Long existingMaterialId = borrowForm.getMaterialId();
            Material existingMaterial = materialRepository.findById(existingMaterialId)
                    .orElseThrow(() -> new RuntimeException("Material not found with ID: " + existingMaterialId));

            BorrowForm createdBorrowForm = borrowFormRepository.save(borrowForm);
            DeductQuantitytoMaterial(existingMaterial, createdBorrowForm, deductedAmount);
            return createdBorrowForm;
        }
        return borrowFormRepository.save(borrowForm);
    }

    public BorrowForm updateBorrowForm(Long borrowId, BorrowForm updatedBorrowForm) {
        BorrowForm existingBorrowForm = borrowFormRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Disposal Form not found with ID: " + borrowId));

        String previousStatus = existingBorrowForm.getStatus();
        String currentStatus = updatedBorrowForm.getStatus();

        Integer previousQty = existingBorrowForm.getQty();
        Integer currentQty = updatedBorrowForm.getQty();

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
        if(updatedBorrowForm.getStatus() != null) existingBorrowForm.setStatus(updatedBorrowForm.getStatus());

        // if status is null, status was not updated, nothing to do
        if (updatedBorrowForm.getStatus() != null || updatedBorrowForm.getQty() != null) {
            Integer deductedAmount = 0;
            // if previous qty is null, we are reducing nothing
            if(previousQty != null && currentQty != null) {
                deductedAmount = currentQty - previousQty; // none are null, then get both
            } else if (previousQty != null) {
                currentQty = previousQty; // they should be the same
                // if not updated, don't deduct more - let deducted amount = 0
            }
            // both null, then what are even doing


            Long existingMaterialId = existingBorrowForm.getMaterialId();
            if (updatedBorrowForm.getMaterialId() != null) existingMaterialId = updatedBorrowForm.getMaterialId();
            Material existingMaterial = materialRepository.findById(existingMaterialId)
                    .orElseThrow(() -> new RuntimeException("Material not found with ID"));
            // if no longer borrowed, return deduction based on total quantity

            BorrowForm borrowForm = borrowFormRepository.save(existingBorrowForm);

            if("Borrowed".equalsIgnoreCase(previousStatus) && "Returned".equalsIgnoreCase(currentStatus)
                    && previousQty != null && previousQty != 0) {
                // if previousQty is null, do nothing - nothing to add back
                DeductQuantitytoMaterial(existingMaterial, borrowForm, -previousQty); // add back
                // regardless of changes to amount, return previously borrowed
                // from borrowed to returned
            } else if ("Returned".equalsIgnoreCase(previousStatus) && "Borrowed".equalsIgnoreCase(currentStatus)
                    && currentQty != null && currentQty != 0) {
                // current status returned -> borrowed : reduce by currentQty
                DeductQuantitytoMaterial(existingMaterial, borrowForm, currentQty);
            } else if ("Borrowed".equalsIgnoreCase(previousStatus)
                    && deductedAmount != 0) {
                // current status borrowed -> borrowed
                // can work even if status is not updated
                // therefore still borrowed/returned, update amount
                DeductQuantitytoMaterial(existingMaterial, borrowForm, deductedAmount);
            }
            // current status returned -> returned : do nothing (what are we even returning)

            return borrowForm;
        }
        return borrowFormRepository.save(existingBorrowForm);
    }

    public void deleteBorrowForm(Long id) {
        borrowFormRepository.deleteById(id);
    }
}
