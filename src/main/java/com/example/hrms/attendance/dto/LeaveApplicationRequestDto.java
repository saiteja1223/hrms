package com.example.hrms.attendance.dto;

import com.example.hrms.attendance.enums.LeaveType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveApplicationRequestDto {
    @NotNull(message = "Leave type cannot be null.")
    private LeaveType leaveType;

    @NotNull(message = "Start date cannot be null.")
    @Future(message = "Leave start date must be in the future.")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null.")
    @Future(message = "Leave end date must be in the future.")
    private LocalDate endDate;

    @NotBlank(message = "A reason for the leave is required.")
    private String reason;
}