package com.cht.network.monitoring.query.page;

import com.cht.network.monitoring.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class SqlQueryHelper {

    private static final Logger log = LoggerFactory.getLogger(SqlQueryHelper.class);

    @Autowired
    DatabaseHelper databaseHelper;

    public String currentDateSql() {
        switch (databaseHelper.getDatabaseType()) {
        case SQL_SERVER:
            return "CONVERT(CHAR(10), GETDATE(), 120)";
        case MYSQL:
            return "DATE_FORMAT(NOW(), '%Y-%m-%d')";
        case POSTGRESQL:
            return "CURRENT_DATE";
        case HSQL:
            return "CURRENT_DATE";
        case ORACLE:
            return "TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD')";
        default:
            log.debug("Unsupported database type!");
            return "";
        }
    }

    public String currentDateTimeSql() {
        switch (databaseHelper.getDatabaseType()) {
        case SQL_SERVER:
            return "CONVERT(CHAR(19), GETDATE(), 120)";
        case MYSQL:
            return "DATE_FORMAT(NOW(), '%Y-%m-%d %H:%m:%i')";
        case POSTGRESQL:
        case HSQL:
            return "TO_CHAR(NOW(), 'YYYY-MM-DD HH24:MI:SS')";
        case ORACLE:
            return "TO_CHAR(CURRENT_DATE, 'YYYY-MM-DD HH24:MI:SS')";
        default:
            log.debug("Unsupported database type!");
            return "";
        }
    }

    public String currentTimeSql() {
        switch (databaseHelper.getDatabaseType()) {
        case SQL_SERVER:
            return "CONVERT(CHAR(8), GETDATE(), 108)";
        case MYSQL:
            return "CURTIME()";
        case POSTGRESQL:
        case HSQL:
            return "TO_CHAR(NOW(), 'HH24:MI:SS')";
        case ORACLE:
            return "TO_CHAR(CURRENT_TIMESTAMP, 'HH24:MI:SS')";
        default:
            log.debug("Unsupported database type!");
            return "";
        }
    }

    public String limitQueryResult(String querySql, int items) {
        switch (databaseHelper.getDatabaseType()) {
        case SQL_SERVER:
        case HSQL:
            if (querySql.length() > 7) {
                //Remove "SELECT ", and add "SELECT TOP " back
                return ("SELECT TOP " + items +  " " + querySql.substring(7));
            } else {
                log.debug("querySql is less than 8 characters and will not be modified.");
                return querySql;
            }
        case MYSQL:
        case POSTGRESQL:
            return querySql + " LIMIT " + items;
        case ORACLE:
            return "SELECT * FROM ( " + querySql + " ) WHERE ROWNUM <= " + items;
        default:
            log.debug("Unsupported database type!");
            return querySql;
        }
    }

    public Query limitQueryResult(Query query, int items) {
        return Query.of(limitQueryResult(query.getString(), items), query.getParameters());
    }

    public Query limitQueryResult(Query.Builder builder, int items) {
        return limitQueryResult(builder.build(), items);
    }
}
