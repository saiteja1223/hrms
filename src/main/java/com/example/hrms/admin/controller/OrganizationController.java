package com.example.hrms.admin.controller;

import com.example.hrms.admin.dtos.OrganizationRegisterRequest;
import com.example.hrms.admin.model.Organization;
import com.example.hrms.admin.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class OrganizationController {

    @Autowired
    private OrganizationService orgService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-org")
    public ResponseEntity<Long> createOrganization(@RequestBody OrganizationRegisterRequest req) {
        Long orgId = orgService.createOrganization(req);
        return ResponseEntity.ok(orgId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrganisationById(@PathVariable Long id){
        String messege=orgService.deleteOrgById(id);
        return new ResponseEntity<>(messege, HttpStatus.OK) ;

    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/organization/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable Long id) {
        return orgService.getOrganization(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}