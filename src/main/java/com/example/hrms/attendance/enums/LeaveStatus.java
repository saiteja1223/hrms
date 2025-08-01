package com.example.hrms.attendance.enums;

/**
 * Represents the lifecycle of a leave application.
 */
public enum LeaveStatus {
    PENDING,        // The employee has applied, and it is awaiting manager approval.
    APPROVED,       // The manager has approved the leave.
    REJECTED,       // The manager has rejected the leave.
    CANCELLED       // The employee has withdrawn their application before approval.
}