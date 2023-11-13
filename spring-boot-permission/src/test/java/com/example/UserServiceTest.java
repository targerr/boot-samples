package com.example;

import com.alibaba.fastjson.JSONObject;
import com.example.dto.UserStatisticInfoDTO;
import com.example.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: wgs
 * @Date 2023/10/27
 * @Classname UserServiceTest
 * @since 1.0.0
 */
@Slf4j
public class UserServiceTest extends BasicTest{
    @Autowired
    private SysUserService userService;

    @Test
    public void testUserHome() throws Exception {
        UserStatisticInfoDTO userHomeDTO = userService.queryUserInfoWithStatistic(1);
        log.info("query userPageDTO: {}", JSONObject.toJSONString(userHomeDTO,true));
    }
}
