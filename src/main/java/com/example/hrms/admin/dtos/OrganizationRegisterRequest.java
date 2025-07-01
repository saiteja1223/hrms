package com.example.hrms.admin.dtos;

import lombok.Data;

@Data
public class OrganizationRegisterRequest {
    private String name;
    private String companyType;
    private String address;
    private String supportContact;
    private double userCharge;
    private String description;
}
