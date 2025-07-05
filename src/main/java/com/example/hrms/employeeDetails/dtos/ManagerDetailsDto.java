package com.example.hrms.employeeDetails.dtos;

import com.example.hrms.employeeDetails.model.DynamicFieldValue;
import com.example.hrms.employeeDetails.model.OnboardingInfoDetails;
import com.example.hrms.employeeDetails.model.SalaryStructureDetails;
import lombok.Data;

import java.util.List;
@Data
public class ManagerDetailsDto {
    private SalaryStructureDetails salaryStructureDetails;
    private OnboardingInfoDetails onboardingInfoDetails;
    private List<DynamicFieldValue> dynamicFields;
}
