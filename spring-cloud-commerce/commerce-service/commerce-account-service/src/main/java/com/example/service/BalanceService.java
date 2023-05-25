package com.example.service;

import com.example.account.BalanceInfo;

/**
 * @Author: wgs
 * @Date 2022/11/15 11:54
 * @Classname IBalanceService
 * @Description 用户余额信息相关接口定义
 */
public interface BalanceService {
    /**
     * 获取当前用户余额信息
     * @return
     */
    BalanceInfo getCurrentUserBalanceInfo();

    /**
     * 扣减用户余额
     * @param balanceInfo
     * @return
     */
    BalanceInfo deductBalance(BalanceInfo balanceInfo);
}
