package com.example.hrms.attendance.service;

import com.example.hrms.attendance.enums.AttendanceStatus;
import com.example.hrms.attendance.model.AttendanceRecord;
import com.example.hrms.attendance.model.Holiday;
import com.example.hrms.attendance.model.WorkingHoursPolicy;
import com.example.hrms.attendance.repository.AttendanceRecordRepository;
import com.example.hrms.attendance.repository.HolidayRepository;
import com.example.hrms.auth.model.User;
import com.example.hrms.auth.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRepo;
    private final UserRepository userRepo;
    private final AttendanceAdminService adminService; // To resolve which rules apply
    private final HolidayRepository holidayRepo;

    @Transactional
    public AttendanceRecord checkIn(String userEmail) {
        User user = userRepo.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("User not found"));
        LocalDate today = LocalDate.now();

        // 1. Check if the user is trying to check in on a non-working day.
        WorkingHoursPolicy policy = adminService.resolvePolicyForEmployee(user.getBasicDetails());
        boolean isHoliday = holidayRepo.existsByOrganizationIdAndHolidayDate(user.getOrgId(), today);
        if (isHoliday || !policy.getWorkingDays().contains(today.getDayOfWeek())) {
            throw new IllegalStateException("Cannot check in on a non-working day or holiday.");
        }

        // 2. Check if a record for today already exists.
        Optional<AttendanceRecord> existingRecord = attendanceRepo.findByUserAndAttendanceDate(user, today);
        if (existingRecord.isPresent()) {
            throw new IllegalStateException("You have already checked in today.");
        }

        // 3. Create and save the new attendance record.
        AttendanceRecord newRecord = new AttendanceRecord();
        newRecord.setUser(user);
        newRecord.setAttendanceDate(today);
        newRecord.setCheckInTime(LocalTime.now());
        newRecord.setStatus(AttendanceStatus.PRESENT);

        return attendanceRepo.save(newRecord);
    }

    @Transactional
    public AttendanceRecord checkOut(String userEmail) {
        User user = userRepo.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("User not found"));
        LocalDate today = LocalDate.now();

        // Find today's record. It must exist to check out.
        AttendanceRecord record = attendanceRepo.findByUserAndAttendanceDate(user, today)
                .orElseThrow(() -> new IllegalStateException("Cannot check out without checking in first."));

        if (record.getCheckOutTime() != null) {
            throw new IllegalStateException("You have already checked out today.");
        }

        record.setCheckOutTime(LocalTime.now());
        return attendanceRepo.save(record);
    }

    // Add methods for getting reports, adding notes, etc. here
}