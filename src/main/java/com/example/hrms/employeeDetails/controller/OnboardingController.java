package com.example.hrms.employeeDetails.controller;

import com.example.hrms.employeeDetails.dtos.*;
import com.example.hrms.employeeDetails.service.EmployeeLifecycleService;
import com.example.hrms.employeeDetails.service.EmployeeOnboardingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/profile")
public class OnboardingController {

    @Autowired
    private EmployeeOnboardingService employeeService;


    @Autowired
    private ObjectMapper objectMapper;



    // This should be a PUT or PATCH since it's updating an existing record
    @PutMapping(value = "/detailsByEmployee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> submitEmployeeDetails(
            // BEST PRACTICE: Use @RequestPart for both DTO and files
            @RequestPart("dto") EmployeeDetailDto dto,
            @RequestPart("degreeCertificate") MultipartFile degreeCertificate,
            @RequestPart("marksheets") MultipartFile marksheets,
            @RequestPart(value = "uploadedCertifications", required = false) MultipartFile uploadedCertifications,
            @RequestPart("relievingLetter") MultipartFile relievingLetter,
            @RequestPart("experienceLetter") MultipartFile experienceLetter,
            @RequestPart("payslips") MultipartFile payslips,
            @RequestPart("aadhaarFile") MultipartFile aadhaarFile,
            @RequestPart("panFile") MultipartFile panFile,
            @RequestPart(value = "passportFile", required = false) MultipartFile passportFile,
            @RequestPart(value = "dependentsInfoFile", required = false) MultipartFile dependentsInfoFile,
            @RequestPart("cancelledChequeFile") MultipartFile cancelledChequeFile,
            @RequestPart("profilePic") MultipartFile profilePic,
            Principal principal
    ) throws IOException {

        // Bundle files into a clean DTO
        var files = new EmployeeFilesDto(
                degreeCertificate, marksheets, uploadedCertifications, relievingLetter,
                experienceLetter, payslips, aadhaarFile, panFile, passportFile,
                dependentsInfoFile, cancelledChequeFile,profilePic
        );

        employeeService.saveEmployeeDetails(dto, principal.getName(), files);
        return ResponseEntity.ok("Your details have been submitted successfully for review.");
    }


    // This adds new data to an existing record
    @PostMapping(value = "/detailsByManager/{empId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> submitManagerDetails(
            @PathVariable Long empId,
            @RequestPart("dto") ManagerDetailsDto dto,
            @RequestPart("offerLetter") MultipartFile offerLetter,
            @RequestPart("signedNda") MultipartFile signedNda,
            @RequestPart(value = "joiningKit", required = false) MultipartFile joiningKit,
            @RequestPart("salaryStructurePdf") MultipartFile salaryStructurePdf
    ) throws IOException {

        var files = new ManagerFilesDto(offerLetter, signedNda, joiningKit, salaryStructurePdf);

          employeeService.saveManagerDetails(dto, empId, files);
        return ResponseEntity.ok("Manager details for employee ID " + empId + " have been successfully saved.");
    }

}
