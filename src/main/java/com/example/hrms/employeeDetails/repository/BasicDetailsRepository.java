package com.example.hrms.employeeDetails.repository;

import com.example.hrms.auth.model.User;
import com.example.hrms.employeeDetails.model.BasicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasicDetailsRepository extends JpaRepository<BasicDetails, Long> {
    Optional<BasicDetails> findByUser_Email(String email);
    boolean existsByUser(User user);
}
