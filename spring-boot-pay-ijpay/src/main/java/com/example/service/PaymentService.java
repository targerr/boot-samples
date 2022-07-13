package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.PayParam;

/**
 * @Author: wgs
 * @Date 2022/7/13 09:59
 * @Classname PaymentService
 * @Description
 */
public interface PaymentService {

    /**
     * 获取到接口code
     *
     * @return
     */
    String getIfCode();

    /**
     * 是否支持该支付方式
     *
     * @param wayCode
     * @return
     */
    boolean isSupport(String wayCode);

    /**
     * 调起支付接口
     *
     * @param payParam
     * @return
     */
    JSONObject pay(PayParam payParam);
}
