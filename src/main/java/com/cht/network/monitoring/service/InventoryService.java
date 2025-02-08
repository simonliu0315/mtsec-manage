package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryInterface;
import com.cht.network.monitoring.domain.InventoryPK;
import com.cht.network.monitoring.dto.InventoryDto;
import com.cht.network.monitoring.repository.InventoryInterfaceRepository;
import com.cht.network.monitoring.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    private final InventoryInterfaceRepository inventoryInterfaceRepository;

    public InventoryService(InventoryRepository inventoryRepository, InventoryInterfaceRepository inventoryInterfaceRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryInterfaceRepository = inventoryInterfaceRepository;
    }

    public Page<InventoryDto> findAll(String filter, Pageable pageable) {

        log.info("findAll");
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Inventory> page = inventoryRepository.findAll(firstPageWithTwoElements);
        List<InventoryDto> dtos = new ArrayList<>();
        for (Inventory inventory : page) {
            InventoryDto inventoryDto = new InventoryDto();
            inventoryDto.setId(inventory.getId());
            inventoryDto.setDeviceName(inventory.getDeviceName());
            inventoryDto.setManageIp(inventory.getManageIp());
            inventoryDto.setDeviceType(inventory.getDeviceType());
            inventoryDto.setCreatedAt(inventory.getUpdatedAt());
            inventoryDto.setUpdatedAt(inventory.getUpdatedAt());
            dtos.add(inventoryDto);

            log.info("{}", inventory);
        }
        return new PageImpl<InventoryDto>(dtos, pageable, page.getTotalElements());
    }

    public InventoryDto findOne(String id, String filter) {
        InventoryPK pk = new InventoryPK();
        pk.setId(id);
        Inventory inventory = inventoryRepository.findById(pk).get();

        InventoryDto dto = new InventoryDto();
        dto.setId(inventory.getId());
        dto.setDeviceName(inventory.getDeviceName());
        dto.setDeviceInterface(inventory.getDeviceInterface());
        dto.setInterfaceDescription(inventory.getInterfaceDescription());
        dto.setManageIp(inventory.getManageIp());
        dto.setDeviceType(inventory.getDeviceType());
        dto.setVendor(inventory.getVendor());
        dto.setModel(inventory.getModel());
        dto.setLocation(inventory.getLocation());
        dto.setSnmpFrequency(inventory.getSnmpFrequency());
        dto.setPingFrequency(inventory.getPingFrequency());
        dto.setGroupId(inventory.getGroupId());
        dto.setExporterType(inventory.getExporterType());
        return dto;
    }

    @Transactional
    public Inventory save(String id,
                          String deviceName,
                          String deviceInterface,
                          String interfaceDescription, String manageIp, String deviceType,
                          String vendor, String model,
                          String location, String snmpFrequency,
                          String pingFrequency, String groupId,
                          String exporterType,
                          String snmpProtocol,
                          String snmpPort,
                          String snmpVersion,
                          String snmpCommunity,
                          List<InventoryInterface> inventoryInterfaces) {
        if (id == null) {
            //inventoryRepository.
            Inventory inventory = new Inventory();
            inventory.setId(UUID.randomUUID().toString());
            inventory.setDeviceName(deviceName);
            inventory.setDeviceInterface(deviceInterface);
            inventory.setInterfaceDescription(interfaceDescription);
            inventory.setManageIp(manageIp);
            inventory.setDeviceType(deviceType);
            inventory.setVendor(vendor);
            inventory.setModel(model);
            inventory.setLocation(location);
            inventory.setSnmpFrequency(snmpFrequency);
            inventory.setPingFrequency(pingFrequency);
            inventory.setGroupId(groupId);
            inventory.setExporterType(inventory.getExporterType());
            inventory.setExporterType(exporterType);
            inventory.setSnmpProtocol(snmpProtocol);
            inventory.setSnmpPort(snmpPort);
            inventory.setSnmpVersion(snmpVersion);
            inventory.setSnmpCommunity(snmpCommunity);
            inventory.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            inventory.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            inventory = inventoryRepository.save(inventory);

            inventoryInterfaceRepository.deleteByInventoryId(id);
            Inventory finalInventory = inventory;
            inventoryInterfaces.stream().forEach(inventoryInterface -> {
                inventoryInterface.setInventoryId(finalInventory.getId());
                inventoryInterface.setIfIndex(inventoryInterface.getIfIndex());
                inventoryInterface.setIfDescr(inventoryInterface.getIfDescr());
                inventoryInterface.setIfName(inventoryInterface.getIfName());
                inventoryInterface.setIfSpeed(inventoryInterface.getIfSpeed());
                inventoryInterface.setIfSpeedNm(inventoryInterface.getIfSpeedNm());
                inventoryInterface.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                inventoryInterface.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                inventoryInterfaceRepository.save(inventoryInterface);
            });

            return inventory;

        } else {

            InventoryPK pk = new InventoryPK();
            pk.setId(id);
            Inventory inventory = inventoryRepository.findById(pk).get();

            inventory.setId(id);
            inventory.setDeviceName(deviceName);
            inventory.setDeviceInterface(deviceInterface);
            inventory.setInterfaceDescription(interfaceDescription);
            inventory.setManageIp(manageIp);
            inventory.setDeviceType(deviceType);
            inventory.setVendor(vendor);
            inventory.setModel(model);
            inventory.setLocation(location);
            inventory.setSnmpFrequency(snmpFrequency);
            inventory.setPingFrequency(pingFrequency);
            inventory.setGroupId(groupId);
            inventory.setExporterType(exporterType);
            inventory.setSnmpProtocol(snmpProtocol);
            inventory.setSnmpPort(snmpPort);
            inventory.setSnmpVersion(snmpVersion);
            inventory.setSnmpCommunity(snmpCommunity);
            inventory.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            inventory = inventoryRepository.save(inventory);

            inventoryInterfaceRepository.deleteByInventoryId(id);
            Inventory finalInventory = inventory;
            inventoryInterfaces.stream().forEach(inventoryInterface -> {
                inventoryInterface.setInventoryId(finalInventory.getId());
                inventoryInterface.setIfIndex(inventoryInterface.getIfIndex());
                inventoryInterface.setIfDescr(inventoryInterface.getIfDescr());
                inventoryInterface.setIfName(inventoryInterface.getIfName());
                inventoryInterface.setIfSpeed(inventoryInterface.getIfSpeed());
                inventoryInterface.setIfSpeedNm(inventoryInterface.getIfSpeedNm());
                inventoryInterface.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                inventoryInterface.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                inventoryInterfaceRepository.save(inventoryInterface);
            });
            return inventory;
        }

    }

    public void delete(String id) {
        InventoryPK pk = new InventoryPK();
        pk.setId(id);

        inventoryRepository.deleteById(pk);
        inventoryInterfaceRepository.deleteByInventoryId(id);
    }

    public List<String> findIfIndexById(String inventoryId) {
        return inventoryInterfaceRepository.findIfIndexByInventoryId(inventoryId);
    }

    public List<Inventory> getInventories(String deviceType, String location) {
        return inventoryRepository.getInventoriesByDeviceTypeAndLocation(deviceType, location);
    }

    public List<InventoryInterface> findInventoryInterfaceByDeviceName(String deviceName) {
        List<Inventory> inventoryList = inventoryRepository.getInventoriesByDeviceName(deviceName);
        if (inventoryList.isEmpty()) {
            return new ArrayList<>();
        } else {
            return inventoryInterfaceRepository.findByInventoryId(inventoryList.get(0).getId());
        }

    }

    public List<Inventory> findInventoryeByDeviceName(String deviceName) {
        return inventoryRepository.getInventoriesByDeviceName(deviceName);
    }
}
