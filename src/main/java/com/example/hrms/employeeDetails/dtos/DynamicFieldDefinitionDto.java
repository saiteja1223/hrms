package com.example.hrms.employeeDetails.dtos;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class DynamicFieldDefinitionDto {

    @NotBlank(message = "Field name cannot be blank.")
    private String fieldName;

    @NotBlank(message = "Field type is required (e.g., text, select).")
    private String fieldType;

    private boolean required;

    private String options; // Comma-separated for select/multi-select types
}
