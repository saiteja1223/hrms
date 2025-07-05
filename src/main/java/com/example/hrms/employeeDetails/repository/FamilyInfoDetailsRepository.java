package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.FamilyInfoDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyInfoDetailsRepository extends JpaRepository<FamilyInfoDetails, Long> {
}
