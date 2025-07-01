package com.example.hrms.auth.dtos;

import lombok.Data;

@Data
public class ManagerRegisterRequest {
    private String email;
    private String password;
}
