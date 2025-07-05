package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.BasicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicDetailsRepository extends JpaRepository<BasicDetails, Long> {
}
