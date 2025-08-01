package com.example.hrms.attendance.repository;

import com.example.hrms.attendance.model.EmployeeLeavePolicy;
import com.example.hrms.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeLeavePolicyRepository extends JpaRepository<EmployeeLeavePolicy, Long> {
    Optional<EmployeeLeavePolicy> findByUserAndYear(User user, int year);
}