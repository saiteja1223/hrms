package com.example.hrms.employeeDetails.service;

import com.example.hrms.auth.enums.Role;
import com.example.hrms.employeeDetails.dtos.DynamicFieldDefinitionDto;
import com.example.hrms.employeeDetails.model.DynamicFieldDefinition;
import com.example.hrms.employeeDetails.repository.DynamicFieldDefinitionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamicFieldDefinitionService {

    private final DynamicFieldDefinitionRepository definitionRepo;

    /**
     * Creates a new dynamic field definition.
     * The role of the creator and the organization ID are passed in for ownership and security.
     */
    @Transactional
    public DynamicFieldDefinition createFieldDefinition(DynamicFieldDefinitionDto dto, Long organizationId, Role creatorRole) {
        DynamicFieldDefinition definition = new DynamicFieldDefinition();
        definition.setFieldName(dto.getFieldName());
        definition.setFieldType(dto.getFieldType());
        definition.setRequired(dto.isRequired());
        definition.setOptions(dto.getOptions());
        definition.setOrganizationId(organizationId);
        definition.setCreatedByRole(creatorRole);

        return definitionRepo.save(definition);
    }

    /**
     * Retrieves all dynamic field definitions for a specific organization.
     * This is the method your frontend would call to build the onboarding form.
     */
    public List<DynamicFieldDefinition> getDefinitionsForOrganization(Long organizationId) {
        // You could add more complex logic here, e.g., also get global fields where orgId is null
        return definitionRepo.findByOrganizationId(organizationId);
    }

    /**
     * Retrieves a single definition by its ID.
     */
    public DynamicFieldDefinition getDefinitionById(Long id) {
        return definitionRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DynamicFieldDefinition not found with ID: " + id));
    }

    /**
     * Updates an existing dynamic field definition.
     */
    @Transactional
    public DynamicFieldDefinition updateFieldDefinition(Long id, DynamicFieldDefinitionDto dto) {
        DynamicFieldDefinition existingDefinition = getDefinitionById(id);

        existingDefinition.setFieldName(dto.getFieldName());
        existingDefinition.setFieldType(dto.getFieldType());
        existingDefinition.setRequired(dto.isRequired());
        existingDefinition.setOptions(dto.getOptions());

        return definitionRepo.save(existingDefinition);
    }

    /**
     * Deletes a dynamic field definition.
     */
    @Transactional
    public void deleteFieldDefinition(Long id) {
        if (!definitionRepo.existsById(id)) {
            throw new EntityNotFoundException("DynamicFieldDefinition not found with ID: " + id);
        }
        definitionRepo.deleteById(id);
    }
}