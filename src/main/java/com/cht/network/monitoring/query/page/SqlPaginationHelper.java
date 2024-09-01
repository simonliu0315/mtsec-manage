package com.cht.network.monitoring.query.page;

import java.util.*;

import com.cht.network.monitoring.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;


@Component
public class SqlPaginationHelper {

    private static final Logger log = LoggerFactory.getLogger(SqlPaginationHelper.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private DatabaseHelper databaseHelper;

    public SqlPaginationHelper() {
    }

    public <T> Page<T> queryForPage(CharSequence sql, Class<T> clazz, Pageable pageable) {
        return queryForPage(Query.builder().append(sql.toString()).build(), clazz, pageable);
    }

    public <T> Page<T> queryForPage(CharSequence sql, Map<String, ?> parameters,
        Class<T> clazz, Pageable pageable) {
        return queryForPage(Query.builder().append(sql.toString(), parameters).build(), clazz, pageable);
    }

    //Support Oracle 12c onward & MS SQL Server 2012 onward & MySQL & PostgreSQL
    public <T> Page<T> queryForPage(Query query, Class<T> clazz, Pageable pageable) {
        if (pageable == null) {
            return new PageImpl<T>(new ArrayList<T>(), pageable, 0);
        }
        String queryString = query.getString();
        String queryStringUpperCase = queryString.toUpperCase();
        int orderIdx = queryStringUpperCase.lastIndexOf(" ORDER BY ");
        String queryCountString = "SELECT COUNT(*) FROM (";
        if (databaseHelper.getDatabaseType().equals(Database.MYSQL)) {
            queryCountString += queryString + ") AS U";
        } else {
            queryCountString += queryString + ")";
        }

        Map<String, Object> parameters = query.getParameters();

        boolean isString = clazz.equals(String.class);
        boolean isPrimitive = ClassUtils.isPrimitiveOrWrapper(clazz);

        logSqlExecute(queryCountString, parameters);

        long rowCount = this.namedParameterJdbcTemplate.queryForObject(
            queryCountString, parameters, Long.class);
        long pageCount = rowCount / pageable.getPageSize();
        if (rowCount > pageable.getPageSize() * pageCount) {
            pageCount++;
        }

//        if (!pageable.getSort().toString().equals("UNSORTED")) {
//            String orderby = extractSortSql(pageable.getSort());
//            if (orderIdx >= 0) {
//                queryString = queryString.substring(0, orderIdx);
//            }
//            queryString += " " + orderby;
//        }

        switch (databaseHelper.getDatabaseType()) {
        case ORACLE:
        case SQL_SERVER:
            queryString += " OFFSET " + pageable.getOffset() +
            " ROWS FETCH NEXT " + pageable.getPageSize() + " ROWS ONLY";
            break;
        case MYSQL:
        case POSTGRESQL:
        case HSQL:
            queryString += " LIMIT " + pageable.getPageSize() +
            " OFFSET " + pageable.getOffset();
            break;
        default:
            log.debug("The pagination helper doesn't supported database: {}",
                    databaseHelper.getDatabaseType().name());
            break;
        }

        logSqlExecute(queryString, parameters);

         List<T> result = null;
        if ((isString) || (isPrimitive)) {
            result = this.namedParameterJdbcTemplate.queryForList(queryString, parameters, clazz);
        } else {
            result = this.namedParameterJdbcTemplate.query(queryString, parameters,
                new BeanPropertyRowMapper<T>(clazz));
        }
        return new PageImpl<T>(result, pageable, rowCount);
    }

    private String extractSortSql(Sort sort) {
        String ret = "";
        if (sort == null) {
            return ret;
        }
        ret = "ORDER BY";
        Iterator<Sort.Order> orders = sort.iterator();
        while (orders.hasNext()) {
            Sort.Order order = orders.next();
            ret += " " + order.getProperty();
            if (order.getDirection() != null) {
                Sort.Direction d = order.getDirection();
                if (d.compareTo(Sort.Direction.ASC) == 0) {
                    ret += " ASC";
                } else {
                    ret += " DESC";
                }
            }
            if (orders.hasNext()) {
                ret += ",";
            }
        }
        return ret;

    }

    private void logSqlExecute(CharSequence sql, Map<String, ?> parameters) {
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL:{}", sql);
            if ((parameters == null) || (parameters.isEmpty())) {
                log.debug("With NO parameters!");
            } else {
                for (Map.Entry<String, ?> entry : parameters.entrySet()) {
                    log.debug("Param:{}, Value:{}", entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
