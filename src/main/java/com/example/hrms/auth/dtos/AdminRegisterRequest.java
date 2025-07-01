package com.example.hrms.auth.dtos;

import lombok.Data;

@Data
public class AdminRegisterRequest {
    private String email;
    private String password;
}
