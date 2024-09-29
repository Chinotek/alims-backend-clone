package com.project.alims.repository;

import com.project.alims.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserId(Long userId);
    User findByEmailOrUsername(String email, String username);
    User findByEmailAndOtp(String email, String otp);
    User findByUsername(String username);
    List<User> findAll();
}
