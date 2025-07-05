package com.example.hrms.employeeDetails.service;

import com.example.hrms.employeeDetails.dtos.EmployeeDetailDto;
import com.example.hrms.employeeDetails.dtos.ManagerDetailsDto;
import com.example.hrms.employeeDetails.model.*;
import com.example.hrms.employeeDetails.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmployeeOnboardingService {

    @Autowired private BasicDetailsRepository basicRepo;
    @Autowired private AddressDetailsRepository addressRepo;
    @Autowired private EducationDetailsRepository educationRepo;
    @Autowired private WorkExperienceRepository workRepo;
    @Autowired private IdentificationDetailsRepository idRepo;
    @Autowired private FamilyInfoDetailsRepository familyRepo;
    @Autowired private BankDetailsRepository bankRepo;
    @Autowired private ProfileInfoDetailsRepository profileRepo;
    @Autowired private DynamicFieldValueRepository dynamicRepo;
    @Autowired private SalaryStructureDetailsRepository salaryRepo;
    @Autowired private OnboardingInfoDetailsRepository onboardingRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void saveEmployeeDetails(EmployeeDetailDto dto,
                                    MultipartFile degreeCertificate,
                                    MultipartFile marksheets,
                                    MultipartFile uploadedCertifications,
                                    MultipartFile relievingLetter,
                                    MultipartFile experienceLetter,
                                    MultipartFile payslips,
                                    MultipartFile aadhaarFile,
                                    MultipartFile panFile,
                                    MultipartFile passportFile,
                                    MultipartFile dependentsInfoFile,
                                    MultipartFile cancelledChequeFile,
                                    String emailOverride) throws IOException {



        BasicDetails dtoBasic = dto.getBasicDetails();
        BasicDetails basic = new BasicDetails();
        basic.setFullName(dtoBasic.getFullName());
        basic.setEmail(emailOverride);
        basic.setMobileNumber(dtoBasic.getMobileNumber());
        basic.setAlternateNumber(dtoBasic.getAlternateNumber());
        basic.setDOB(dtoBasic.getDOB());
        basic.setGender(dtoBasic.getGender());
        basic.setMaritalStatus(dtoBasic.getMaritalStatus());
        basic.setNationality(dtoBasic.getNationality());
        basic.setBloodGroup(dtoBasic.getBloodGroup());
        basicRepo.save(basic);

        AddressDetails dtoAddress = dto.getAddressDetails();
        AddressDetails address = new AddressDetails();
        address.setCurrentAddress(dtoAddress.getCurrentAddress());
        address.setPermanentAddress(dtoAddress.getPermanentAddress());
        addressRepo.save(address);

        for (EducationDetails edu : dto.getEducationDetails()) {
            EducationDetails education = new EducationDetails();
            education.setHighestQualification(edu.getHighestQualification());
            education.setUniversityName(edu.getUniversityName());
            education.setYearOfPassing(edu.getYearOfPassing());
            education.setOtherDegrees(edu.getOtherDegrees());
            education.setCertifications(edu.getCertifications());
            education.setDegreeCertificate(degreeCertificate.getBytes());
            education.setMarksheets(marksheets.getBytes());
            education.setUploadedCertifications(uploadedCertifications != null ? uploadedCertifications.getBytes() : null);
            education.setApplicationStatus(edu.getApplicationStatus());
            educationRepo.save(education);
        }

        for (WorkExperience workExp : dto.getWorkExperiences()) {
            WorkExperience work = new WorkExperience();
            work.setCompanyName(workExp.getCompanyName());
            work.setDesignation(workExp.getDesignation());
            work.setStartDate(workExp.getStartDate());
            work.setEndDate(workExp.getEndDate());
            work.setReasonForLeaving(workExp.getReasonForLeaving());
            work.setRelievingLetter(relievingLetter.getBytes());
            work.setExperienceLetter(experienceLetter.getBytes());
            work.setPayslips(payslips.getBytes());
            work.setApplicationStatus(workExp.getApplicationStatus());
            workRepo.save(work);
        }

        IdentificationDetails dtoId = dto.getIdentificationDetails();
        IdentificationDetails id = new IdentificationDetails();
        id.setAadhaarNumber(dtoId.getAadhaarNumber());
        id.setPanNumber(dtoId.getPanNumber());
        id.setPassportNumber(dtoId.getPassportNumber());
        id.setVoterId(dtoId.getVoterId());
        id.setDrivingLicense(dtoId.getDrivingLicense());
        id.setAadhaarFile(aadhaarFile.getBytes());
        id.setPanFile(panFile.getBytes());
        id.setPassportFile(passportFile != null ? passportFile.getBytes() : null);
        id.setApplicationStatus(dtoId.getApplicationStatus());
        idRepo.save(id);

        FamilyInfoDetails dtoFamily = dto.getFamilyInfo();
        FamilyInfoDetails family = new FamilyInfoDetails();
        family.setParentName(dtoFamily.getParentName());
        family.setSpouseName(dtoFamily.getSpouseName());
        family.setEmergencyContactName(dtoFamily.getEmergencyContactName());
        family.setEmergencyContactRelation(dtoFamily.getEmergencyContactRelation());
        family.setEmergencyContactNumber(dtoFamily.getEmergencyContactNumber());
        family.setDependentsInfoFile(dependentsInfoFile != null ? dependentsInfoFile.getBytes() : null);
        family.setApplicationStatus(dtoFamily.getApplicationStatus());
        familyRepo.save(family);

        BankDetails dtoBank = dto.getBankDetails();
        BankDetails bank = new BankDetails();
        bank.setBankName(dtoBank.getBankName());
        bank.setAccountNumber(dtoBank.getAccountNumber());
        bank.setIfscCode(dtoBank.getIfscCode());
        bank.setBranchName(dtoBank.getBranchName());
        bank.setUan(dtoBank.getUan());
        bank.setPfNumber(dtoBank.getPfNumber());
        bank.setEsiNumber(dtoBank.getEsiNumber());
        bank.setCancelledCheque(cancelledChequeFile.getBytes());
        bank.setApplicationStatus(dtoBank.getApplicationStatus());
        bankRepo.save(bank);

        if (dto.getProfileInfo() != null) {
            profileRepo.save(dto.getProfileInfo());
        }

        if (dto.getDynamicFieldValue() != null) {
            for (DynamicFieldValue field : dto.getDynamicFieldValue()) {
                dynamicRepo.save(field);
            }
        }
    }

    @Transactional
    public void saveManagerDetails(ManagerDetailsDto dto,MultipartFile offerLetter,
                                   MultipartFile signedNda,
                                   MultipartFile joiningKit,
                                   MultipartFile salaryStructurePdf,
                                   Long empId) {
        SalaryStructureDetails dtoSalary = dto.getSalaryStructureDetails();
        SalaryStructureDetails salary = new SalaryStructureDetails();
        salary.setBaseSalary(dtoSalary.getBaseSalary());
        salary.setHra(dtoSalary.getHra());
        salary.setAllowances(dtoSalary.getAllowances());
        salary.setBonus(dtoSalary.getBonus());
        salary.setDeductions(dtoSalary.getDeductions());
        salary.setEffectiveDate(dtoSalary.getEffectiveDate());
        salary.setSalaryStructurePdf(dtoSalary.getSalaryStructurePdf());
        salary.setCtc(salary.getBaseSalary() + salary.getHra() + salary.getAllowances()
                + (salary.getBonus() != null ? salary.getBonus() : 0.0)
                - (salary.getDeductions() != null ? salary.getDeductions() : 0.0));
        salaryRepo.save(salary);

        OnboardingInfoDetails dtoOnboarding = dto.getOnboardingInfoDetails();
        OnboardingInfoDetails onboarding = new OnboardingInfoDetails();
        onboarding.setDateOfJoining(dtoOnboarding.getDateOfJoining());
        onboarding.setDesignation(dtoOnboarding.getDesignation());
        onboarding.setDepartment(dtoOnboarding.getDepartment());
        onboarding.setReportingManager(dtoOnboarding.getReportingManager());
        onboarding.setOfferLetter(dtoOnboarding.getOfferLetter());
        onboarding.setSignedNda(dtoOnboarding.getSignedNda());
        onboarding.setJoiningKit(dtoOnboarding.getJoiningKit());
        onboardingRepo.save(onboarding);

        if (dto.getDynamicFields() != null) {
            for (DynamicFieldValue field : dto.getDynamicFields()) {
                dynamicRepo.save(field);
            }
        }
    }
}
