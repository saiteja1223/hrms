package com.example.hrms.employeeDetails.model;

import com.example.hrms.employeeDetails.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_education_details")
public class EducationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private String finalKey;

    @Column(nullable = false)
    private String highestQualification;

    @Column(nullable = false)
    private String universityName;

    @Column(nullable = false)
    private Integer yearOfPassing;

    @ElementCollection
    private List<String> otherDegrees;  // Optional list of other degrees

    @ElementCollection
    private List<String> certifications;  // Optional list of certification names

    // Required file uploads
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB",nullable = false)
    private byte[] degreeCertificate;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB",nullable = false)
    private byte[] marksheets;

    // Optional file upload
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] uploadedCertifications;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_details_id", nullable = false)
    @JsonIgnore
    private BasicDetails basicDetails;

}
