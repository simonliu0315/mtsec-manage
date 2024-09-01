package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.EventStatistics;
import com.cht.network.monitoring.domain.EventStatisticsPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventStatisticsRepository extends JpaRepository<EventStatistics, EventStatisticsPK>, EventStatisticsRepositoryCustom {
}
