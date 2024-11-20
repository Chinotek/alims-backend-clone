package com.project.alims.service;

import com.project.alims.model.CalibrationLog;
import com.project.alims.repository.CalibrationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public CalibrationLog createCalibrationLog(CalibrationLog calibrationLog, MultipartFile file) throws IOException {
        if (file.getSize() > 0) {
            calibrationLog.setFile(file.getBytes());
            calibrationLog.setAttachments(file.getOriginalFilename());
            calibrationLog.setFileType(file.getContentType());
        }
        return calibrationLogRepository.save(calibrationLog);
    }

    // Find all CalibrationLogs
    public List<CalibrationLog> getAllCalibrationLogs() {
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
    public List<CalibrationLog> findCalibrationLogsByMaterialId(Long materialId) {
        return calibrationLogRepository.findByMaterialId(materialId);
    }

    // Update an existing CalibrationLog
    public CalibrationLog updateCalibrationLog(Long calibrationId, CalibrationLog updatedCalibrationLog, MultipartFile file) throws IOException {
        CalibrationLog existingCalibrationLog = findCalibrationLogById(calibrationId);
        if (existingCalibrationLog != null) {
            existingCalibrationLog.setUserId(updatedCalibrationLog.getUserId());
            existingCalibrationLog.setMaterialId(updatedCalibrationLog.getMaterialId());
            existingCalibrationLog.setUser(updatedCalibrationLog.getUser());
            existingCalibrationLog.setMaterial(updatedCalibrationLog.getMaterial());

            existingCalibrationLog.setCalibrationDate(updatedCalibrationLog.getCalibrationDate());
            existingCalibrationLog.setNextCalibration(updatedCalibrationLog.getNextCalibration());
            existingCalibrationLog.setNotes(updatedCalibrationLog.getNotes());

            // if no file sent, nothing happens, otherwise replace with new file
            if (file.getSize() > 0) {
                existingCalibrationLog.setFile(file.getBytes());
                existingCalibrationLog.setAttachments(file.getOriginalFilename());
                existingCalibrationLog.setFileType(file.getContentType());
            }
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
