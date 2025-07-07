package com.example.hrms.employeeDetails.model;

import com.example.hrms.auth.model.User;
import com.example.hrms.employeeDetails.enums.BloodGroup;
import com.example.hrms.employeeDetails.enums.Gender;
import com.example.hrms.employeeDetails.enums.MaritalStatus;
import com.example.hrms.employeeDetails.enums.OnboardingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee-basic_details")
public class BasicDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id;
    // Link to the main User table
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    @ToString.Exclude
    private User user;

    private String FullName;
    private String PersonalEmail;
    private String MobileNumber;
    private String AlternateNumber;
    private  LocalDate Dob;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    private  String Nationality;
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnboardingStatus onboardingStatus;

    // --- RELATIONSHIPS WITH CASCADING ---
    @OneToOne(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private AddressDetails addressDetails;

    @OneToOne(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BankDetails bankDetails;

    @OneToMany(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EducationDetails> educationDetails = new ArrayList<>();

    @OneToOne(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private FamilyInfoDetails familyInfoDetails;

    @OneToOne(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private IdentificationDetails identificationDetails;

    @OneToOne(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private OnboardingInfoDetails onboardingInfoDetails;

    @OneToOne(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProfileInfoDetails profileInfoDetails;

    @OneToOne(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private SalaryStructureDetails salaryStructureDetails;

    @OneToMany(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> workExperiences = new ArrayList<>();

    @OneToMany(mappedBy = "basicDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DynamicFieldValue>dynamicFieldValues=new ArrayList<>();


}



