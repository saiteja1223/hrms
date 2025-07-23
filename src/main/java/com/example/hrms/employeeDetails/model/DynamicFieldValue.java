package com.example.hrms.employeeDetails.model;

import com.example.hrms.employeeDetails.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dynamic_field_values")
@Data
public class DynamicFieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient // For linking files during the API call. Not saved to DB.
    private String finalKey;

    // The single, correct mapping to the "Question"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_definition_id", nullable = false)
    private DynamicFieldDefinition fieldDefinition;

    // The single, correct mapping to the "Employee"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_details_id", nullable = false)
    @JsonIgnore
    private BasicDetails basicDetails;

    @Lob
    private String value;  // The employee's answer to the question

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    // This is the new relationship to the file data. It will be null for text fields.
    @OneToOne(mappedBy = "dynamicFieldValue", cascade = CascadeType.ALL, orphanRemoval = true)
    private DynamicFieldFile file;

    // Note: The redundant 'employeeId' and duplicate 'fieldDefinition' fields have been removed.
}