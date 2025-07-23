package com.example.hrms.employeeDetails.service;

import com.example.hrms.employeeDetails.dtos.EmployeeDetailDto;
import com.example.hrms.employeeDetails.dtos.ManagerDetailsDto;
import com.example.hrms.employeeDetails.dtos.ManagerFilesDto;
import com.example.hrms.employeeDetails.enums.OnboardingStatus;
import com.example.hrms.employeeDetails.model.*;
import com.example.hrms.employeeDetails.repository.BasicDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeOnboardingService {

    private final BasicDetailsRepository basicRepo;

    @Transactional
    public void saveEmployeeDetails(EmployeeDetailDto dto, String userEmail, Map<String, MultipartFile> files) throws IOException {
        // 1. Find the EXISTING parent record created by the manager.
        BasicDetails employee = basicRepo.findByUser_Email(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Onboarding not initiated for user: " + userEmail));

        // 2. Update the primitive fields on the main employee object from the DTO.
        BasicDetails dtoBasic = dto.getBasicDetails();
        employee.setMobileNumber(dtoBasic.getMobileNumber());
        employee.setPersonalEmail(dtoBasic.getPersonalEmail());
        employee.setAlternateNumber(dtoBasic.getAlternateNumber());
        employee.setDob(dtoBasic.getDob());
        employee.setGender(dtoBasic.getGender());
        employee.setMaritalStatus(dtoBasic.getMaritalStatus());
        employee.setNationality(dtoBasic.getNationality());
        employee.setBloodGroup(dtoBasic.getBloodGroup());

        // 3. Create/Update child entities, link them to the parent, and set them on the parent.

        // --- One-to-One Relationships ---

        // Address Details (no files)
        if (dto.getAddressDetails() != null) {
            AddressDetails address = dto.getAddressDetails();
            address.setBasicDetails(employee);
            employee.setAddressDetails(address);
        }

        // Bank Details (has one static file)
        if (dto.getBankDetails() != null) {
            BankDetails bank = dto.getBankDetails();
            MultipartFile chequeFile = files.get("cancelledChequeFile");
            if (chequeFile != null && !chequeFile.isEmpty()) {
                bank.setCancelledCheque(chequeFile.getBytes());
            }
            bank.setBasicDetails(employee);
            employee.setBankDetails(bank);
        }

        // Identification Details (has multiple static files)
        if (dto.getIdentificationDetails() != null) {
            IdentificationDetails id = dto.getIdentificationDetails();
            MultipartFile aadhaarFile = files.get("aadhaarFile");
            MultipartFile panFile = files.get("panFile");
            MultipartFile passportFile = files.get("passportFile"); // Optional

            if (aadhaarFile != null && !aadhaarFile.isEmpty()) id.setAadhaarFile(aadhaarFile.getBytes());
            if (panFile != null && !panFile.isEmpty()) id.setPanFile(panFile.getBytes());
            if (passportFile != null && !passportFile.isEmpty()) id.setPassportFile(passportFile.getBytes());

            id.setBasicDetails(employee);
            employee.setIdentificationDetails(id);
        }

        // Family Info Details (has one optional static file)
        if (dto.getFamilyInfo() != null) {
            FamilyInfoDetails family = dto.getFamilyInfo();
            MultipartFile dependentsFile = files.get("dependentsInfoFile");
            if (dependentsFile != null && !dependentsFile.isEmpty()) {
                family.setDependentsInfoFile(dependentsFile.getBytes());
            }
            family.setBasicDetails(employee);
            employee.setFamilyInfoDetails(family);
        }

        // Profile Info Details (has one static file)
        if (dto.getProfileInfo() != null) {
            ProfileInfoDetails profile = dto.getProfileInfo();
            MultipartFile profilePic = files.get("profilePic");
            if(profilePic != null && !profilePic.isEmpty()) {
                profile.setProfilePicture(profilePic.getBytes());
            }
            profile.setBasicDetails(employee);
            employee.setProfileInfoDetails(profile);
        }

        // --- One-to-Many Relationships ---

        // Education Details (List with dynamic files)
        if (dto.getEducationDetails() != null && !dto.getEducationDetails().isEmpty()) {
            employee.getEducationDetails().clear();
            for (EducationDetails edu : dto.getEducationDetails()) {
                String keyPrefix = edu.getFinalKey();
                if (keyPrefix == null || keyPrefix.isBlank()) continue;

                MultipartFile degreeFile = files.get(keyPrefix + "_degree");
                MultipartFile marksheetFile = files.get(keyPrefix + "_marks");
                MultipartFile certsFile = files.get(keyPrefix + "_certs");

                if (degreeFile != null) edu.setDegreeCertificate(degreeFile.getBytes());
                if (marksheetFile != null) edu.setMarksheets(marksheetFile.getBytes());
                if (certsFile != null) edu.setUploadedCertifications(certsFile.getBytes());

                edu.setBasicDetails(employee);
                employee.getEducationDetails().add(edu);
            }
        }

        // Work Experience (List with dynamic files)
        if(dto.getWorkExperiences() != null && !dto.getWorkExperiences().isEmpty()){
            employee.getWorkExperiences().clear();
            for(WorkExperience work : dto.getWorkExperiences()){
                String keyPrefix = work.getFinalKey(); // Requires adding @Transient finalKey to WorkExperience model
                if (keyPrefix == null || keyPrefix.isBlank()) continue;

                MultipartFile relievingFile = files.get(keyPrefix + "_relieving");
                MultipartFile experienceFile = files.get(keyPrefix + "_experience");
                MultipartFile payslipsFile = files.get(keyPrefix + "_payslips");

                if (relievingFile != null) work.setRelievingLetter(relievingFile.getBytes());
                if (experienceFile != null) work.setExperienceLetter(experienceFile.getBytes());
                if (payslipsFile != null) work.setPayslips(payslipsFile.getBytes());

                work.setBasicDetails(employee);
                employee.getWorkExperiences().add(work);
            }
        }

        // Dynamic Field Values (List, no files)
        if (dto.getDynamicFieldValues() != null && !dto.getDynamicFieldValues().isEmpty()) {
            employee.getDynamicFieldValues().clear();

            for (DynamicFieldValue dfv : dto.getDynamicFieldValues()) {
                // Link the "answer" to the employee first
                dfv.setBasicDetails(employee);

                // This assumes the DTO provides enough information to know the field's type.
                DynamicFieldDefinition definition = dfv.getFieldDefinition();

                // Check if this dynamic field is defined as a 'file' type.
                if (definition != null && "file".equalsIgnoreCase(definition.getFieldType())) {

                    // A. This is a FILE field.
                    String keyPrefix = dfv.getFinalKey();
                    if (keyPrefix != null && !keyPrefix.isBlank()) {

                        // Construct the unique file key (e.g., "dyn_0_file")
                        String fileKey = keyPrefix + "_file";
                        MultipartFile file = files.get(fileKey);

                        if (file != null && !file.isEmpty()) {
                            // 1. The 'value' field stores the original filename as a text reference.
                            dfv.setValue(file.getOriginalFilename());

                            // 2. Create the separate DynamicFieldFile entity to hold the actual file bytes.
                            DynamicFieldFile dynamicFile = new DynamicFieldFile();
                            dynamicFile.setData(file.getBytes());
                            dynamicFile.setOriginalFilename(file.getOriginalFilename());
                            dynamicFile.setContentType(file.getContentType());

                            // 3. Create the crucial bidirectional link between the answer and its file.
                            dynamicFile.setDynamicFieldValue(dfv);
                            dfv.setFile(dynamicFile);

                            // NOTE: We don't need to save `dynamicFile` separately because
                            // the CascadeType.ALL on the 'file' relationship in DynamicFieldValue will handle it.
                        }
                    }
                }
                // B. This is a TEXT field. The 'value' is already set correctly from the JSON.
                // No extra logic is needed.

                employee.getDynamicFieldValues().add(dfv);
            }
        }

        // 4. Update the overall status of the process
        employee.setOnboardingStatus(OnboardingStatus.PENDING_MANAGER_REVIEW);

        // 5. ONE SAVE CALL to persist all changes and new child entities.
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
            for (DynamicFieldValue dfv : dto.getDynamicFields()) {
                dfv.setBasicDetails(employee);
                employee.getDynamicFieldValues().add(dfv);
            }
        }



        employee.setOnboardingStatus(OnboardingStatus.COMPLETED);
        basicRepo.save(employee);
    }
}