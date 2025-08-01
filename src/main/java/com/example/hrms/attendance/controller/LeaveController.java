package com.example.hrms.attendance.controller;

import com.example.hrms.attendance.dto.LeaveApplicationRequestDto;
import com.example.hrms.attendance.dto.LeaveBalanceDto;
import com.example.hrms.attendance.model.LeaveApplication;
import com.example.hrms.attendance.service.LeaveService;
import com.example.hrms.auth.model.User;
import com.example.hrms.auth.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final UserRepository userRepo; // To fetch full User objects

    // Endpoint for an employee to check their own leave balance for the current year.
    @GetMapping("/balance")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<LeaveBalanceDto> getMyLeaveBalance(Principal principal) {
        User user = userRepo.findByEmail(principal.getName()).orElseThrow();
        LeaveBalanceDto balance = leaveService.getLeaveBalance(user, LocalDate.now().getYear());
        return ResponseEntity.ok(balance);
    }

    // Endpoint for an employee to apply for a new leave.
    @PostMapping("/apply")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<LeaveApplication> applyForLeave(@RequestBody @Valid LeaveApplicationRequestDto dto, Principal principal) {
        User applicant = userRepo.findByEmail(principal.getName()).orElseThrow();
        LeaveApplication application = leaveService.applyForLeave(dto, applicant);
        return ResponseEntity.ok(application);
    }

    // Endpoint for a manager to see all PENDING leave requests for their team.
    @GetMapping("/requests/pending-for-my-team")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<LeaveApplication>> getPendingApprovals(Principal principal) {
        // The service logic would find the manager's BasicDetails from the principal
        // and then find all applications where they are the approver.
        return null; // Placeholder for service call
    }

    // Endpoint for a manager to approve a specific leave request.
    @PostMapping("/{leaveId}/approve")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<LeaveApplication> approveLeave(@PathVariable Long leaveId, Principal principal) {
        User manager = userRepo.findByEmail(principal.getName()).orElseThrow();
        LeaveApplication approvedApplication = leaveService.approveLeave(leaveId, manager);
        return ResponseEntity.ok(approvedApplication);
    }
}