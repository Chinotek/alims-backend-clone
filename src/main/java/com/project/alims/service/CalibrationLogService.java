package com.project.alims.service;

import com.project.alims.model.CalibrationLog;
import com.project.alims.repository.CalibrationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalibrationLogService {
    private final CalibrationLogRepository calibrationLogRepository;

    @Autowired
    public CalibrationLogService(CalibrationLogRepository calibrationLogRepository) {
        this.calibrationLogRepository = calibrationLogRepository;
    }

    // Create a new CalibrationLog entry
    public CalibrationLog createCalibrationLog(CalibrationLog calibrationLog) {
        return calibrationLogRepository.save(calibrationLog);
    }

    // Find all CalibrationLogs
    public List<CalibrationLog> findAllCalibrationLogs() {
        return calibrationLogRepository.findAll();
    }

    // Find CalibrationLog by calibrationId
    public CalibrationLog findCalibrationLogById(Long calibrationId) {
        Optional<CalibrationLog> calibrationLog = calibrationLogRepository.findById(calibrationId);
        return calibrationLog.orElse(null);
    }

    // Find CalibrationLogs by userId
    public List<CalibrationLog> findCalibrationLogsByUserId(Long userId) {
        return calibrationLogRepository.findByUserId(userId);
    }

    // Find CalibrationLogs by item_code (Material)
    public List<CalibrationLog> findCalibrationLogsByItemCode(String itemCode) {
        return calibrationLogRepository.findByMaterialItemCode(itemCode);
    }

    // Update an existing CalibrationLog
    public CalibrationLog updateCalibrationLog(Long calibrationId, CalibrationLog updatedCalibrationLog) {
        CalibrationLog existingCalibrationLog = findCalibrationLogById(calibrationId);
        if (existingCalibrationLog != null) {
            existingCalibrationLog.setCalibrationDate(updatedCalibrationLog.getCalibrationDate());
            existingCalibrationLog.setNextCalibration(updatedCalibrationLog.getNextCalibration());
            existingCalibrationLog.setNotes(updatedCalibrationLog.getNotes());
            existingCalibrationLog.setAttachments(updatedCalibrationLog.getAttachments());
            // dateUpdated will be updated by @PreUpdate in the entity class
            return calibrationLogRepository.save(existingCalibrationLog);
        } else {
            throw new RuntimeException("CalibrationLog not found with ID: " + calibrationId);
        }
    }

    // Delete a CalibrationLog by id
    public void deleteCalibrationLog(Long calibrationId) {
        CalibrationLog calibrationLog = findCalibrationLogById(calibrationId);
        if (calibrationLog != null) {
            calibrationLogRepository.deleteById(calibrationId);
        } else {
            throw new RuntimeException("CalibrationLog not found with ID: " + calibrationId);
        }
    }
}
