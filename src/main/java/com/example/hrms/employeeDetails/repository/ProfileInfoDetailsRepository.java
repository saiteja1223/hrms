package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.ProfileInfoDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileInfoDetailsRepository extends JpaRepository<ProfileInfoDetails, Long> {
}
