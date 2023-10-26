package com.example.service;

/**
 * @Author: wgs
 * @Date 2023/10/25
 * @Classname UserStatisticService
 * @since 1.0.0
 * 用户统计服务
 */
public interface UserStatisticService {
    /**
     * 添加在线人数
     *
     * @param add 正数，表示添加在线人数；负数，表示减少在线人数
     * @return
     */
    int incrOnlineUserCnt(int add);

    /**
     * 查询在线用户人数
     *
     * @return
     */
    int getOnlineUserCnt();
}
