package com.example.hrms.attendance.dto;

import com.example.hrms.attendance.enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveStatusUpdateDto {

    /**
     * The new status for the leave application.
     * This must be either APPROVED or REJECTED.
     */
    @NotNull(message = "New status cannot be null. Must be APPROVED or REJECTED.")
    private LeaveStatus newStatus;

    /**
     * An optional comment from the manager, for example,
     * explaining why a leave request was rejected.
     */
    private String managerComment;
}