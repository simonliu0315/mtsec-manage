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
        DomesticCircuitDto dto = new DomesticCircuitDto();
        List<DomesticCircuitDto> list = new ArrayList<>();
        dto.setWarnLevel("warning");
        dto.setDeviceName("TWAREN-TP-ASR9006-01");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-02");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-03");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-04");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-05");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-06");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-07");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-08");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-09");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-10");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        dto = new DomesticCircuitDto();
        dto.setWarnLevel("danger");
        dto.setDeviceName("TWAREN-TP-ASR9006-11");
        dto.setDeviceInterface("Te0/0/0/5.3");
        dto.setCheckTime(new Date());
        dto.setInterfaceDescription("INT#20_TWAREN-TP-ASR9006-01 to CHI-4801l 10GE (2671UD80004)");
        list.add(dto);
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Inventory> page = inventoryRepository.findAll(firstPageWithTwoElements);
        List<DomesticCircuitDto> dtos = new ArrayList<>();
        for(Inventory inventory : page) {
            DomesticCircuitDto domesticCircuitDto = new DomesticCircuitDto();
            domesticCircuitDto.setWarnLevel("danger");
            domesticCircuitDto.setDeviceId(String.valueOf(inventory.getId()));
            domesticCircuitDto.setDeviceName(inventory.getDeviceName());
            domesticCircuitDto.setDeviceInterface(inventory.getDeviceInterface());
            dtos.add(domesticCircuitDto);

            log.info("{}", inventory);
        }
        return new PageImpl<DomesticCircuitDto>(dtos, pageable, page.getTotalElements());
    }
}
