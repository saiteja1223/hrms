package com.example.hrms.employeeDetails.model;

import com.example.hrms.employeeDetails.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_family_details")
public class FamilyInfoDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String parentName; // Father's or Mother's Name

    private String spouseName; // Optional

    @Column(nullable = false)
    private String emergencyContactName;

    @Column(nullable = false)
    private String emergencyContactRelation;

    @Column(nullable = false)
    private Long emergencyContactNumber;

    @Lob
    private byte[] dependentsInfoFile; // Optional: e.g. PDF of dependent list
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_details_id", nullable = false)
    @JsonIgnore
    private BasicDetails basicDetails;
}

