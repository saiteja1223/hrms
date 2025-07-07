package com.example.hrms.employeeDetails.controller;

import com.example.hrms.employeeDetails.dtos.InitiateOnboardingDto;
import com.example.hrms.employeeDetails.model.BasicDetails;
import com.example.hrms.employeeDetails.service.EmployeeLifecycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/employeeLifeCycle")
@RequiredArgsConstructor
public class EmployeeLifecycleController {

    private final EmployeeLifecycleService employeeLifecycleService;

    @PostMapping("/initiateOnboarding")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Object>> initiateOnboarding(@RequestBody InitiateOnboardingDto dto) {
        // The service now returns the full BasicDetails object
        BasicDetails createdEmployee = employeeLifecycleService.initiateOnboarding(dto);

        // Create a structured, useful JSON response
        String message = String.format("Onboarding successfully initiated for %s.", createdEmployee.getFullName());
        Map<String, Object> response = Map.of(
                "message", message,
                "employeeId", createdEmployee.getId(),
                "status", createdEmployee.getOnboardingStatus()
        );

        return ResponseEntity.ok(response);
    }
}