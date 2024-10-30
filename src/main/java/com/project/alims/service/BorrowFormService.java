package com.project.alims.service;

import com.project.alims.model.BorrowForm;
import com.project.alims.repository.BorrowFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowFormService {

    private final BorrowFormRepository borrowFormRepository;

    @Autowired
    public BorrowFormService(BorrowFormRepository borrowFormRepository) {
        this.borrowFormRepository = borrowFormRepository;
    }

    public BorrowForm saveBorrowForm(BorrowForm borrowForm) {
        return borrowFormRepository.save(borrowForm);
    }

    public List<BorrowForm> getAllBorrowForms() {
        return borrowFormRepository.findAll();
    }

    public Optional<BorrowForm> getBorrowFormById(Long id) {
        return borrowFormRepository.findById(id);
    }

    public BorrowForm updateBorrowForm(Long id, BorrowForm updatedBorrowForm) {
        return borrowFormRepository.findById(id).map(borrowForm -> {
            borrowForm.setDateBorrowed(updatedBorrowForm.getDateBorrowed());
            borrowForm.setDetailsOfBorrowed(updatedBorrowForm.getDetailsOfBorrowed());
            borrowForm.setEquipment(updatedBorrowForm.getEquipment());
            borrowForm.setQty(updatedBorrowForm.getQty());
            borrowForm.setUnit(updatedBorrowForm.getUnit());
            borrowForm.setBorrowerDetail(updatedBorrowForm.getBorrowerDetail());
            borrowForm.setDepartment(updatedBorrowForm.getDepartment());
            borrowForm.setTimeBorrowed(updatedBorrowForm.getTimeBorrowed());
            borrowForm.setDateReturned(updatedBorrowForm.getDateReturned());
            borrowForm.setTimeReturned(updatedBorrowForm.getTimeReturned());
            borrowForm.setRemarks(updatedBorrowForm.getRemarks());
            borrowForm.setDamageMaterials(updatedBorrowForm.getDamageMaterials());
            return borrowFormRepository.save(borrowForm);
        }).orElseThrow(() -> new RuntimeException("BorrowForm not found with id " + id));
    }

    public void deleteBorrowForm(Long id) {
        borrowFormRepository.deleteById(id);
    }
}
