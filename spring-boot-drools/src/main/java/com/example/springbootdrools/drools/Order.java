package com.example.springbootdrools.drools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2024/11/18 15:58
 * @Classname Order
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    /**
     * 订单金额
     */
    private int amount;

    /**
     * 积分
     */
    private int score;

    List<OrderItem> orderItemList;
}
