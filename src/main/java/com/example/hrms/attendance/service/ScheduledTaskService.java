package com.example.hrms.attendance.service;

import com.example.hrms.attendance.enums.AttendanceStatus;
import com.example.hrms.attendance.model.AttendanceRecord;
import com.example.hrms.attendance.model.Holiday;
import com.example.hrms.attendance.model.WorkingHoursPolicy;
import com.example.hrms.attendance.repository.AttendanceRecordRepository;
import com.example.hrms.attendance.repository.HolidayRepository;
import com.example.hrms.auth.model.User;
import com.example.hrms.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j // For logging
public class ScheduledTaskService {

    private final UserRepository userRepository;
    private final AttendanceRecordRepository attendanceRepo;
    private final HolidayRepository holidayRepo;
    private final AttendanceAdminService adminService; // For resolving policies

    /**
     * This method runs automatically every night at 11:59 PM.
     * Its job is to mark employees as ABSENT if they were scheduled to work
     * but have no attendance record for the day.
     * cron = "[Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week]"
     */
    @Scheduled(cron = "0 59 23 * * ?") // Runs at 23:59:00 every day
    @Transactional
    public void markAbsentEmployees() {
        LocalDate today = LocalDate.now();
        log.info("Starting nightly task: Mark Absent Employees for {}", today);

        // 1. Get all active employees. In a real app, you would filter by isActive=true.
        List<User> allUsers = userRepository.findAll();

        // 2. Get all attendance records for today to check against.
        // This is more efficient than querying one-by-one inside the loop.
        Set<Long> usersWithAttendanceToday = attendanceRepo.findUserIdsWithAttendanceOnDate(today);

        // 3. Get all holidays for today to check against.
        List<Holiday> todaysHolidays = holidayRepo.findAllByHolidayDate(today);
        Set<Long> orgsWithHolidayToday = todaysHolidays.stream().map(Holiday::getOrganizationId).collect(Collectors.toSet());

        int absentCount = 0;
        for (User user : allUsers) {
            // If user already has a record (PRESENT, ON_LEAVE, etc.), skip them.
            if (usersWithAttendanceToday.contains(user.getId())) {
                continue;
            }

            // Employee doesn't have an onboarding record yet, skip.
            if (user.getBasicDetails() == null) {
                continue;
            }

            // Check if today was a holiday for this user's organization.
            if (orgsWithHolidayToday.contains(user.getOrgId())) {
                continue; // It was a holiday, so they are not absent.
            }

            // Check if today was a working day for this specific employee.
            WorkingHoursPolicy policy = adminService.resolvePolicyForEmployee(user.getBasicDetails());
            if (policy.getWorkingDays().contains(today.getDayOfWeek())) {
                // Today was a working day, it wasn't a holiday, and they have no record. They were ABSENT.
                AttendanceRecord absentRecord = new AttendanceRecord();
                absentRecord.setUser(user);
                absentRecord.setAttendanceDate(today);
                absentRecord.setStatus(AttendanceStatus.ABSENT);
                attendanceRepo.save(absentRecord);
                absentCount++;
            }
        }
        log.info("Finished nightly task: Marked {} employees as ABSENT.", absentCount);
    }

    // You could add other scheduled tasks here, like:
    // @Scheduled(cron = "0 0 1 1 1 ?") // Runs at 1 AM on January 1st every year
    // public void generateNewAnnualLeavePolicies() { ... }
}