package com.example.order;

import cn.monitor4all.logRecord.annotation.OperationLog;
import cn.monitor4all.logRecord.context.LogRecordContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/5/16 16:51
 * @Classname OrderController
 * @Description
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @OperationLog(bizType = "addressChange", bizId = "#order.orderNo", msg = "'用户' + #userName + '修改了订单的配送地址：从' + #oldAddress + '修改到' + #order.newAddress")

    @PostMapping("/create")
    public JSONObject createOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());

        // 手动传递日志上下文：用户信息 地址旧值
        LogRecordContext.putVariables("userName", "王亚茹");
        LogRecordContext.putVariables("oldAddress", "滨江区");
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        return JSON.parseObject(JSON.toJSONString(order));
    }
}
