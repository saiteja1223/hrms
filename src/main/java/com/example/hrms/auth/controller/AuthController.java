package com.example.hrms.auth.controller;

import com.example.hrms.auth.dtos.AdminRegisterRequest;
import com.example.hrms.auth.dtos.LoginRequest;
import com.example.hrms.auth.dtos.OrgAdminRegisterRequest;
import com.example.hrms.auth.dtos.UserRegisterRequest;
import com.example.hrms.auth.enums.Role;
import com.example.hrms.auth.model.User;
import com.example.hrms.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRegisterRequest req) throws AccessDeniedException {
        return ResponseEntity.ok(authService.registerUser(req.getEmail(), req.getPassword(), Role.ADMIN, null, Role.ADMIN));
    }

    @PostMapping("/org-admin/register")
    public ResponseEntity<String> registerOrgAdmin(@RequestBody OrgAdminRegisterRequest req,Principal principal) throws AccessDeniedException {
        User creator = authService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(authService.registerUser(req.getEmail(), req.getPassword(), Role.ORG_ADMIN, req.getOrgId(), creator.getRole()));
    }
    @PostMapping("/manager/register")
    public ResponseEntity<String> registerManager(@RequestBody OrgAdminRegisterRequest req,Principal principal) throws AccessDeniedException {
        User creator = authService.getUserByEmail(principal.getName());
        Long orgId = creator.getOrgId();
        return ResponseEntity.ok(authService.registerUser(req.getEmail(), req.getPassword(), Role.MANAGER,orgId , creator.getRole()));
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterRequest req, Principal principal) throws AccessDeniedException {
        User creator = authService.getUserByEmail(principal.getName());
        Long orgId = creator.getOrgId();
        return ResponseEntity.ok(authService.registerUser(req.getEmail(), req.getPassword(), Role.EMPLOYEE, orgId, creator.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }
}