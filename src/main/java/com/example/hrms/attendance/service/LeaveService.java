package com.example.hrms.attendance.service;

import com.example.hrms.attendance.dto.LeaveApplicationRequestDto;
import com.example.hrms.attendance.dto.LeaveBalanceDto;
import com.example.hrms.attendance.enums.AttendanceStatus;
import com.example.hrms.attendance.enums.LeaveStatus;
import com.example.hrms.attendance.enums.LeaveType;
import com.example.hrms.attendance.model.*;
import com.example.hrms.attendance.repository.AttendanceRecordRepository;
import com.example.hrms.attendance.repository.EmployeeLeavePolicyRepository;
import com.example.hrms.attendance.repository.LeaveApplicationRepository;
import com.example.hrms.auth.model.User;
import com.example.hrms.employeeDetails.model.BasicDetails;
import com.example.hrms.employeeDetails.model.OnboardingInfoDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveApplicationRepository leaveRepo;
    private final EmployeeLeavePolicyRepository policyRepo;
    private final AttendanceRecordRepository attendanceRepo;

    // Gets the remaining leave balance for a given employee and year.
    public LeaveBalanceDto getLeaveBalance(User user, int year) {
        EmployeeLeavePolicy policy = policyRepo.findByUserAndYear(user, year)
                .orElse(new EmployeeLeavePolicy()); // Return empty/zero policy if none is set

        return LeaveBalanceDto.builder()
                .employeeName(user.getBasicDetails().getFullName())
                .year(year)
                .totalSickLeaves(policy.getTotalSickLeaves())
                .sickLeavesTaken(policy.getSickLeavesTaken())
                .sickLeavesRemaining(policy.getTotalSickLeaves() - policy.getSickLeavesTaken())
                .totalEarnedLeaves(policy.getTotalEarnedLeaves())
                .earnedLeavesTaken(policy.getEarnedLeavesTaken())
                .earnedLeavesRemaining(policy.getTotalEarnedLeaves() - policy.getEarnedLeavesTaken())
                .build();
    }

    // An employee applies for leave.
    @Transactional
    public LeaveApplication applyForLeave(LeaveApplicationRequestDto dto, User applicant) {
        // Business logic validation
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        // Check for overlapping applications
        List<LeaveApplication> overlaps = leaveRepo.findByApplicant_IdAndStatusNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                applicant.getId(), LeaveStatus.REJECTED, dto.getEndDate(), dto.getStartDate());
        if (!overlaps.isEmpty()) {
            throw new IllegalStateException("You already have a pending or approved leave application for this date range.");
        }

        // Check leave balance
        long daysRequested = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;
        EmployeeLeavePolicy policy = policyRepo.findByUserAndYear(applicant, dto.getStartDate().getYear()).orElseThrow();
        if (dto.getLeaveType() == LeaveType.SICK_LEAVE && (policy.getTotalSickLeaves() - policy.getSickLeavesTaken()) < daysRequested) {
            throw new IllegalStateException("Insufficient sick leave balance.");
        }
        if (dto.getLeaveType() == LeaveType.EARNED_LEAVE && (policy.getTotalEarnedLeaves() - policy.getEarnedLeavesTaken()) < daysRequested) {
            throw new IllegalStateException("Insufficient earned leave balance.");
        }

        // Find the applicant's manager
        String managerName = Optional.ofNullable(applicant.getBasicDetails().getOnboardingInfoDetails())
                .map(OnboardingInfoDetails::getReportingManager)
                .orElseThrow(() -> new IllegalStateException("No manager assigned. Cannot apply for leave."));
        // Create and save the application
        LeaveApplication application = new LeaveApplication();
        application.setApplicant(applicant);
        //application.setApprover(manager);
        application.setApproverInfo(managerName);
        application.setStartDate(dto.getStartDate());
        application.setEndDate(dto.getEndDate());
        application.setLeaveType(dto.getLeaveType());
        application.setReason(dto.getReason());
        application.setStatus(LeaveStatus.PENDING);

        return leaveRepo.save(application);
    }

    // A manager approves a leave request.
    @Transactional
    public LeaveApplication approveLeave(Long leaveId, User managerUser) {
        LeaveApplication application = leaveRepo.findById(leaveId).orElseThrow();

        // Security check
        if (!application.getApprover().getUser().equals(managerUser)) {
            throw new SecurityException("You are not authorized to approve this leave request.");
        }

        application.setStatus(LeaveStatus.APPROVED);

        // Decrement leave balance and create attendance records for the leave period
        long days = ChronoUnit.DAYS.between(application.getStartDate(), application.getEndDate()) + 1;
        EmployeeLeavePolicy policy = policyRepo.findByUserAndYear(application.getApplicant(), application.getStartDate().getYear()).orElseThrow();

        if(application.getLeaveType() == LeaveType.SICK_LEAVE) {
            policy.setSickLeavesTaken(policy.getSickLeavesTaken() + (int)days);
        } else {
            policy.setEarnedLeavesTaken(policy.getEarnedLeavesTaken() + (int)days);
        }
        policyRepo.save(policy);

        // Populate attendance records
        for (LocalDate date = application.getStartDate(); !date.isAfter(application.getEndDate()); date = date.plusDays(1)) {
            AttendanceRecord record = new AttendanceRecord();
            record.setUser(application.getApplicant());
            record.setAttendanceDate(date);
            record.setStatus(application.getLeaveType() == LeaveType.SICK_LEAVE ? AttendanceStatus.SICK_LEAVE : AttendanceStatus.ON_LEAVE);
            attendanceRepo.save(record);
        }

        return leaveRepo.save(application);
    }
}