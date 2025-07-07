package com.example.hrms.employeeDetails.dtos;

import org.springframework.web.multipart.MultipartFile;

public record EmployeeFilesDto(MultipartFile degreeCertificate,
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
                               MultipartFile profilePic) {
}
