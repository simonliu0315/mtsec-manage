package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.domain.EventStatistics;
import com.cht.network.monitoring.dto.EventLogCnt;
import com.cht.network.monitoring.dto.InventoryEventLogDto;
import com.cht.network.monitoring.query.Query;
import com.cht.network.monitoring.query.SqlExecutor;
import com.cht.network.monitoring.query.page.SqlPaginationHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class EventStatisticsRepositoryImpl implements EventStatisticsRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(EventStatisticsRepositoryImpl.class);


    private final SqlPaginationHelper sqlPaginationHelper;


    private final SqlExecutor sqlExecutor;


    public EventStatisticsRepositoryImpl(SqlPaginationHelper sqlPaginationHelper, SqlExecutor sqlExecutor) {
        this.sqlPaginationHelper = sqlPaginationHelper;
        this.sqlExecutor = sqlExecutor;
    }

    public List<EventStatistics> findEventLogCnt(String filter, String type, String dayType,
                                                 int fromInterval, int toInterval, int limit) {
        Query.Builder builder = Query.builder();
        builder.expandIterableParameters(false)
                .append("select * from ")
                .append("event_statistics")
                .append("where type = :type ", type)

                .appendWhen(StringUtils.equals(dayType, "MINUTE"),"and check_time BETWEEN NOW() - INTERVAL :fromInterval MINUTE", fromInterval)
                .appendWhen(StringUtils.equals(dayType, "MINUTE")," AND NOW()  - INTERVAL :toInterval MINUTE", toInterval)
                .appendWhen(StringUtils.equals(dayType, "HOUR"),"and check_time BETWEEN NOW() - INTERVAL :fromInterval HOUR", fromInterval)
                .appendWhen(StringUtils.equals(dayType, "HOUR")," AND NOW()  - INTERVAL :toInterval HOUR", toInterval)
                .appendWhen(StringUtils.equals(dayType, "DAY"),"and check_time BETWEEN NOW() - INTERVAL :fromInterval DAY", fromInterval)
                .appendWhen(StringUtils.equals(dayType, "DAY")," AND NOW()  - INTERVAL :toInterval DAY", toInterval)

                .append("order by check_time desc")
                .append("limit :limit", limit);
                //.appendWhen(StringUtils.isNotBlank(filter), "AND (A.device_name like :filter", "%" + filter + "%")
                //.appendWhen(StringUtils.isNotBlank(filter), "OR A.device_interface like :filter", "%" + filter + "%")
                //.appendWhen(StringUtils.isNotBlank(filter), "OR A.interface_description like :filter)", "%" + filter + "%")
                //.append("order by A.id desc");
        Query query = builder.build();

        log.info("{} {}", query.toString(), query.getParameters());
        return sqlExecutor.queryForList(query.getString(), query.getParameters(), EventStatistics.class);
    }
}
