package com.example.hrms.auth.model;

import com.example.hrms.auth.enums.Role;
import com.example.hrms.employeeDetails.model.BasicDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String email;

        private String password;

        @Enumerated(EnumType.STRING)
        private Role role;

        private Long orgId; // nullable for Admin

        private Boolean isActive = true;

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private BasicDetails basicDetails;

}

