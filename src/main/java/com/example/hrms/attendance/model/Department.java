package com.example.hrms.attendance.model;

import com.example.hrms.employeeDetails.model.OnboardingInfoDetails;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long organizationId;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "department")
    private Set<OnboardingInfoDetails> employees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_policy_id")
    private WorkingHoursPolicy defaultWorkingHoursPolicy;
}