package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.DynamicFieldDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicFieldDefinitionRepository extends JpaRepository<DynamicFieldDefinition, Long> {
    // This allows you to find all custom fields for a specific organization
    List<DynamicFieldDefinition> findByOrganizationId(Long organizationId);

    // This allows you to find fields created by a specific role
    List<DynamicFieldDefinition> findByCreatedByRole(String role);

}
