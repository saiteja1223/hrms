package com.example.hrms.employeeDetails.model;

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
    @Column(columnDefinition = "MEDIUMBLOB",nullable = false)
    private byte[] offerLetter;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB",nullable = false)
    private byte[] signedNda;

    // Optional document
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] joiningKit;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_details_id", nullable = false)
    @JsonIgnore
    private BasicDetails basicDetails;
}

