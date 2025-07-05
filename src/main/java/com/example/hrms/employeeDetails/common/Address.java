package com.example.hrms.employeeDetails.common;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}