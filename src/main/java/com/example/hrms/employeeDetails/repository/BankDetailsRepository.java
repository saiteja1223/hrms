package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {
}
