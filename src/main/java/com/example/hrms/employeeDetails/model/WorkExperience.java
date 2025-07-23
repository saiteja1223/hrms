package com.example.hrms.employeeDetails.model;

import com.example.hrms.employeeDetails.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="employee_workExp")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private String finalKey;

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
    @Column(columnDefinition = "MEDIUMBLOB",nullable = false)
    private byte[] relievingLetter;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB",nullable = false)
    private byte[] experienceLetter;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB",nullable = false)
    private byte[] payslips;  // You can also store a zip or PDF of all 3 months
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_details_id", nullable = false)
    @JsonIgnore
    private BasicDetails basicDetails;

}

