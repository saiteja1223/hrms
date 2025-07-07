package com.example.hrms.employeeDetails.service;

import com.example.hrms.employeeDetails.dtos.EmployeeDetailDto;
import com.example.hrms.employeeDetails.dtos.EmployeeFilesDto;
import com.example.hrms.employeeDetails.dtos.ManagerDetailsDto;
import com.example.hrms.employeeDetails.dtos.ManagerFilesDto;
import com.example.hrms.employeeDetails.enums.OnboardingStatus;
import com.example.hrms.employeeDetails.model.*;
import com.example.hrms.employeeDetails.repository.BasicDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor // Use modern constructor injection
public class EmployeeOnboardingService {

    // With cascading, we ONLY need the repository for the ROOT entity.
    private final BasicDetailsRepository basicRepo;

    @Transactional
    public void saveEmployeeDetails(EmployeeDetailDto dto, String userEmail, EmployeeFilesDto files) throws IOException {
        // 1. Find the EXISTING parent record created by the manager.
        BasicDetails employee = basicRepo.findByUser_Email(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Onboarding not initiated for user: " + userEmail));

        // 2. Update the primitive fields on the main employee object
        BasicDetails dtoBasic = dto.getBasicDetails();
        employee.setMobileNumber(dtoBasic.getMobileNumber());
        employee.setPersonalEmail(dtoBasic.getPersonalEmail());
        employee.setAlternateNumber(dtoBasic.getAlternateNumber());
        employee.setDob(dtoBasic.getDob());
        employee.setGender(dtoBasic.getGender());
        employee.setMaritalStatus(dtoBasic.getMaritalStatus());
        employee.setNationality(dtoBasic.getNationality());
        employee.setBloodGroup(dtoBasic.getBloodGroup());

        // 3. Create NEW child objects, link them to the parent, and set them on the parent.
        // This is what tells JPA to run INSERT statements for the children.

        // --- One-to-One Relationships ---

        // Address Details
        if (dto.getAddressDetails() != null) {
            AddressDetails address = dto.getAddressDetails();
            address.setBasicDetails(employee); // Link child to parent
            employee.setAddressDetails(address); // Link parent to child
        }

        // Bank Details
        if (dto.getBankDetails() != null) {
            BankDetails bank = dto.getBankDetails();
            bank.setCancelledCheque(files.cancelledChequeFile().getBytes());
            bank.setBasicDetails(employee);
            employee.setBankDetails(bank);
        }

        // Identification Details
        if (dto.getIdentificationDetails() != null) {
            IdentificationDetails id = dto.getIdentificationDetails();
            id.setAadhaarFile(files.aadhaarFile().getBytes());
            id.setPanFile(files.panFile().getBytes());
            if (files.passportFile() != null) {
                id.setPassportFile(files.passportFile().getBytes());
            }
            id.setBasicDetails(employee);
            employee.setIdentificationDetails(id);
        }

        // Family Info Details
        if (dto.getFamilyInfo() != null) {
            FamilyInfoDetails family = dto.getFamilyInfo();
            if (files.dependentsInfoFile() != null) {
                family.setDependentsInfoFile(files.dependentsInfoFile().getBytes());
            }
            family.setBasicDetails(employee);
            employee.setFamilyInfoDetails(family);
        }

        // Profile Info Details
        if (dto.getProfileInfo() != null) {
            ProfileInfoDetails profile = dto.getProfileInfo();
            // Assuming profilePicture is a required part of this DTO if it's not null
            profile.setProfilePicture(files.profilePic().getBytes());
            profile.setBasicDetails(employee);
            employee.setProfileInfoDetails(profile);
        }

        // --- One-to-Many Relationships ---

        // Education Details (List)
        if (dto.getEducationDetails() != null && !dto.getEducationDetails().isEmpty()) {
            employee.getEducationDetails().clear(); // Clear old list to handle updates correctly
            for (EducationDetails edu : dto.getEducationDetails()) {
                edu.setDegreeCertificate(files.degreeCertificate().getBytes());
                edu.setMarksheets(files.marksheets().getBytes());
                if (files.uploadedCertifications() != null) {
                    edu.setUploadedCertifications(files.uploadedCertifications().getBytes());
                }
                edu.setBasicDetails(employee); // Link each item in the list
                employee.getEducationDetails().add(edu);
            }
        }

        // Work Experience (List)
        if(dto.getWorkExperiences() != null && !dto.getWorkExperiences().isEmpty()){
            employee.getWorkExperiences().clear();
            for(WorkExperience work : dto.getWorkExperiences()){
                work.setRelievingLetter(files.relievingLetter().getBytes());
                work.setExperienceLetter(files.experienceLetter().getBytes());
                work.setPayslips(files.payslips().getBytes());
                work.setBasicDetails(employee);
                employee.getWorkExperiences().add(work);
            }
        }

        // Dynamic Field Values (List)
        if (dto.getDynamicFieldValues() != null && !dto.getDynamicFieldValues().isEmpty()) {
            employee.getDynamicFieldValues().clear();
            for (DynamicFieldValue dfv : dto.getDynamicFieldValues()) {
                // Here, we assume the DTO contains the definition ID and the value.
                // The service needs to fetch the actual definition if the DTO only has the ID.
                // For simplicity here, we assume the DTO has the full object, which is less ideal.
                dfv.setBasicDetails(employee);
                employee.getDynamicFieldValues().add(dfv);
            }
        }

        // 4. Update the overall status of the process
        employee.setOnboardingStatus(OnboardingStatus.PENDING_MANAGER_REVIEW);

        // 5. ONE SAVE CALL. This single line saves the updated parent AND
        //    inserts all the new children thanks to the cascade settings.
        basicRepo.save(employee);
    }

    @Transactional
    public void saveManagerDetails(ManagerDetailsDto dto, Long empId, ManagerFilesDto files) throws IOException {
        BasicDetails employee = basicRepo.findById(empId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + empId));

        // Create and link Salary Details
        if (dto.getSalaryStructureDetails() != null) {
            SalaryStructureDetails salary = dto.getSalaryStructureDetails();
            salary.setSalaryStructurePdf(files.salaryStructurePdf().getBytes());
            salary.setBasicDetails(employee);
            employee.setSalaryStructureDetails(salary);
        }

        // Create and link Onboarding Info
        if (dto.getOnboardingInfoDetails() != null) {
            OnboardingInfoDetails onboardingInfo = dto.getOnboardingInfoDetails();
            onboardingInfo.setOfferLetter(files.offerLetter().getBytes());
            onboardingInfo.setSignedNda(files.signedNda().getBytes());
            if (files.joiningKit() != null) {
                onboardingInfo.setJoiningKit(files.joiningKit().getBytes());
            }
            onboardingInfo.setBasicDetails(employee);
            employee.setOnboardingInfoDetails(onboardingInfo);
        }

        // Handle Manager-added Dynamic Fields
        if (dto.getDynamicFields() != null && !dto.getDynamicFields().isEmpty()) {
            // We assume manager fields are added to the same list.
            // A more complex design might have separate lists.
            for (DynamicFieldValue dfv : dto.getDynamicFields()) {
                dfv.setBasicDetails(employee);
                employee.getDynamicFieldValues().add(dfv);
            }
        }


        employee.setOnboardingStatus(OnboardingStatus.COMPLETED);

        basicRepo.save(employee);
    }
}