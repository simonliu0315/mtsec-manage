package com.cht.network.monitoring.query.page;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Component;


@Component
public class DatabaseHelper {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHelper.class);

    @Autowired
//    @Qualifier("jdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    private Database databaseType;

    public DatabaseHelper() {
    }

    public Database getDatabaseType() {
        if (databaseType != null) {
            return databaseType;
        } else {
            if (jdbcTemplate != null) {
                Connection conn = null;
                try {
                    conn = jdbcTemplate.getJdbcTemplate().getDataSource().getConnection();
                    String databaseName = conn.getMetaData().getDatabaseProductName().toUpperCase();
                    log.debug("Database product name: " + databaseName);
                    if (databaseName.indexOf("ORACLE") >= 0) {
                        databaseType = Database.ORACLE;
                    } else if (databaseName.indexOf("SQL SERVER") >= 0) {
                        databaseType = Database.SQL_SERVER;
                    } else if (databaseName.indexOf("MYSQL") >= 0) {
                        databaseType = Database.MYSQL;
                    } else if (databaseName.indexOf("POSTGRE") >= 0) {
                        databaseType = Database.POSTGRESQL;
                    } else if (databaseName.indexOf("HSQL") >= 0) {
                        databaseType = Database.HSQL;
                    } else {
                        databaseType = Database.DEFAULT;
                    }
                } catch (SQLException e) {
                    log.error(e.getMessage());
                    databaseType = Database.DEFAULT;
                } finally {
                    if (conn != null) {
                        safeClose(conn);
                    }
                }
            } else {
                databaseType = Database.HSQL;
            }
            return databaseType;
        }
    }

    public static void safeClose(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }
}
