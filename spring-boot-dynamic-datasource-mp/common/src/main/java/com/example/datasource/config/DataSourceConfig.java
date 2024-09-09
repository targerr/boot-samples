package com.example.datasource.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class DataSourceConfig {

    @Bean(name = "master")
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "slave")
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource() {
        return new HikariDataSource();
    }


    @Bean
    @Primary
    public DataSource dataSource(@Autowired(required = false) @Qualifier("master") DataSource masterDataSource, @Autowired(required = false) @Qualifier("slave") DataSource slaveDataSource,
                                 @Autowired(required = false) @Qualifier("xj") DataSource xj) {
        //数据源集合
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.addDataSource("master", masterDataSource);
        if(Objects.nonNull(slaveDataSource)) {
            dataSource.addDataSource("slave", slaveDataSource);
        }
        if(Objects.nonNull(xj)) {
            dataSource.addDataSource("xj", xj);
        }

        return dataSource;
    }
}
