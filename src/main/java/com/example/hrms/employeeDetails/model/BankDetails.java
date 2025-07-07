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
@Table(name = "employee_bank_details")
public class BankDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private Long accountNumber;

    @Column(nullable = false)
    private String ifscCode;

    @Column(nullable = false)
    private String branchName;

    private String uan;        // Optional
    private String pfNumber;   // Optional
    private String esiNumber;  // Optional

    @Column(nullable = false)
    private byte[] cancelledCheque;  // File path to the uploaded cancelled cheque
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_details_id", nullable = false)
    @JsonIgnore
    private BasicDetails basicDetails;
}

