package com.github.datasource.enums;

/**
 * @Author: wgs
 * @Date 2024/9/14 16:23
 * @Classname DataSourceType
 * @Description 数据源
 */
public enum DataSourceType {
    /**
     * 主库
     */
    MASTER,

    /**
     * 从库
     */
    SLAVE,
    /**
     * 灰度
     */
    GRAY;
}
