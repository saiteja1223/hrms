package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.IdentificationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentificationDetailsRepository extends JpaRepository<IdentificationDetails, Long> {
}
