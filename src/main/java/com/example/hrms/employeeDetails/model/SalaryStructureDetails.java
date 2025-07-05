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
@Table(name = "employee_salary_details")
public class SalaryStructureDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double baseSalary;

    @Column(nullable = false)
    private Double hra;

    @Column(nullable = false)
    private Double allowances;

    private Double bonus;        // Optional
    private Double deductions;   // Optional

    @Column(nullable = false)
    private Double ctc;          // Auto-calculated (set in service/controller)

    @Column(nullable = false)
    private LocalDate effectiveDate;

    @Lob
    @Column(nullable = false)
    private byte[] salaryStructurePdf;
}

