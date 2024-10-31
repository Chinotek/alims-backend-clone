package com.project.alims.repository;

import com.project.alims.model.IncidentForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentFormRepository extends JpaRepository<IncidentForm, Long> {
}