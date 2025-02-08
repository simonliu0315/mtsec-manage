package com.cht.network.monitoring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = {"com.cht.network.monitoring.repository"})
@Configuration
public class DBConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DBConfiguration.class);
    {
        log.info("我被載入了：DBConfiguration");
    }

    @Autowired
    private Environment environment;

    @Primary
    @Bean(name = "Properties")
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        HikariDataSource dataSource =new HikariDataSource(hikariConfig());
        return dataSource;
    }


    @Primary
    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.cht.network.monitoring.domain");
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactory.setPersistenceUnitName("monitoring");

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        //properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
        properties.put("showSql", true);

        entityManagerFactory.setJpaPropertyMap(properties);
        return entityManagerFactory;

    }
    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager (
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Primary
    @Bean(name = "jdbcTemplate")
    public NamedParameterJdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    private boolean isDevMode() {
        log.info("length: {}", environment.getActiveProfiles().length);
        if (environment.getActiveProfiles().length == 0) {
            return true;
        }
        String value = environment.getActiveProfiles()[0];
        if (StringUtils.isBlank(value)) {
            value = "PRODUCTION";
        }
        value = value.toUpperCase();
        return !value.contains("PRODUCTION");
    }
}
