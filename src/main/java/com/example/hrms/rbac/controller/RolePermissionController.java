package com.example.hrms.rbac.controller;

import com.example.hrms.auth.model.User;
import com.example.hrms.rbac.dtos.FullRoleRequest;
import com.example.hrms.rbac.model.DynamicRole;
import com.example.hrms.rbac.model.Permission;
import com.example.hrms.rbac.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.hrms.auth.service.AuthService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolePermissionController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RoleService roleService;
    @PreAuthorize("hasRole('ORG_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createRoleWithPermissionsAndUsers(@RequestBody FullRoleRequest req, Principal principal) {
        User creator = authService.getUserByEmail(principal.getName());
        Long orgId = creator.getOrgId();
        roleService.createRoleAndAssign(req,orgId);
        return ResponseEntity.ok("Role, Permissions, and Users assigned successfully");
    }
    @PreAuthorize("hasRole('ORG_ADMIN') or @authzService.hasAllPermissions(authentication, 'VIEW_SALARY', 'VIEW_ROLES')")
    @GetMapping("/getRoles/by-org")
    public ResponseEntity<List<DynamicRole>> getRolesByOrgId( Principal principal){
        User creator = authService.getUserByEmail(principal.getName());
        Long orgId = creator.getOrgId();
        System.out.println(orgId+" "+creator);
        return ResponseEntity.ok(roleService.getRolesByOrgId(orgId));
    }
    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getAllPermissions(){

          return ResponseEntity.ok(roleService.getAllPermissions());
    }
   @GetMapping("/premissionByRole/{roleId}")
    public ResponseEntity<List<Permission>> getPermissionsByRole(@PathVariable Long roleId){
        return ResponseEntity.ok(roleService.getPermissionsByRole(roleId));
   }
   @GetMapping("/rolesByPermission/{permId}")
    public ResponseEntity<List<DynamicRole>>getRolesByPermission(@PathVariable Long permId,Principal principal){
       User creator = authService.getUserByEmail(principal.getName());
       Long orgId = creator.getOrgId();
        return ResponseEntity.ok(roleService.getRolesByPermission(permId,orgId));
   }
   @DeleteMapping("/deletePermFromRole/{roleId}/{permId}")
    public ResponseEntity<String> deletePermissionFromRole(@PathVariable Long roleId,@PathVariable Long permId){

       return ResponseEntity.ok(roleService.deletePermissionFromRole(roleId,permId));
   }
    @DeleteMapping("/deleteUserFromRole/{roleId}/users/{userId}")
    public ResponseEntity<String> removeUserFromRole(@PathVariable Long roleId, @PathVariable Long userId) {
        roleService.removeUserFromRole(roleId, userId);
        return ResponseEntity.ok("User Removed from Role");
    }
}
