package com.example.hrms.attendance.controller;

import com.example.hrms.attendance.model.AttendanceRecord;
import com.example.hrms.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/attendance")
@PreAuthorize("hasRole('EMPLOYEE')")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceRecord> checkIn(Principal principal) {
        AttendanceRecord record = attendanceService.checkIn(principal.getName());
        return ResponseEntity.ok(record);
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceRecord> checkOut(Principal principal) {
        AttendanceRecord record = attendanceService.checkOut(principal.getName());
        return ResponseEntity.ok(record);
    }

    // ... Add other endpoints like getting reports, adding work notes etc. ...
}