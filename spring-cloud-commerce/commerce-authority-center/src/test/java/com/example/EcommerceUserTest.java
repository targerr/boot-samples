package com.example;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.example.dao.EcommerceUserDao;
import com.example.entity.EcommerceUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Author: wgs
 * @Date 2022/11/8 16:21
 * @Classname EcommerceUserTest
 * @Description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class EcommerceUserTest {
    @Autowired
    private EcommerceUserDao ecommerceUserDao;

    @Test
    public void createUserRecord() {

        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername("tom");
        ecommerceUser.setPassword(MD5.create().digestHex("12345678"));
        ecommerceUser.setExtraInfo("{}");
        ecommerceUser.setCreateTime(new Date());
        ecommerceUser.setUpdateTime(new Date());
        log.info("save user: [{}]",
                JSON.toJSON(ecommerceUserDao.save(ecommerceUser)));
    }
}
