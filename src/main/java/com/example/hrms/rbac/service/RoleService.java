package com.example.hrms.rbac.service;

import com.example.hrms.rbac.dtos.FullRoleRequest;
import com.example.hrms.rbac.model.DynamicRole;
import com.example.hrms.rbac.model.Permission;
import com.example.hrms.rbac.model.RolePermission;
import com.example.hrms.rbac.model.UserRole;
import com.example.hrms.rbac.repository.DynamicRoleRepository;
import com.example.hrms.rbac.repository.PermissionRepository;
import com.example.hrms.rbac.repository.RolePermissionRepository;
import com.example.hrms.rbac.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private DynamicRoleRepository roleRepo;
    @Autowired
    private PermissionRepository permRepo;
    @Autowired
    private RolePermissionRepository rolePermRepo;
    @Autowired
    private UserRoleRepository userRoleRepo;
    public void createRoleAndAssign(FullRoleRequest req,Long orgId) {
        // 1. Create Role
        DynamicRole role = new DynamicRole();
        role.setName(req.getRoleName());
        role.setOrgId(orgId);
        role = roleRepo.save(role); // Save and get ID

        // 2. Assign Permissions
        for (String permName : req.getPermissions()) {
            Permission perm = permRepo.findByName(permName)
                    .orElseGet(() -> permRepo.save(new Permission(null, permName)));
            rolePermRepo.save(new RolePermission(null, role.getId(), perm.getId()));
            System.out.println("Saving permission to role: " + permName + " -> RoleId: " + role.getId());
        }

        // 3. Assign Users to Role
        for (Long userId : req.getUserIds()) {
            userRoleRepo.save(new UserRole(null, userId, role.getId()));
            System.out.println("Assigning user to role: " + userId + " -> RoleId: " + role.getId());
        }



    }


    public List<DynamicRole> getRolesByOrgId(Long orgId) {
         return roleRepo.findAll().stream().filter(r->r.getOrgId().equals(orgId)).collect(Collectors.toList());

    }

    public List<Permission> getAllPermissions() {
        return permRepo.findAll();
    }

    public List<Permission> getPermissionsByRole(Long roleId) {
        List<Long> permId= rolePermRepo.findAll().stream().filter(r->r.getRoleId().equals(roleId))
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
        return permRepo.findAllById(permId);
    }

    public List<DynamicRole> getRolesByPermission(Long permId, Long orgId) {
        List <Long> rolePerm=rolePermRepo.findAll().stream().filter(r->r.getPermissionId().equals(permId))
                .map(RolePermission::getRoleId)
                .collect(Collectors.toList());
        return roleRepo.findAllById(rolePerm).stream().filter(r->r.getOrgId().equals(orgId))
                .collect(Collectors.toList());
    }

    public String deletePermissionFromRole(Long roleId,Long permId ) {
        List<RolePermission> matchingMappings = rolePermRepo.findAll().stream()
                .filter(rp -> rp.getRoleId().equals(roleId) && rp.getPermissionId().equals(permId))
                .collect(Collectors.toList());

        if (matchingMappings.isEmpty()) {
            return " No such permission mapped to this role.";
        }

        // 3. Delete mapping
        rolePermRepo.deleteAll(matchingMappings);
        return " Permission removed from role successfully.";


    }
    public void removeUserFromRole(Long roleId, Long userId) {
        List<UserRole> mappings = userRoleRepo.findAll().stream()
                .filter(ur -> ur.getRoleId().equals(roleId) && ur.getUserId().equals(userId))
                .collect(Collectors.toList());
        userRoleRepo.deleteAll(mappings);
    }


}
