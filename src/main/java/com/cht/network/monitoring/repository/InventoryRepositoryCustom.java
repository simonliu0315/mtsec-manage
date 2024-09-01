package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.dto.EventLogCnt;
import com.cht.network.monitoring.dto.EventLogInventoryDto;
import com.cht.network.monitoring.dto.InventoryEventLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryRepositoryCustom {

    public Page<InventoryEventLogDto> getInventoryAndEventLog(String filter, String type,
                                                              Pageable pageable);

    public List<EventLogCnt> findEventLogCnt(String filter, String type);
}
