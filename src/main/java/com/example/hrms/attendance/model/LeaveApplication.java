package com.example.hrms.attendance.model;

import com.example.hrms.attendance.enums.LeaveStatus;
import com.example.hrms.attendance.enums.LeaveType;
import com.example.hrms.auth.model.User;
import com.example.hrms.employeeDetails.model.BasicDetails;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "leave_applications")
@Data
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The person applying for leave IS the User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_user_id", nullable = false)
    private User applicant;

    // The approver IS a manager, who is also an employee.
    // The link to BasicDetails is correct for finding them in the hierarchy.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_employee_id")
    private BasicDetails approver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String reason;
    private String approverInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status;


    private String managerComment; // Optional field for manager's feedback
}