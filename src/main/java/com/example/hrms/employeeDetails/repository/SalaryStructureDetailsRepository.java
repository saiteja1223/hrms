package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.SalaryStructureDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryStructureDetailsRepository extends JpaRepository<SalaryStructureDetails, Long> {
}

