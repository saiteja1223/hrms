package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.AddressDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDetailsRepository extends JpaRepository<AddressDetails, Long> {
}
