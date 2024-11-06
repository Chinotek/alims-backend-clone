package com.project.alims.controller;

import com.project.alims.model.CalibrationLog;
import com.project.alims.service.CalibrationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/calibration")
public class CalibrationLogController {

    private final CalibrationLogService calibrationLogService;

    @Autowired
    public CalibrationLogController(CalibrationLogService calibrationLogService) {
        this.calibrationLogService = calibrationLogService;
    }

    // Create a new CalibrationLog
    @PostMapping("/create")
    public ResponseEntity<CalibrationLog> createCalibrationLog(@RequestBody CalibrationLog calibrationLog) {
        CalibrationLog createdLog = calibrationLogService.createCalibrationLog(calibrationLog);
        return ResponseEntity.ok(createdLog);
    }

    // Get all CalibrationLogs
    @GetMapping()
    public ResponseEntity<List<CalibrationLog>> getAllCalibrationLogs() {
        List<CalibrationLog> calibrationLogs = calibrationLogService.getAllCalibrationLogs();
        return ResponseEntity.ok(calibrationLogs);
    }

    // Get CalibrationLog by calibrationId
    @GetMapping("/{id}")
    public ResponseEntity<CalibrationLog> getCalibrationLogById(@PathVariable Long id) {
        CalibrationLog calibrationLog = calibrationLogService.findCalibrationLogById(id);
        if (calibrationLog != null) {
            return ResponseEntity.ok(calibrationLog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get CalibrationLogs by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CalibrationLog>> getCalibrationLogsByUserId(@PathVariable Long userId) {
        List<CalibrationLog> logs = calibrationLogService.findCalibrationLogsByUserId(userId);
        return ResponseEntity.ok(logs);
    }

    // Get CalibrationLogs by itemCode (Material)
    @GetMapping("/material/{materialId}")
    public ResponseEntity<List<CalibrationLog>> getCalibrationLogsByItemCode(@PathVariable Long materialId) {
        List<CalibrationLog> logs = calibrationLogService.findCalibrationLogsByMaterialId(materialId);
        return ResponseEntity.ok(logs);
    }

    // Update a CalibrationLog by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<CalibrationLog> updateCalibrationLog(@PathVariable Long id, @RequestBody CalibrationLog updatedCalibrationLog) {
        try {
            CalibrationLog calibrationLog = calibrationLogService.updateCalibrationLog(id, updatedCalibrationLog);
            return ResponseEntity.ok(calibrationLog);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a CalibrationLog by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCalibrationLog(@PathVariable Long id) {
        try {
            calibrationLogService.deleteCalibrationLog(id);
            return ResponseEntity.ok("Calibration Log deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
