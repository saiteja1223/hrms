package com.example.hrms.admin.service;

import com.example.hrms.admin.dtos.OrganizationRegisterRequest;
import com.example.hrms.admin.model.Organization;
import com.example.hrms.admin.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository orgRepo;

    public Long createOrganization(OrganizationRegisterRequest req) {
        Organization org = new Organization();
        org.setName(req.getName());
        org.setCompanyType(req.getCompanyType());
        org.setAddress(req.getAddress());
        org.setSupportContact(req.getSupportContact());
        org.setUserCharge(req.getUserCharge());
        org.setDescription(req.getDescription());
        return orgRepo.save(org).getId();
    }

    public Optional<Organization> getOrganization(Long id) {
        return orgRepo.findById(id);
    }

    public String deleteOrgById(Long id) {
        if (orgRepo.findById(id).isPresent()){
            orgRepo.deleteById(id);
            return "success";
        }
        else {
            return "failed";
        }


    }
}
