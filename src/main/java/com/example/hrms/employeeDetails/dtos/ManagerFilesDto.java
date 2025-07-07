package com.example.hrms.employeeDetails.dtos;

import org.springframework.web.multipart.MultipartFile;

public record ManagerFilesDto(MultipartFile offerLetter,
                              MultipartFile signedNda,
                              MultipartFile joiningKit,
                              MultipartFile salaryStructurePdf) {
}
