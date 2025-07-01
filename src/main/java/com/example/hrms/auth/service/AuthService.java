package com.example.hrms.auth.service;

import com.example.hrms.auth.enums.Role;
import com.example.hrms.auth.jwt.JwtUtil;
import com.example.hrms.auth.model.User;
import com.example.hrms.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    public String registerUser(String email, String password, Role role, Long orgId, Role creatorRole) throws AccessDeniedException {
        if (role == Role.ADMIN && orgId != null)
            throw new IllegalArgumentException("ADMIN should not have orgId");

       else if (role == Role.ORG_ADMIN && creatorRole != Role.ADMIN)
            throw new AccessDeniedException("Only ADMIN can create ORG_ADMIN");

       else if (role == Role.MANAGER && creatorRole != Role.ORG_ADMIN)
            throw new AccessDeniedException("Only ORG_ADMIN can create MANAGER");

       else if (role == Role.EMPLOYEE && creatorRole != Role.MANAGER)
            throw new AccessDeniedException("Only MANAGER can create EMPLOYEE");

        if (userRepo.existsByEmail(email))
            throw new RuntimeException("Email already registered");

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setOrgId(orgId);
        user.setIsActive(true);

        userRepo.save(user);
        return "User registered successfully";
    }

    public String login(String email, String password) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid email"));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Invalid credentials");

        return jwtUtil.generateToken(user);
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

