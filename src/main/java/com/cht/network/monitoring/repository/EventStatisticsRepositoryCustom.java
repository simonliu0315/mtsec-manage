package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.EventStatistics;
import com.cht.network.monitoring.domain.EventStatisticsPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventStatisticsRepositoryCustom {

    public List<EventStatistics> findEventLogCnt(String filter, String type, String dayType,
                                                 int fromInterval, int toInterval, int limit);

}
