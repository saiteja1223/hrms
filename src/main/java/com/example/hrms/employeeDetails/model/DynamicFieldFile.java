package com.example.hrms.employeeDetails.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dynamic_field_files")
@Data
public class DynamicFieldFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This creates the link back to the specific "answer" this file belongs to.
    @OneToOne
    @JoinColumn(name = "dynamic_field_value_id", nullable = false)
    private DynamicFieldValue dynamicFieldValue;

    private String originalFilename;
    private String contentType;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB") // Ensure the column is large enough
    private byte[] data;
}