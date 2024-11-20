package com.project.alims.controller;

import com.project.alims.model.CalibrationLog;
import com.project.alims.service.CalibrationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
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
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalibrationLog> createCalibrationLog(@RequestPart("body") CalibrationLog calibrationLog,
                                                               @RequestPart("file") MultipartFile file) throws IOException {
        CalibrationLog createdLog = calibrationLogService.createCalibrationLog(calibrationLog, file);
        return ResponseEntity.ok(createdLog);
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> downloadCalibrationLogFile(@PathVariable Long id) {
        CalibrationLog calibrationLog = calibrationLogService.findCalibrationLogById(id);
        byte[] bytes = calibrationLog.getFile();
        if (bytes == null) throw new RuntimeException("File not found");
        ByteArrayResource file = new ByteArrayResource(bytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(calibrationLog.getAttachments()).build().toString())
                .body(file);
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
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalibrationLog> updateCalibrationLog(@PathVariable Long id,
                                                               @RequestPart("body") CalibrationLog updatedCalibrationLog,
                                                               @RequestPart("file") MultipartFile file) throws IOException {
            CalibrationLog calibrationLog = calibrationLogService.updateCalibrationLog(id, updatedCalibrationLog, file);
            return ResponseEntity.ok(calibrationLog);
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
