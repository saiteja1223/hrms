package com.example.hrms.attendance.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayDto {
    @NotNull(message = "Holiday date cannot be null.")
    @FutureOrPresent(message = "Holiday date must be in the present or future.")
    private LocalDate holidayDate;

    @NotBlank(message = "Holiday name is required.")
    private String holidayName;
}