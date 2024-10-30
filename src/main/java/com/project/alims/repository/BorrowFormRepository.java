package com.project.alims.repository;

import com.project.alims.model.BorrowForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowFormRepository extends JpaRepository<BorrowForm, Long> {
}
