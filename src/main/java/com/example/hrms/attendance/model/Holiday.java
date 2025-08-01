package com.example.hrms.attendance.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "holidays")
@Data
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long organizationId;

    @Column(nullable = false)
    private LocalDate holidayDate;

    @Column(nullable = false)
    private String holidayName;
}