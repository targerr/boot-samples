package com.example.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.PayParam;
import com.example.service.PaymentService;

/**
 * @Author: wgs
 * @Date 2022/6/14 16:07
 * @Classname WxpayPaymentService
 * @Description
 */
public class WxpayPaymentService implements PaymentService {

    @Override
    public String getIfCode() {
        return "wxpay";
    }

    @Override
    public boolean isSupport(String wayCode) {
        return false;
    }

    @Override
    public JSONObject pay(PayParam payParam) {
        return null;
    }
}
