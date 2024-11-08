package com.project.alims.controller;

import com.project.alims.model.BorrowForm;
import com.project.alims.service.BorrowFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/borrow")
public class BorrowFormController {

    private final BorrowFormService borrowFormService;

    @Autowired
    public BorrowFormController(BorrowFormService borrowFormService) {
        this.borrowFormService = borrowFormService;
    }

    @PostMapping
    public ResponseEntity<BorrowForm> createBorrowForm(@RequestBody BorrowForm borrowForm) {
        BorrowForm savedBorrowForm = borrowFormService.createBorrowForm(borrowForm);
        return ResponseEntity.ok(savedBorrowForm);
    }

    @GetMapping
    public ResponseEntity<List<BorrowForm>> getAllBorrowForms() {
        List<BorrowForm> borrowForms = borrowFormService.getAllBorrowForms();
        return ResponseEntity.ok(borrowForms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowForm> getBorrowFormById(@PathVariable Long id) {
        return borrowFormService.getBorrowFormById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowForm> updateBorrowForm(@PathVariable Long id, @RequestBody BorrowForm borrowForm) {
        BorrowForm updatedBorrowForm = borrowFormService.updateBorrowForm(id, borrowForm);
        return ResponseEntity.ok(updatedBorrowForm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowForm(@PathVariable Long id) {
        borrowFormService.deleteBorrowForm(id);
        return ResponseEntity.noContent().build();
    }
}
