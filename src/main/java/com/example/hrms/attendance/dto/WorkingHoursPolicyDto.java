package com.example.hrms.attendance.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Data
public class WorkingHoursPolicyDto {
    @NotBlank(message = "Policy name is required.")
    private String policyName;

    @NotEmpty(message = "At least one working day must be selected.")
    private Set<DayOfWeek> workingDays;

    @NotNull(message = "Start time is required.")
    private LocalTime startTime;

    @NotNull(message = "End time is required.")
    private LocalTime endTime;

    private boolean isOrganizationDefault;
}