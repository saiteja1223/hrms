package com.example.hrms.attendance.enums;

/**
 * Represents the status of an employee's attendance on a single day.
 */
public enum AttendanceStatus {
    PRESENT,        // Employee has checked in.
    ABSENT,         // Employee was scheduled to work but did not check in.
    ON_LEAVE,       // Employee is on an approved earned leave.
    SICK_LEAVE,     // Employee is on an approved sick leave.
    HOLIDAY,        // The day is a declared public holiday for the organization.
    WEEK_OFF        // The day is a non-working day as per the work policy (e.g., Saturday/Sunday).
}