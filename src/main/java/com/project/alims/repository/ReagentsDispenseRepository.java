package com.project.alims.repository;

import com.project.alims.model.ReagentsDispense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReagentsDispenseRepository extends JpaRepository<ReagentsDispense, Long> {
}
