package com.example.hrms.attendance.service;

import com.example.hrms.attendance.dto.HolidayDto;
import com.example.hrms.attendance.dto.WorkingHoursPolicyDto;
import com.example.hrms.attendance.model.Department;
import com.example.hrms.attendance.model.Holiday;
import com.example.hrms.attendance.model.WorkingHoursPolicy;
import com.example.hrms.attendance.repository.DepartmentRepository;
import com.example.hrms.attendance.repository.HolidayRepository;
import com.example.hrms.attendance.repository.WorkingHoursPolicyRepository;
import com.example.hrms.employeeDetails.model.BasicDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceAdminService {

    private final WorkingHoursPolicyRepository policyRepo;
    private final HolidayRepository holidayRepo;
    private final DepartmentRepository departmentRepo;

    // Creates a new "work week" template (e.g., Mon-Fri, 9-5) for an organization.
    @Transactional
    public WorkingHoursPolicy createWorkingHoursPolicy(WorkingHoursPolicyDto dto, Long organizationId) {
        WorkingHoursPolicy policy = new WorkingHoursPolicy();
        policy.setOrganizationId(organizationId);
        policy.setPolicyName(dto.getPolicyName());
        policy.setWorkingDays(dto.getWorkingDays());
        policy.setStartTime(dto.getStartTime());
        policy.setEndTime(dto.getEndTime());
        policy.setOrganizationDefault(dto.isOrganizationDefault());

        // Logic to ensure only one default policy per org
        if (dto.isOrganizationDefault()) {
            policyRepo.findByOrganizationIdAndIsOrganizationDefaultTrue(organizationId)
                    .ifPresent(oldDefault -> {
                        oldDefault.setOrganizationDefault(false);
                        policyRepo.save(oldDefault);
                    });
        }

        return policyRepo.save(policy);
    }

    // Creates a new public holiday for the company calendar.
    @Transactional
    public Holiday createHoliday(HolidayDto dto, Long organizationId) {
        if (holidayRepo.existsByOrganizationIdAndHolidayDate(organizationId, dto.getHolidayDate())) {
            throw new IllegalStateException("A holiday for this date already exists.");
        }
        Holiday holiday = new Holiday();
        holiday.setOrganizationId(organizationId);
        holiday.setHolidayDate(dto.getHolidayDate());
        holiday.setHolidayName(dto.getHolidayName());
        return holidayRepo.save(holiday);
    }

    // Assigns a specific work policy to be the default for an entire Department.
    @Transactional
    public Department assignPolicyToDepartment(Long policyId, Long departmentId) {
        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with ID: " + departmentId));
        WorkingHoursPolicy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new EntityNotFoundException("WorkingHoursPolicy not found with ID: " + policyId));

        department.setDefaultWorkingHoursPolicy(policy);
        return departmentRepo.save(department);
    }

    // This is the key logic to find the correct set of work rules for any given employee.
    public WorkingHoursPolicy resolvePolicyForEmployee(BasicDetails employee) {
        // Priority 1: Check for a policy assigned directly to the employee.
        if (employee.getAssignedWorkingHoursPolicy() != null) {
            return employee.getAssignedWorkingHoursPolicy();
        }
        // Priority 2: Check for a policy assigned to the employee's department.
        Department dept = employee.getOnboardingInfoDetails().getDepartment();
        if (dept != null && dept.getDefaultWorkingHoursPolicy() != null) {
            return dept.getDefaultWorkingHoursPolicy();
        }
        // Priority 3: Fall back to the organization-wide default policy.
        return policyRepo.findByOrganizationIdAndIsOrganizationDefaultTrue(employee.getUser().getOrgId())
                .orElseThrow(() -> new IllegalStateException("No default working hours policy is configured for the organization."));
    }
}