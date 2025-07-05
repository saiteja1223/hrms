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
public class DynamicFieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DynamicFieldDefinition fieldDefinition;

    private Long employeeId;

    @Lob
    private String value;  // e.g. "Java,React", or a single LinkedIn URL, etc.
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
}
