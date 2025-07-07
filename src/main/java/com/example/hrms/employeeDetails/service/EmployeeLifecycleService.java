package com.example.hrms.employeeDetails.service;

import com.example.hrms.auth.model.User;
import com.example.hrms.auth.repository.UserRepository;
import com.example.hrms.employeeDetails.dtos.InitiateOnboardingDto;
import com.example.hrms.employeeDetails.enums.OnboardingStatus;
import com.example.hrms.employeeDetails.model.BasicDetails;
import com.example.hrms.employeeDetails.repository.BasicDetailsRepository;
import lombok.RequiredArgsConstructor; // BEST PRACTICE: Use this instead of @Autowired
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // BEST PRACTICE: Make service methods transactional

@Service
@RequiredArgsConstructor // This handles the @Autowired for you in a cleaner way
public class EmployeeLifecycleService {

    // final fields are injected by @RequiredArgsConstructor
    private final UserRepository userRepository;
    private final BasicDetailsRepository basicRepo;

    // It's a best practice to make methods that change data @Transactional
    @Transactional
    public BasicDetails initiateOnboarding(InitiateOnboardingDto dto) {
        // Find the User. NOTE: In a real system, the manager would create the user first
        // via your AuthService. This code assumes the User already exists.
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User with email " + dto.getEmail() + " does not exist. Please register the user first."));

        // You should definitely uncomment and use this check. It's critical.
        if (basicRepo.existsByUser(user)) {
            throw new IllegalStateException("Onboarding has already been initiated for user: " + dto.getEmail());
        }

        // Create the "shell" record
        BasicDetails employeeShell = new BasicDetails();
        employeeShell.setFullName(dto.getFullName());
        employeeShell.setOnboardingStatus(OnboardingStatus.INITIATED);

        // --- THIS IS THE KEY FIX - SETTING THE LINK ON BOTH SIDES ---
        // 1. Link Child to Parent (The "Owning" Side)
        employeeShell.setUser(user);
        // 2. Link Parent to Child (The "Inverse" Side - for object graph consistency)
        user.setBasicDetails(employeeShell);

        // Save the new employee record. Because of the cascade settings on User,
        // JPA will also see that the 'user' object is "dirty" and manage it correctly.
        return basicRepo.save(employeeShell);
    }
}