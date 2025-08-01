package com.example.hrms.employeeDetails.model;

import com.example.hrms.attendance.model.WorkingHoursPolicy;
import com.example.hrms.auth.model.User;
import com.example.hrms.employeeDetails.enums.BloodGroup;
import com.example.hrms.employeeDetails.enums.Gender;
import com.example.hrms.employeeDetails.enums.MaritalStatus;
import com.example.hrms.employeeDetails.enums.OnboardingStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"manager", "directReports"}) // To prevent errors
@ToString(exclude = {"manager", "directReports"})      // To prevent errors
@Table(name = "employee_basic_details")
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
    // Add this new relationship for employee-specific policy assignments
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_policy_id")
    private WorkingHoursPolicy assignedWorkingHoursPolicy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private BasicDetails manager;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private Set<BasicDetails> directReports = new HashSet<>();


}



