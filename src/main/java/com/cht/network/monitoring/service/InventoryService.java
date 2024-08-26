package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryPK;
import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.dto.InventoryDto;
import com.cht.network.monitoring.repository.InventoryRepository;
import com.cht.network.monitoring.rest.vm.InventoryVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public InventoryDto findOne(String id, String filter) {
        InventoryPK pk = new InventoryPK();
        pk.setId(id);
        Inventory inventory= inventoryRepository.findById(pk).get();

        InventoryDto dto = new InventoryDto();
        dto.setId(inventory.getId());
        dto.setDeviceName(inventory.getDeviceName());
        dto.setDeviceInterface(inventory.getDeviceInterface());
        dto.setInterfaceDescription(inventory.getInterfaceDescription());
        return dto;
    }

    public Inventory save(String id,
                          String deviceName,
                          String deviceInterface,
                          String interfaceDescription) {
        if (id == null) {
            //inventoryRepository.
            Inventory inventory = new Inventory();
            inventory.setId(UUID.randomUUID().toString());
            inventory.setDeviceName(deviceName);
            inventory.setDeviceInterface(deviceInterface);
            inventory.setInterfaceDescription(interfaceDescription);
            return inventoryRepository.save(inventory);

        } else {
            Inventory inventory = new Inventory();
            inventory.setId(id);
            inventory.setDeviceName(deviceName);
            inventory.setDeviceInterface(deviceInterface);
            inventory.setInterfaceDescription(interfaceDescription);
            return inventoryRepository.save(inventory);
        }

    }

    public void delete(String id) {
        InventoryPK pk = new InventoryPK();
        pk.setId(id);

        inventoryRepository.deleteById(pk);
    }
}
