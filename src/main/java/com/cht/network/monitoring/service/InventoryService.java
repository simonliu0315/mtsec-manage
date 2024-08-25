package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.dto.InventoryDto;
import com.cht.network.monitoring.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Page<InventoryDto> findAll(String filter, Pageable pageable) {

        log.info("findAll");
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Inventory> page = inventoryRepository.findAll(firstPageWithTwoElements);
        List<InventoryDto> dtos = new ArrayList<>();
        for(Inventory inventory : page) {
            InventoryDto inventoryDto = new InventoryDto();
            inventoryDto.setId(inventory.getId());
            inventoryDto.setDeviceName(inventory.getDeviceName());
            inventoryDto.setDeviceInterface(inventory.getDeviceInterface());
            inventoryDto.setInterfaceDescription(inventory.getInterfaceDescription());
            dtos.add(inventoryDto);

            log.info("{}", inventory);
        }
        return new PageImpl<InventoryDto>(dtos, pageable, page.getTotalElements());
    }
}
