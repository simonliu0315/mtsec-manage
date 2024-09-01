package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.EventLog;
import com.cht.network.monitoring.domain.EventStatistics;
import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.dto.EventLogCnt;
import com.cht.network.monitoring.dto.EventLogInventoryDto;
import com.cht.network.monitoring.dto.InventoryEventLogDto;
import com.cht.network.monitoring.repository.EventLogRepository;
import com.cht.network.monitoring.repository.EventStatisticsRepository;
import com.cht.network.monitoring.repository.InventoryRepository;
import org.apache.commons.lang3.StringUtils;
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

    private final EventLogRepository eventLogRepository;

    private final EventStatisticsRepository eventStatisticsRepository;

    public DomesticCircuitService(InventoryRepository inventoryRepository, EventLogRepository eventLogRepository,
                                  EventStatisticsRepository eventStatisticsRepository) {
        this.inventoryRepository = inventoryRepository;
        this.eventLogRepository= eventLogRepository;
        this.eventStatisticsRepository = eventStatisticsRepository;
    }

    public Page<DomesticCircuitDto> findAll(String filter, Pageable pageable) {

        log.info("findAll filter {}", filter);
        filter = StringUtils.stripToEmpty(filter);
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        //Page<Inventory> page = inventoryRepository.findInventoryByDeviceNameIsContainingOrDeviceInterfaceIsContainingOrInterfaceDescriptionIsContaining(filter,
        //        filter, filter, firstPageWithTwoElements);
        Page<InventoryEventLogDto> page = inventoryRepository.getInventoryAndEventLog(filter, "Traffic", firstPageWithTwoElements);
                List<DomesticCircuitDto> dtos = new ArrayList<>();
        for(InventoryEventLogDto inventory : page) {
            DomesticCircuitDto domesticCircuitDto = new DomesticCircuitDto();
            domesticCircuitDto.setWarnLevel(inventory.getLevel());
            domesticCircuitDto.setDeviceId(String.valueOf(inventory.getId()));
            domesticCircuitDto.setDeviceName(inventory.getDeviceName());
            domesticCircuitDto.setDeviceInterface(inventory.getDeviceInterface());
            domesticCircuitDto.setInterfaceDescription(inventory.getInterfaceDescription());
            dtos.add(domesticCircuitDto);

            log.info("{}", inventory);
        }
        return new PageImpl<DomesticCircuitDto>(dtos, pageable, page.getTotalElements());
    }

    public List<EventLogCnt> getEventLogCnt(String filter) {
        return inventoryRepository.findEventLogCnt(filter, "Traffic");
    }

    public List<EventLogCnt> getEventLogHistoryCnt(String filter) {
        return inventoryRepository.findEventLogCnt(filter, "Traffic");
    }

    public List<EventStatistics> getEventLogHistoryCnt(String filter,
                                                int fromInterval, int toInterval, int limit) {
        return eventStatisticsRepository.findEventLogCnt(filter, "Traffic",
                "MINUTE", fromInterval, toInterval, limit);
    }


}
