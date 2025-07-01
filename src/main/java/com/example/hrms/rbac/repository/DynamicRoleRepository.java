package com.example.hrms.rbac.repository;

import com.example.hrms.rbac.model.DynamicRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DynamicRoleRepository extends JpaRepository<DynamicRole, Long> {
    List<DynamicRole> findByOrgId(Long orgId);
    Optional<DynamicRole> findByNameAndOrgId(String name, Long orgId);
}
