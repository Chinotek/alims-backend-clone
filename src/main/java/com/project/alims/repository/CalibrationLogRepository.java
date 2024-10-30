package com.project.alims.repository;

import com.project.alims.model.CalibrationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalibrationLogRepository extends JpaRepository<CalibrationLog, Long> {

    // Custom query to find all calibration logs by user_id
    List<CalibrationLog> findByUserId(Long userId);

    // Custom query to find all calibration logs by item_code (material)
    List<CalibrationLog> findByMaterialItemCode(String itemCode);
}
