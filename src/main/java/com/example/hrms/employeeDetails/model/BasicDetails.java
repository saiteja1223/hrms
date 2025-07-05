package com.example.hrms.employeeDetails.model;

import com.example.hrms.employeeDetails.enums.BloodGroup;
import com.example.hrms.employeeDetails.enums.Gender;
import com.example.hrms.employeeDetails.enums.MaritalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee-basic_details")
public class BasicDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id;
    private String FullName;
    private String Email;
    private String MobileNumber;
    private String AlternateNumber;
    private Data DOB;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    private  String Nationality;
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;



}
