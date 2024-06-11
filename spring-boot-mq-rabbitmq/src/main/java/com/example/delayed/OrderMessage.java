package com.example.delayed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Author: wgs
 * @Date 2024/6/11 15:23
 * @Classname OrderMessage
 * @Description 定义一个消息体类，用来存储需要发送的消息：
 * 参考： https://zhuanlan.zhihu.com/p/641458427*
 */
@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage implements Serializable {

    /**
     * 商户订单号
     */
    private String orderId;

    /**
     * 支付宝订单号
     */
    private String tradeNo;
}
