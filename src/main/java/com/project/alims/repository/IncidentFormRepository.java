package com.project.alims.repository;

import com.project.alims.model.IncidentForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentFormRepository extends JpaRepository<IncidentForm, Long> {
    List<IncidentForm> findByUserUserId(Long userId);
}