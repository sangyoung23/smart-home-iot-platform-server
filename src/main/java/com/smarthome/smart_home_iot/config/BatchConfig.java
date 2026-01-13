package com.smarthome.smart_home_iot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig extends DefaultBatchConfiguration {

    private final DataSource dataSource;

    @Override
    public JobRepository jobRepository() {
        try {
            JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
            factory.setDataSource(dataSource);
            factory.setTransactionManager(getTransactionManager());
            factory.setDatabaseType("MYSQL");
            factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
            factory.afterPropertiesSet();

            return factory.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JobRepository", e);
        }
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return new JdbcTransactionManager(dataSource);
    }
}