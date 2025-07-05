package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.OnboardingInfoDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardingInfoDetailsRepository extends JpaRepository<OnboardingInfoDetails, Long> {
}
