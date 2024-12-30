package com.example.springbootdrools.drools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2024/11/18 16:20
 * @Classname OrderItem
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    /**
     * 订单项名称
     */
    private String name;

    /**
     * 订单项金额
     */
    private int amount;
}
