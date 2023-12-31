package org.ckr.rwseperation.testapp.config;

import liquibase.integration.spring.SpringLiquibase;
import org.ckr.rwseperation.config.PrimaryReadWriteSeperationDsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiquibaseConfig {

    @Autowired
    private PrimaryReadWriteSeperationDsConfig primaryDsConfig;

    @Bean
    @ConditionalOnProperty(name="spring.datasource.primary-read-write-seperation-ds.read-write.url")
    public SpringLiquibase readWriteLiquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(primaryDsConfig.readWriteDataSource());
        liquibase.setChangeLog("classpath:db/changelog/readwrite-master.xml");
        return liquibase;
    }

    @Bean
    @ConditionalOnProperty(name="spring.datasource.primary-read-write-seperation-ds.read.url")
    public SpringLiquibase readOnlyLiquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(primaryDsConfig.readOnlyDataSource());
        liquibase.setChangeLog("classpath:db/changelog/readonly-master.xml");
        return liquibase;
    }
}
