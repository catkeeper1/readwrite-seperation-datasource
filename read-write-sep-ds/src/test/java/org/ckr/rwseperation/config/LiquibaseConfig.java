package org.ckr.rwseperation.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiquibaseConfig {

    @Autowired
    private PrimaryReadWriteSeperationDsConfig primaryDsConfig;

    @Bean
    public SpringLiquibase readWriteLiquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(primaryDsConfig.readWriteDataSource());
        liquibase.setChangeLog("classpath:db/changelog/readwrite-master.xml");
        return liquibase;
    }

    @Bean
    public SpringLiquibase readOnlyLiquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(primaryDsConfig.readOnlyDataSource());
        liquibase.setChangeLog("classpath:db/changelog/readonly-master.xml");
        return liquibase;
    }
}
