package com.example.dal;

/**
 * @Author: wgs
 * @Date 2024/4/23 14:22
 * @Classname MasterSlaveDsEnum
 * @Description 主从数据源的枚举
 */
public enum MasterSlaveDsEnum implements DS {
    /**
     * master主数据源类型
     */
    MASTER,
    /**
     * slave从数据源类型
     */
    SLAVE;
}
