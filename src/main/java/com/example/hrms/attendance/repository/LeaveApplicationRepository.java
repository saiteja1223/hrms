package com.example.hrms.attendance.repository;

import com.example.hrms.attendance.enums.LeaveStatus;
import com.example.hrms.attendance.model.LeaveApplication;
import com.example.hrms.employeeDetails.model.BasicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    List<LeaveApplication> findByApproverAndStatus(BasicDetails approver, LeaveStatus status);

    // Checks for overlapping leave applications for a single applicant.
    List<LeaveApplication> findByApplicant_IdAndStatusNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long applicantId, LeaveStatus status, LocalDate endDate, LocalDate startDate);
}