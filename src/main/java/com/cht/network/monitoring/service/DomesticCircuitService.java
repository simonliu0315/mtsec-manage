package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DomesticCircuitService {

    private static final Logger log = LoggerFactory.getLogger(DomesticCircuitService.class);

    private final InventoryRepository inventoryRepository;

    public DomesticCircuitService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Page<DomesticCircuitDto> findAll(String filter, Pageable pageable) {

        log.info("findAll");
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Inventory> page = inventoryRepository.findAll(firstPageWithTwoElements);
        List<DomesticCircuitDto> dtos = new ArrayList<>();
        for(Inventory inventory : page) {
            DomesticCircuitDto domesticCircuitDto = new DomesticCircuitDto();
            domesticCircuitDto.setWarnLevel("danger");
            domesticCircuitDto.setDeviceId(String.valueOf(inventory.getId()));
            domesticCircuitDto.setDeviceName(inventory.getDeviceName());
            domesticCircuitDto.setDeviceInterface(inventory.getDeviceInterface());
            domesticCircuitDto.setInterfaceDescription(inventory.getInterfaceDescription());
            dtos.add(domesticCircuitDto);

            log.info("{}", inventory);
        }
        return new PageImpl<DomesticCircuitDto>(dtos, pageable, page.getTotalElements());
    }
}
