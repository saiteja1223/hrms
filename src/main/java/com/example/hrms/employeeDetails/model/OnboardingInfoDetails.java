package com.example.hrms.employeeDetails.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_onboarding_details")
public class OnboardingInfoDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateOfJoining;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String reportingManager; // Can be linked to Employee later as @ManyToOne

    // Required document uploads
    @Lob
    @Column(nullable = false)
    private byte[] offerLetter;

    @Lob
    @Column(nullable = false)
    private byte[] signedNda;

    // Optional document
    @Lob
    private byte[] joiningKit;
}

