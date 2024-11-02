package com.project.alims.repository;

import com.project.alims.model.DisposalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisposalFormRepository extends JpaRepository<DisposalForm, Long> {
    List<DisposalForm> findByUserUserId(Long userId);
    List<DisposalForm> findByMaterialItemCode(String itemCode);
}
