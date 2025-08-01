package com.example.hrms.attendance.repository;

import com.example.hrms.attendance.model.WorkingHoursPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingHoursPolicyRepository extends JpaRepository<WorkingHoursPolicy, Long> {
    List<WorkingHoursPolicy> findByOrganizationId(Long organizationId);
    Optional<WorkingHoursPolicy> findByOrganizationIdAndIsOrganizationDefaultTrue(Long organizationId);
}