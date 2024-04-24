package com.example.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * @Author: wgs
 * @Date 2024/4/23 14:45
 * @Classname MyRoutingDataSource
 * @Description 多数据源
 */
@Slf4j
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("返回数据源: {}", DsContextHolder.get());
        return DsContextHolder.get();
    }
}
