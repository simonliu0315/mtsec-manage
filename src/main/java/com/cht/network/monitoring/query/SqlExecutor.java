package com.cht.network.monitoring.query;

import com.cht.network.monitoring.repository.InventoryRepositoryImpl;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class SqlExecutor {
    private static final Logger log = LoggerFactory.getLogger(SqlExecutor.class);


    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SqlExecutor(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int delete(CharSequence sql) {
        return this.delete(new Query(sql));
    }

    public int[] delete(CharSequence sql, List<Map<String, ?>> listOfParameters) {
        return this.executeInBatch(sql, listOfParameters);
    }

    public int delete(CharSequence sql, Map<String, ?> parameters) {
        return this.delete(new Query(sql, parameters));
    }

    public int delete(Query query) {
        return this.execute(query);
    }

    /** @deprecated */
    @Deprecated
    public int deleteBySql(CharSequence sql, Map<String, ?> parameters) {
        return this.delete(sql, parameters);
    }

    private int execute(Query query) {
        Objects.requireNonNull(query, "Parameter \"query\" must not be null.");
        String queryString = query.getString();
        Map<String, Object> parameters = query.getParameters();
        this.logSqlExecute(queryString, parameters);
        return this.jdbcTemplate.update(queryString, parameters);
    }

    private int[] executeInBatch(CharSequence sql, List<Map<String, ?>> parameterss) {
        Preconditions.checkArgument(this.isNotNullAndNotEmpty(sql), "Query string must not be null.");
        Objects.requireNonNull(parameterss, "Conditions must not be null. SQL without parameter is inadeqte to use batch method.");
        this.logExecuteInBatch(sql, parameterss);
        return this.jdbcTemplate.batchUpdate(sql.toString(), (Map[])parameterss.toArray(new Map[parameterss.size()]));
    }

    public int insert(CharSequence sql) {
        return this.insert(new Query(sql));
    }

    public int[] insert(CharSequence sql, List<Map<String, ?>> listOfParameters) {
        return this.executeInBatch(sql, listOfParameters);
    }

    public int insert(CharSequence sql, Map<String, ?> parameters) {
        return this.insert(new Query(sql, parameters));
    }

    public int insert(Query query) {
        return this.execute(query);
    }

    private boolean isNotNullAndNotEmpty(CharSequence sql) {
        return sql != null && !Strings.isNullOrEmpty(sql.toString());
    }

    /** @deprecated */
    @Deprecated
    public int insertBySql(CharSequence sql, Map<String, ?> parameters) {
        return this.insert(sql, parameters);
    }

    private void logSqlExecute(CharSequence sql, Map<String, ?> parameters) {
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL:{}", sql);
            if (parameters != null && !parameters.isEmpty()) {
                Iterator var3 = parameters.entrySet().iterator();

                while(var3.hasNext()) {
                    Map.Entry<String, ?> entry = (Map.Entry)var3.next();
                    log.debug("Param:{}, Value:{}", entry.getKey(), entry.getValue());
                }
            } else {
                log.debug("With NO parameters!");
            }
        }

    }

    private void logExecuteInBatch(CharSequence sql, List<Map<String, ?>> parameterss) {
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL:{}", sql);

            for(int i = 0; i < parameterss.size(); ++i) {
                Map<String, ?> parameters = (Map)parameterss.get(0);
                log.debug("List[{}]", i);
                if (parameters != null && !parameters.isEmpty()) {
                    Iterator var5 = parameters.entrySet().iterator();

                    while(var5.hasNext()) {
                        Map.Entry<String, ?> entry = (Map.Entry)var5.next();
                        log.debug("Param:{}, Value:{}", entry.getKey(), entry.getValue());
                    }
                } else {
                    log.debug("With NO parameters!");
                }
            }
        }

    }

    public <T> List<T> queryForList(CharSequence sql, Class<T> clazz) {
        return this.queryForList(new Query(sql), clazz);
    }

    public <T> List<T> queryForList(CharSequence sql, Map<String, ?> parameters, Class<T> clazz) {
        return this.queryForList(new Query(sql, parameters), clazz);
    }

    public <T> List<T> queryForList(Query query, Class<T> clazz) {
        String queryString = query.getString();
        Map<String, Object> parameters = query.getParameters();
        this.logSqlExecute(queryString, parameters);
        return this.isSimpleType(clazz) ? this.jdbcTemplate.queryForList(queryString, parameters, clazz) : this.jdbcTemplate.query(queryString, parameters, new BeanPropertyRowMapper(clazz));
    }

    private <T> boolean isSimpleType(Class<T> clazz) {
        return ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.equals(String.class) || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class);
    }

    public int update(CharSequence sql) {
        return this.update(new Query(sql));
    }

    public int[] update(CharSequence sql, List<Map<String, ?>> listOfParameters) {
        return this.executeInBatch(sql, listOfParameters);
    }

    public int update(CharSequence sql, Map<String, ?> parameters) {
        return this.update(new Query(sql, parameters));
    }

    public int update(Query query) {
        return this.execute(query);
    }

    /** @deprecated */
    @Deprecated
    public int updateBySql(CharSequence sql, Map<String, ?> parameters) {
        return this.update(sql.toString(), (Map)parameters);
    }
}
