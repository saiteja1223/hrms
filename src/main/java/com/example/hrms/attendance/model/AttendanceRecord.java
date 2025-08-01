package com.example.hrms.attendance.model;

import com.example.hrms.attendance.enums.AttendanceStatus;
import com.example.hrms.auth.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance_records")
@Data
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user; // Linked to the User identity

    @Column(nullable = false)
    private LocalDate attendanceDate;

    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @Lob
    private String workProgressNote;
}