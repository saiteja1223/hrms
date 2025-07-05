package com.example.hrms.employeeDetails.model;

import com.example.hrms.employeeDetails.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileInfoDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private byte[] profilePicture;  // Required

    @Column(columnDefinition = "TEXT")
    private String bio;             // Optional large text
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
}

