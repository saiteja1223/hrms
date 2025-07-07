package com.example.hrms.employeeDetails.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_dynamicFields")
public class DynamicFieldDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fieldName;         // e.g. "Skills", "LinkedIn Profile"
    private String fieldType;         // e.g. "text", "textarea", "select", "multi-select"

    private boolean required;         // is field mandatory?

    private String options;           // comma-separated for dropdowns (e.g., "Java,React,Python")

    private String createdByRole;     // "ORG_ADMIN", "MANAGER", etc.

    private Long organizationId;      // Org-specific fields


}

