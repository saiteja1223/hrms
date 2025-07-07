package com.example.hrms.employeeDetails.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InitiateOnboardingDto {
    private String fullName;
    private String email; // The email the new employee will use to log in
}