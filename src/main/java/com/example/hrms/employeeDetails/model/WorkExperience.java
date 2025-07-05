package com.example.hrms.employeeDetails.model;

import com.example.hrms.employeeDetails.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String reasonForLeaving;  // Optional

    @Lob
    @Column(nullable = false)
    private byte[] relievingLetter;

    @Lob
    @Column(nullable = false)
    private byte[] experienceLetter;

    @Lob
    @Column(nullable = false)
    private byte[] payslips;  // You can also store a zip or PDF of all 3 months
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
}

