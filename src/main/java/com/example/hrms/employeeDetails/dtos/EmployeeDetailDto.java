package com.example.hrms.employeeDetails.dtos;

import com.example.hrms.employeeDetails.model.*;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDetailDto {
    private BasicDetails basicDetails;
    private AddressDetails addressDetails;
    private List<EducationDetails> educationDetails;
    private List<WorkExperience> workExperiences;
    private IdentificationDetails identificationDetails;
    private FamilyInfoDetails familyInfo;
    private BankDetails bankDetails;
    private List<DynamicFieldValue> dynamicFieldValue;
    private ProfileInfoDetails profileInfo;

}
