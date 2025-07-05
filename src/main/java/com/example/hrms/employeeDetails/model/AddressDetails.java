package com.example.hrms.employeeDetails.model;

import com.example.hrms.employeeDetails.common.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_address_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Address currentAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "perm_street")),
            @AttributeOverride(name = "city", column = @Column(name = "perm_city")),
            @AttributeOverride(name = "state", column = @Column(name = "perm_state")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "perm_zip")),
            @AttributeOverride(name = "country", column = @Column(name = "perm_country"))
    })
    private Address permanentAddress;

}
