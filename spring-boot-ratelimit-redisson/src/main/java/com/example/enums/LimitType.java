package com.example.enums;

/**
 * @Author: wgs
 * @Date 2022/7/1 15:51
 * @Classname LimitType
 * @Description 限流类型
 */

public enum LimitType {
    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP,

    /**
     * 实例限流(集群多后端实例)
     */
    CLUSTER
}
