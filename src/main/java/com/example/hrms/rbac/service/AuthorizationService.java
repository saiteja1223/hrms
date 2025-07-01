package com.example.hrms.rbac.service;

import com.example.hrms.auth.model.User;
import com.example.hrms.auth.repository.UserRepository;
import com.example.hrms.rbac.model.Permission;
import com.example.hrms.rbac.model.RolePermission;
import com.example.hrms.rbac.model.UserRole;
import com.example.hrms.rbac.repository.PermissionRepository;
import com.example.hrms.rbac.repository.RolePermissionRepository;
import com.example.hrms.rbac.repository.UserRoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("authzService")
public class AuthorizationService {

    @Autowired private UserRepository userRepo;
    @Autowired private UserRoleRepository userRoleRepo;
    @Autowired private RolePermissionRepository rolePermRepo;
    @Autowired private PermissionRepository permRepo;

    // Check if user has any one of the given permissions
    public boolean hasAnyPermission(Authentication auth, String... permissions) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) return false;

        List<Long> roleIds = userRoleRepo.findAll().stream()
                .filter(ur -> ur.getUserId().equals(user.getId()))
                .map(UserRole::getRoleId)
                .toList();

        List<Long> permIds = rolePermRepo.findAll().stream()
                .filter(rp -> roleIds.contains(rp.getRoleId()))
                .map(RolePermission::getPermissionId)
                .toList();

        List<String> userPerms = permRepo.findAllById(permIds).stream()
                .map(Permission::getName)
                .toList();

        for (String perm : permissions) {
            if (userPerms.contains(perm)) return true;
        }
        return false;
    }

    // Optional: Check if user has all required permissions
    public boolean hasAllPermissions(Authentication auth, String... permissions) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) return false;

        List<Long> roleIds = userRoleRepo.findAll().stream()
                .filter(ur -> ur.getUserId().equals(user.getId()))
                .map(UserRole::getRoleId)
                .toList();

        List<Long> permIds = rolePermRepo.findAll().stream()
                .filter(rp -> roleIds.contains(rp.getRoleId()))
                .map(RolePermission::getPermissionId)
                .toList();

        List<String> userPerms = permRepo.findAllById(permIds).stream()
                .map(Permission::getName)
                .toList();

        return Arrays.stream(permissions).allMatch(userPerms::contains);
    }

}