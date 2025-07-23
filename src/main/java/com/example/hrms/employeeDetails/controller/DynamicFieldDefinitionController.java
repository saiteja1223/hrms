package com.example.hrms.employeeDetails.controller;

import com.example.hrms.auth.enums.Role;
import com.example.hrms.auth.model.User;
import com.example.hrms.auth.repository.UserRepository;
import com.example.hrms.employeeDetails.dtos.DynamicFieldDefinitionDto;
import com.example.hrms.employeeDetails.model.DynamicFieldDefinition;
import com.example.hrms.employeeDetails.service.DynamicFieldDefinitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/dynamic-field-definitions")
@RequiredArgsConstructor
public class DynamicFieldDefinitionController {

    private final DynamicFieldDefinitionService definitionService;
    private final UserRepository userRepository;
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<DynamicFieldDefinition> createDefinition(
            @RequestBody @Valid DynamicFieldDefinitionDto dto,
            Principal principal // Use this to get user details
    ) {

         User user = userRepository.findByEmail(principal.getName()).orElseThrow(()->new RuntimeException("user dosent exist"));
        // In a real app, you would extract the orgId and role from the authenticated principal
        Long orgId = user.getOrgId(); // Placeholder: get from authentication.getPrincipal().getOrgId()
        Role role = user.getRole(); // Placeholder: get from authentication.getAuthorities()

        DynamicFieldDefinition createdDefinition = definitionService.createFieldDefinition(dto, orgId, role);
        return new ResponseEntity<>(createdDefinition, HttpStatus.CREATED);
    }

    /**
     * Public or Employee-facing endpoint to get all fields needed for an organization's form.
     */
    @GetMapping("/organization/{orgId}")
    @PreAuthorize("isAuthenticated()") // Any logged-in user can see the questions
    public ResponseEntity<List<DynamicFieldDefinition>> getDefinitionsByOrg(@PathVariable Long orgId) {
        List<DynamicFieldDefinition> definitions = definitionService.getDefinitionsForOrganization(orgId);
        return ResponseEntity.ok(definitions);
    }

    /**
     * Endpoint for an ADMIN or MANAGER to update an existing custom field.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<DynamicFieldDefinition> updateDefinition(
            @PathVariable Long id,
            @RequestBody @Valid DynamicFieldDefinitionDto dto
    ) {
        DynamicFieldDefinition updatedDefinition = definitionService.updateFieldDefinition(id, dto);
        return ResponseEntity.ok(updatedDefinition);
    }

    /**
     * Endpoint for an ADMIN or MANAGER to delete a custom field.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteDefinition(@PathVariable Long id) {
        definitionService.deleteFieldDefinition(id);
        return ResponseEntity.noContent().build();
    }
}