package com.example.hrms.employeeDetails.enums;

public enum OnboardingStatus {
    INITIATED,                 // Shell record created by manager, waiting for employee
    PENDING_MANAGER_REVIEW,    // Employee has submitted their part
    COMPLETED,                 // Manager has added their part and approved
    REJECTED
}