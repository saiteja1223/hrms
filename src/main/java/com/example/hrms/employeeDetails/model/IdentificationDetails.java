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
@Table(name = "employee_iden_details")
public class IdentificationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long aadhaarNumber;

    @Column(nullable = false)
    private String panNumber;

    private String passportNumber;
    private String voterId;
    private String drivingLicense;


    @Lob
    @Column(nullable = false)
    private byte[] aadhaarFile;

    @Lob
    @Column(nullable = false)
    private byte[] panFile;

    @Lob
    private byte[] passportFile;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_details_id", nullable = false)
    @JsonIgnore
    private BasicDetails basicDetails;

}
