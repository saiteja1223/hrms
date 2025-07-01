package com.example.hrms.rbac.dtos;

import lombok.Data;

import java.util.List;

@Data
public class FullRoleRequest {
    private String roleName;
    private Long orgId;
    private List<String> permissions;
    private List<Long> userIds;
}
