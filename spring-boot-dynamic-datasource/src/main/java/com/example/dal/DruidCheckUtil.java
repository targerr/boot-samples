package com.example.dal;

import org.springframework.util.ClassUtils;

/**
 * @Author: wgs
 * @Date 2024/4/23 14:30
 * @Classname DruidCheckUtil
 * @Description
 */
public class DruidCheckUtil {
    /**
     * 判断是否包含durid相关的数据包
     *
     * @return
     */
    public static boolean hasDuridPkg() {
        return ClassUtils.isPresent("com.alibaba.druid.pool.DruidDataSource", DataSourceConfig.class.getClassLoader());
    }

}
