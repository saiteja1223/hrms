package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.EducationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationDetailsRepository extends JpaRepository<EducationDetails, Long> {
}
