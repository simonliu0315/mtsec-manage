package com.cht.network.monitoring.repository;

import com.cht.network.monitoring.dto.EventLogCnt;
import com.cht.network.monitoring.dto.EventLogInventoryDto;
import com.cht.network.monitoring.dto.InventoryEventLogDto;
import com.cht.network.monitoring.query.Query;
import com.cht.network.monitoring.query.SqlExecutor;
import com.cht.network.monitoring.query.page.SqlPaginationHelper;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryRepositoryImpl implements InventoryRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(InventoryRepositoryImpl.class);


    private final SqlPaginationHelper sqlPaginationHelper;


    private final SqlExecutor sqlExecutor;


    public InventoryRepositoryImpl(SqlPaginationHelper sqlPaginationHelper, SqlExecutor sqlExecutor) {
        this.sqlPaginationHelper = sqlPaginationHelper;
        this.sqlExecutor = sqlExecutor;
    }


    public Page<InventoryEventLogDto> getInventoryAndEventLog(String filter, String type,
                                                              Pageable pageable) {
        Query.Builder builder = Query.builder();
        builder.expandIterableParameters(true)
                .append("SELECT A.*, ")
                .append("IFNULL((select level from network.event_log")
                .append("where device_id = A.id and closed_at is null and type= :type order by occurred_at desc limit 1),'Normal') as level", type)
                .append("FROM inventory A")
                .append("where 1=1")
                .appendWhen(StringUtils.isNotBlank(filter), "AND (A.device_name like :filter", "%" + filter + "%")
                .appendWhen(StringUtils.isNotBlank(filter), "OR A.device_interface like :filter", "%" + filter + "%")
                .appendWhen(StringUtils.isNotBlank(filter), "OR A.interface_description like :filter)", "%" + filter + "%")
                .append("order by A.id desc");
        Query query = builder.build();
        return sqlPaginationHelper.queryForPage(query, InventoryEventLogDto.class, pageable);

    }

    public List<EventLogCnt> findEventLogCnt(String filter, String type) {
        Query.Builder builder = Query.builder();
        builder.expandIterableParameters(false)
                .append("select IFNULL(level, 'Normal') as level, count(*) as cnt from (")
                .append("SELECT A.*, (select level from ")
                .append("network.event_log where device_id = A.id and closed_at is null and type = :type", type)
                .append("order by occurred_at desc limit 1) as level ")
                .append("FROM network.inventory A ) as U group by level");
                //.appendWhen(StringUtils.isNotBlank(filter), "AND (A.device_name like :filter", "%" + filter + "%")
                //.appendWhen(StringUtils.isNotBlank(filter), "OR A.device_interface like :filter", "%" + filter + "%")
                //.appendWhen(StringUtils.isNotBlank(filter), "OR A.interface_description like :filter)", "%" + filter + "%")
                //.append("order by A.id desc");
        Query query = builder.build();

        log.info("{} {}", query.toString(), query.getParameters());
        return sqlExecutor.queryForList(query.getString(), query.getParameters(), EventLogCnt.class);
    }
}
