package com.example.hrms.auth.model;

import com.example.hrms.attendance.model.AttendanceRecord;
import com.example.hrms.attendance.model.EmployeeLeavePolicy;
import com.example.hrms.attendance.model.LeaveApplication;
import com.example.hrms.auth.enums.Role;
import com.example.hrms.employeeDetails.model.BasicDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String email;

        private String password;

        @Enumerated(EnumType.STRING)
        private Role role;

        private Long orgId; // nullable for Admin

        private Boolean isActive = true;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private BasicDetails basicDetails;

        // A user has a list of leave policies (one for each year).
        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<EmployeeLeavePolicy> leavePolicies = new ArrayList<>();

        // A user has a list of all their daily attendance records.
        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<AttendanceRecord> attendanceRecords = new ArrayList<>();

        // A user has a list of all the leave requests they have submitted.
        @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<LeaveApplication> submittedLeaveApplications = new ArrayList<>();
}



