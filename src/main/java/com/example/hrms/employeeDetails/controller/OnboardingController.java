package com.example.hrms.employeeDetails.controller;

import com.example.hrms.employeeDetails.dtos.EmployeeDetailDto;
import com.example.hrms.employeeDetails.dtos.ManagerDetailsDto;
import com.example.hrms.employeeDetails.dtos.ManagerFilesDto;
import com.example.hrms.employeeDetails.service.EmployeeOnboardingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest; // <-- Important import

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class OnboardingController {

    private final EmployeeOnboardingService employeeService;


    // We need ObjectMapper again because we are manually parsing the DTO from a string
    private final ObjectMapper objectMapper;

    // This is the updated and correct method for handling dynamic files
    @PutMapping(value = "/detailsByEmployee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> submitEmployeeDetails(
            MultipartHttpServletRequest request, // <-- 1. Accept the whole multipart request
            Principal principal
    ) throws IOException {
         System.out.println("request"+request);
        // 2. Manually get the JSON string part from the request
        String dtoJson = request.getParameter("dto");
        if (dtoJson == null) {
            return ResponseEntity.badRequest().body("Missing required 'dto' part in the form-data request.");
        }

        // 3. Manually parse the JSON string into your DTO object
        EmployeeDetailDto dto = objectMapper.readValue(dtoJson, EmployeeDetailDto.class);

        // 4. Get a Map of ALL uploaded files. The keys will be the unique names you send
        // from Postman (e.g., "education_0_degree", "profilePic", etc.)
        Map<String, MultipartFile> files = request.getFileMap();

        // 5. Pass the DTO and the complete file map to the service layer
        employeeService.saveEmployeeDetails(dto, principal.getName(), files);

        return ResponseEntity.ok("Your details have been submitted successfully for review.");
    }

    // ... your submitManagerDetails method can remain as it is if it doesn't have dynamic files ...


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
