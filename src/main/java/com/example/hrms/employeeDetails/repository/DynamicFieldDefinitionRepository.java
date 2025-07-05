package com.example.hrms.employeeDetails.repository;

import com.example.hrms.employeeDetails.model.DynamicFieldDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicFieldDefinitionRepository extends JpaRepository<DynamicFieldDefinition, Long> {
}
