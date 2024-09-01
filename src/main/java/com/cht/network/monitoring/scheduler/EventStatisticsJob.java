package com.cht.network.monitoring.scheduler;

import com.cht.network.monitoring.domain.EventStatistics;
import com.cht.network.monitoring.dto.EventLogCnt;
import com.cht.network.monitoring.repository.EventStatisticsRepository;
import com.cht.network.monitoring.repository.InventoryRepository;
import com.cht.network.monitoring.service.DomesticCircuitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@EnableAsync
@Component
public class EventStatisticsJob {

    private static final Logger log = LoggerFactory.getLogger(DomesticCircuitService.class);


    private final InventoryRepository inventoryRepository;

    private final EventStatisticsRepository eventStatisticsRepository;

    public EventStatisticsJob(InventoryRepository inventoryRepository, EventStatisticsRepository eventStatisticsRepository) {
        this.inventoryRepository = inventoryRepository;
        this.eventStatisticsRepository = eventStatisticsRepository;
    }
    //@Async
    //@Scheduled(cron = "? 0/5 * * * ?")
    @Scheduled(fixedRate=  60 * 1000)
    public void scheduleFixedDelayTask() {
        log.info("scheduleFixedDelayTask");
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zdt = now.atZone(ZoneId.systemDefault());
        log.info("now.getMinute() {}", now.getMinute());
        if (now.getMinute() % 5 == 0) {
            List<EventLogCnt> list =  inventoryRepository.findEventLogCnt("", "Traffic");
            //insert cnt
            EventStatistics eventStatistics = new EventStatistics();
            eventStatistics.setCheckTime(Date.from(zdt.toInstant()));
            eventStatistics.setType("Traffic");
            for(EventLogCnt cnt :list) {
                switch(cnt.getLevel()) {
                    case "Critical": eventStatistics.setCriticalCount(cnt.getCnt());
                        break;
                    case "Minor": eventStatistics.setMinorCount(cnt.getCnt());
                        break;
                    case "Normal": eventStatistics.setNormalCount(cnt.getCnt());
                        break;
                    default: eventStatistics.setNormalCount(cnt.getCnt());
                        break;
                }
            }
            eventStatistics.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            eventStatistics.setCreatedBy("System");
            eventStatistics.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            eventStatistics.setUpdatedBy("System");
            eventStatisticsRepository.saveAndFlush(eventStatistics);
            }
        }

    }

