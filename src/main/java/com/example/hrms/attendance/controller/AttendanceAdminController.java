package com.example.hrms.attendance.controller;

import com.example.hrms.attendance.dto.HolidayDto;
import com.example.hrms.attendance.dto.WorkingHoursPolicyDto;
import com.example.hrms.attendance.model.Department;
import com.example.hrms.attendance.model.Holiday;
import com.example.hrms.attendance.model.WorkingHoursPolicy;
import com.example.hrms.attendance.service.AttendanceAdminService;
import com.example.hrms.auth.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/attendance-config")
@PreAuthorize("hasRole('ADMIN')") // Secures all methods in this controller for ADMIN role
@RequiredArgsConstructor
public class AttendanceAdminController {

    private final AttendanceAdminService adminService;

    // Endpoint for an ADMIN to create a new "Work Week" policy.
    @PostMapping("/policies")
    public ResponseEntity<WorkingHoursPolicy> createWorkPolicy(@RequestBody @Valid WorkingHoursPolicyDto dto, Authentication auth) {
        Long orgId = ((User) auth.getPrincipal()).getOrgId();
        WorkingHoursPolicy policy = adminService.createWorkingHoursPolicy(dto, orgId);
        return new ResponseEntity<>(policy, HttpStatus.CREATED);
    }

    // Endpoint for an ADMIN to create a new "Holiday".
    @PostMapping("/holidays")
    public ResponseEntity<Holiday> createHoliday(@RequestBody @Valid HolidayDto dto, Authentication auth) {
        Long orgId = ((User) auth.getPrincipal()).getOrgId();
        Holiday holiday = adminService.createHoliday(dto, orgId);
        return new ResponseEntity<>(holiday, HttpStatus.CREATED);
    }

    // Endpoint for an ADMIN to assign a specific work policy to a department.
    @PostMapping("/departments/{deptId}/assign-policy/{policyId}")
    public ResponseEntity<Department> assignPolicyToDepartment(@PathVariable Long deptId, @PathVariable Long policyId) {
        Department updatedDepartment = adminService.assignPolicyToDepartment(policyId, deptId);
        return ResponseEntity.ok(updatedDepartment);
    }
}