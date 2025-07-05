package com.example.hrms.employeeDetails.controller;

import com.example.hrms.employeeDetails.dtos.EmployeeDetailDto;
import com.example.hrms.employeeDetails.dtos.ManagerDetailsDto;
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

    @PostMapping(value = "/detailsByEmployee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> submitEmployeeDetails(
            @RequestParam("dto") String dtoJson,
            @RequestParam("degreeCertificate") MultipartFile degreeCertificate,
            @RequestParam("marksheets") MultipartFile marksheets,
            @RequestParam(value = "uploadedCertifications", required = false) MultipartFile uploadedCertifications,
            @RequestParam("relievingLetter") MultipartFile relievingLetter,
            @RequestParam("experienceLetter") MultipartFile experienceLetter,
            @RequestParam("payslips") MultipartFile payslips,
            @RequestParam("aadhaarFile") MultipartFile aadhaarFile,
            @RequestParam("panFile") MultipartFile panFile,
            @RequestParam(value = "passportFile", required = false) MultipartFile passportFile,
            @RequestParam(value = "dependentsInfoFile", required = false) MultipartFile dependentsInfoFile,
            @RequestParam("cancelledChequeFile") MultipartFile cancelledChequeFile,
            Principal principal
    ) throws IOException {
        EmployeeDetailDto dto = objectMapper.readValue(dtoJson, EmployeeDetailDto.class);
        employeeService.saveEmployeeDetails(dto, degreeCertificate, marksheets, uploadedCertifications,
                relievingLetter, experienceLetter, payslips, aadhaarFile, panFile, passportFile,
                dependentsInfoFile, cancelledChequeFile, principal.getName());
        return ResponseEntity.ok("Employee details added by employee successfully");
    }

    @PostMapping(value = "/detailsByManager/{empId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> submitManagerDetails(
            @RequestParam("dto") String dtoJson,
            @RequestParam("offerLetter") MultipartFile offerLetter,
            @RequestParam("signedNda") MultipartFile signedNda,
            @RequestParam(value = "joiningKit", required = false) MultipartFile joiningKit,
            @RequestParam("salaryStructurePdf") MultipartFile salaryStructurePdf,
            @PathVariable Long empId
    ) throws IOException {
        ManagerDetailsDto dto = objectMapper.readValue(dtoJson, ManagerDetailsDto.class);
        employeeService.saveManagerDetails(dto, offerLetter, signedNda, joiningKit,salaryStructurePdf, empId);
        return ResponseEntity.ok("Employee details added by manager successfully");
    }
}
