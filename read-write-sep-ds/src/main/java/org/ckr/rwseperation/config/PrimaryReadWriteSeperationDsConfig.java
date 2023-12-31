package org.ckr.rwseperation.config;


import org.ckr.rwseperation.datasource.ReadWriteSeperationDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@Import(PrimaryReadWriteTxnInterceptorConfig.class)
public class PrimaryReadWriteSeperationDsConfig implements EnvironmentAware {

    private Environment env;


    @Bean()
    @ConfigurationProperties("spring.datasource.primary-read-write-seperation-ds.read-write")
    @ConditionalOnProperty(name="spring.datasource.primary-read-write-seperation-ds.read-write.url")
    public DataSourceProperties readWriteDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean()
    @ConfigurationProperties(prefix = "spring.datasource.primary-read-write-seperation-ds.read-write.pool")
    @ConditionalOnProperty(name="spring.datasource.primary-read-write-seperation-ds.read-write.url")
    public DataSource readWriteDataSource() {
        return readWriteDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean()
    @ConfigurationProperties("spring.datasource.primary-read-write-seperation-ds.read")
    @ConditionalOnProperty(name="spring.datasource.primary-read-write-seperation-ds.read.url")
    public DataSourceProperties readOnlyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean()
    @ConfigurationProperties(prefix = "spring.datasource.primary-read-write-seperation-ds.read.pool")
    @ConditionalOnProperty(name="spring.datasource.primary-read-write-seperation-ds.read.url")
    public DataSource readOnlyDataSource() {
        return readOnlyDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean("primaryReadWriteSeperationDs")
    @ConditionalOnProperty(name="spring.datasource.primary-read-write-seperation-ds.read-write.url")
    public DataSource readWriteSeperationDataSource() {
        DataSource readOnlyDs;

        if (env.getProperty("spring.datasource.primary-read-write-seperation-ds.read.url") != null) {
            readOnlyDs = readOnlyDataSource();
        } else {
            //if read only data source is not defined, use read wirte data source as read only data source
            readOnlyDs = readWriteDataSource();
        }

        return new ReadWriteSeperationDataSource(readWriteDataSource(), readOnlyDs);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
