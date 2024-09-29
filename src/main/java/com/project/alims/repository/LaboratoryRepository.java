package com.project.alims.repository;

import com.project.alims.model.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {
    Laboratory findByLabId(Long labId);
}
