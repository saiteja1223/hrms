package com.example.hrms.attendance.model;

import com.example.hrms.auth.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employee_leave_policies")
@Data
public class EmployeeLeavePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user; // Linked to the User identity

    private int year; // The calendar year this policy applies to, e.g., 2025

    private int totalSickLeaves;
    private int totalEarnedLeaves;

    @Column(columnDefinition = "integer default 0")
    private int sickLeavesTaken = 0;

    @Column(columnDefinition = "integer default 0")
    private int earnedLeavesTaken = 0;
}