package com.example.hrms.attendance.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "working_hours_policies")
@Data
public class WorkingHoursPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long organizationId;

    @Column(nullable = false)
    private String policyName;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER) // A set of days like {MONDAY, TUESDAY, ...}
    @CollectionTable(name="policy_working_days", joinColumns=@JoinColumn(name="policy_id"))
    @Column(name="day_of_week")
    private Set<DayOfWeek> workingDays;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    // This flag marks the fallback policy for the entire organization
    private boolean isOrganizationDefault;

    @OneToMany(mappedBy = "defaultWorkingHoursPolicy", fetch = FetchType.LAZY)
    private Set<Department> departmentsWithThisPolicy = new HashSet<>();
}