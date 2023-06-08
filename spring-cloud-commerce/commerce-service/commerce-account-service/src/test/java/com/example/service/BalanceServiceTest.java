package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.BaseTest;
import com.example.account.BalanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: wgs
 * @Date 2022/11/16 15:44
 * @Classname BalanceServiceTest
 * @Description 用户余额相关测试
 */
@Slf4j
public class BalanceServiceTest extends BaseTest {
    @Autowired
    private BalanceService balanceService;

    @Test
    public void testGetCurrentUserBalance() {
        log.info("当前用户余额信息:{}",
                JSONObject.toJSONString(balanceService.getCurrentUserBalanceInfo())
        );
    }

    @Test
    public void deductBalance(){
        BalanceInfo balanceInfo = new BalanceInfo(10L,5L);
        log.info("测试扣减用户余额: {}",
                JSONObject.toJSONString(balanceService.deductBalance(balanceInfo))
                );
    }
}
