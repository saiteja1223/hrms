package com.example.hrms.attendance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder // The Builder pattern is great for creating DTOs with many fields.
public class LeaveBalanceDto {
    private String employeeName;
    private int year;

    private int totalSickLeaves;
    private int sickLeavesTaken;
    private int sickLeavesRemaining;

    private int totalEarnedLeaves;
    private int earnedLeavesTaken;
    private int earnedLeavesRemaining;
}