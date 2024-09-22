package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.EventLog;
import com.cht.network.monitoring.domain.EventLogPK;
import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.domain.InventoryPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventLogRepository extends JpaRepository<EventLog, EventLogPK> {


    public List<EventLog> findByDeviceIdAndAccessTypeAndTransactionTypeAndClosedAtIsNullOrderByCreatedAtDesc(String deviceId, String accessType, String transactionType);

    public Page<EventLog> findByAccessTypeAndClosedAtIsNullOrderByCreatedAtDesc(String accessType, Pageable pageable);

    public int countByLevelAndAccessTypeAndClosedAtIsNull(String level, String accessType);
}
