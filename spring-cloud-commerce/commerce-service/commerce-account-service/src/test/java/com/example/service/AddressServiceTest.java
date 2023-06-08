package com.example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.BaseTest;
import com.example.account.AddressInfo;
import com.example.entity.EcommerceAddress;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

/**
 * @Author: wgs
 * @Date 2022/11/16 15:51
 * @Classname AddressServiceTest
 * @Description 地址服务测试
 */
@Slf4j
public class AddressServiceTest extends BaseTest {
    @Autowired
    private AddressService addressService;

    /**
     * 测试新增用户地址
     */
    @Test
    public void testCreateAddress() {
        AddressInfo.AddressItem addressItem = new AddressInfo.AddressItem();
        addressItem.setUsername("tom");
        addressItem.setPhone("18800000001");
        addressItem.setProvince("上海市");
        addressItem.setCity("上海市");
        addressItem.setAddressDetail("陆家嘴");

        log.info("test create address info: [{}]", JSON.toJSONString(
                addressService.createAddressInfo(
                        new AddressInfo(loginUserInfo.getId(), Collections.singletonList(addressItem))
                )
        ));
    }

    /**
     * 获取当前用户信息
     */
    @Test
    public void testGetCurrentUserAddress() {
        log.info("获取当前用户地址信息:{}",
                JSONObject.toJSONString(addressService.getCurrentAddressInfo())
        );
    }

    @Test
    public void testGetById() {
        log.info("根据 id 获取用户信息: {}",
                JSONObject.toJSONString(addressService.getAddressInfoById(10L),true)
        );
    }
}
