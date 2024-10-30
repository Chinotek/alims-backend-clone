package com.project.alims.repository;

import com.project.alims.model.Reagents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReagentRepository extends JpaRepository<Reagents, Long> {
    // Additional custom queries can be added here if needed
}
