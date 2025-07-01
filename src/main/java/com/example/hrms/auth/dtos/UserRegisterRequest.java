package com.example.hrms.auth.dtos;

import com.example.hrms.auth.enums.Role;
import lombok.Data;

@Data
public class UserRegisterRequest {
    private String email;
    private String password;
}
