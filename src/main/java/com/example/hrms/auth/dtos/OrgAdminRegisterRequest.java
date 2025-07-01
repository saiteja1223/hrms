package com.example.hrms.auth.dtos;

import lombok.Data;

@Data
public class OrgAdminRegisterRequest {
    private String email;
    private String password;
    private Long orgId;

}
