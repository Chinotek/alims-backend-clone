package com.project.alims.controller;

import com.project.alims.model.Reagents;
import com.project.alims.service.ReagentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/reagents")
public class ReagentController {

    private final ReagentService reagentService;

    @Autowired
    public ReagentController(ReagentService reagentService) {
        this.reagentService = reagentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Reagents> createReagent(@RequestBody Reagents reagent) {
        Reagents createdReagent = reagentService.createReagent(reagent);
        return new ResponseEntity<>(createdReagent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Reagents>> getAllReagents() {
        List<Reagents> reagents = reagentService.findAllReagents();
        return new ResponseEntity<>(reagents, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reagents> getReagentById(@PathVariable Long id) {
        Optional<Reagents> reagent = reagentService.findById(id);
        return reagent.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Reagents> updateReagent(@PathVariable Long id, @RequestBody Reagents updatedReagent) {
        try {
            Reagents reagent = reagentService.updateReagent(id, updatedReagent);
            return new ResponseEntity<>(reagent, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReagent(@PathVariable Long id) {
        reagentService.deleteReagent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
