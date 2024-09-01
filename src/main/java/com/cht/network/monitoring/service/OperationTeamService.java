package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryPK;
import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.domain.OperationTeamPK;
import com.cht.network.monitoring.dto.InventoryDto;
import com.cht.network.monitoring.dto.OperationTeamDto;
import com.cht.network.monitoring.repository.OperationTeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OperationTeamService {

    private static final Logger log = LoggerFactory.getLogger(OperationTeamService.class);

    private final OperationTeamRepository operationTeamRepository;

    public OperationTeamService(OperationTeamRepository operationTeamRepository) {
        this.operationTeamRepository = operationTeamRepository;
    }

    public Page<OperationTeamDto> findAll(String filter, Pageable pageable) {

        log.info("findAll {}", filter);
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<OperationTeam> page = operationTeamRepository.findAll(firstPageWithTwoElements);
        page = operationTeamRepository.findOperationTeamsByNameIsContainingOrCompanyIsContainingOrJobTitleIsContainingOrMobileIsContainingOrTelephoneIsContainingOrFaxIsContainingOrEmailIsContainingOrderByUpdatedAtDesc(
                filter, filter, filter, filter, filter, filter, filter, firstPageWithTwoElements
        );
        List<OperationTeamDto> dtos = new ArrayList<>();
        for(OperationTeam operationTeam : page) {
            OperationTeamDto dto = new OperationTeamDto();
            dto.setId(operationTeam.getId());
            dto.setName(operationTeam.getName());
            dto.setCompany(operationTeam.getCompany());
            dto.setJobTitle(operationTeam.getJobTitle());
            dto.setMobile(operationTeam.getMobile());
            dto.setTelephone(operationTeam.getTelephone());
            dto.setFax(operationTeam.getFax());
            dto.setEmail(operationTeam.getEmail());
            dto.setRemark(operationTeam.getRemark());
            dtos.add(dto);

            log.info("{}", operationTeam);
        }
        return new PageImpl<OperationTeamDto>(dtos, pageable, page.getTotalElements());
    }

    public OperationTeamDto findOne(String id, String filter) {
        OperationTeamPK pk = new OperationTeamPK();
        pk.setId(id);
        OperationTeam operationTeam= operationTeamRepository.findById(pk).get();

        OperationTeamDto dto = new OperationTeamDto();
        dto.setId(operationTeam.getId());
        dto.setName(operationTeam.getName());
        dto.setCompany(operationTeam.getCompany());
        dto.setJobTitle(operationTeam.getJobTitle());
        dto.setMobile(operationTeam.getMobile());
        dto.setTelephone(operationTeam.getTelephone());
        dto.setFax(operationTeam.getFax());
        dto.setEmail(operationTeam.getEmail());
        dto.setRemark(operationTeam.getRemark());
        return dto;
    }


    public OperationTeam save(String id,
                              String name,
                              String company,
                              String jobTitle,
                              String mobile,
                              String telephone,
                              String fax,
                              String email,
                              String remark) {
        if (id == null) {
            //inventoryRepository.
            OperationTeam operationTeam = new OperationTeam();
            operationTeam.setId(UUID.randomUUID().toString());
            operationTeam.setName(name);
            operationTeam.setCompany(company);
            operationTeam.setJobTitle(jobTitle);
            operationTeam.setMobile(mobile);
            operationTeam.setTelephone(telephone);
            operationTeam.setFax(fax);
            operationTeam.setEmail(email);
            operationTeam.setRemark(remark);
            operationTeam.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            operationTeam.setCreatedBy("TEST");
            operationTeam.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            operationTeam.setUpdatedBy("TEST");
            return operationTeamRepository.save(operationTeam);
        } else {
            OperationTeamPK pk = new OperationTeamPK();
            pk.setId(id);
            OperationTeam operationTeam= operationTeamRepository.findById(pk).get();
            operationTeam.setName(name);
            operationTeam.setCompany(company);
            operationTeam.setJobTitle(jobTitle);
            operationTeam.setMobile(mobile);
            operationTeam.setTelephone(telephone);
            operationTeam.setFax(fax);
            operationTeam.setEmail(email);
            operationTeam.setRemark(remark);
            operationTeam.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            operationTeam.setUpdatedBy("TEST");
            return operationTeamRepository.save(operationTeam);
        }
    }

    public void delete(String id) {
        OperationTeamPK pk = new OperationTeamPK();
        pk.setId(id);
        operationTeamRepository.deleteById(pk);
    }

}
