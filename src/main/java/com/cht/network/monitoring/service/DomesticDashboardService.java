package com.cht.network.monitoring.service;

import com.cht.network.monitoring.domain.EventLog;
import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryPK;
import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.dto.EventLogDto;
import com.cht.network.monitoring.dto.InventoryEventLogDto;
import com.cht.network.monitoring.repository.EventLogRepository;
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
public class DomesticDashboardService {

    private static final Logger log = LoggerFactory.getLogger(DomesticDashboardService.class);

    private final EventLogRepository eventLogRepository;

    private final InventoryRepository inventoryRepository;

    public DomesticDashboardService(EventLogRepository eventLogRepository, InventoryRepository inventoryRepository) {
        this.eventLogRepository = eventLogRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Page<EventLog> getInnerEventLogsNotClosed(Pageable pageable) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<EventLog> eventLogs =  eventLogRepository.findByAccessTypeAndClosedAtIsNullOrderByCreatedAtDesc("I", firstPageWithTwoElements);

        for(EventLog eventLog : eventLogs) {
            InventoryPK id = new InventoryPK();
            id.setId(eventLog.getDeviceId());
            inventoryRepository.findById(id).ifPresent(inventory -> {
                eventLog.setDeviceId(inventory.getDeviceName());
            });

            log.info("{}", eventLog);
        }
        return new PageImpl<EventLog>(eventLogs.getContent(), pageable, eventLogs.getTotalElements());
    }

    public Page<EventLog> getOutterEventLogsNotClosed(Pageable pageable) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<EventLog> eventLogs =  eventLogRepository.findByAccessTypeAndClosedAtIsNullOrderByCreatedAtDesc("O", firstPageWithTwoElements);

        for(EventLog eventLog : eventLogs) {
            InventoryPK id = new InventoryPK();
            id.setId(eventLog.getDeviceId());
            inventoryRepository.findById(id).ifPresent(inventory -> {
                eventLog.setDeviceId(inventory.getDeviceName());
            });

            log.info("{}", eventLog);
        }
        return new PageImpl<EventLog>(eventLogs.getContent(), pageable, eventLogs.getTotalElements());
    }

    public int getInnerEventLogsCount(String level) {
        return eventLogRepository.countByLevelAndAccessTypeAndClosedAtIsNull(level, "I");
    }

    public int getOutterEventLogsCount(String level) {
        return eventLogRepository.countByLevelAndAccessTypeAndClosedAtIsNull(level, "O");
    }
}
