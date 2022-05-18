package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/5/16 16:50
 * @Classname Order
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    public Long orderId;
    public String orderNo;
    public String productName;
    public String userName;
    public String newAddress;
    public Date createTime;

    public UserDO creator;
    public UserDO updater;
    public List<String> items;

    @Data
    public static class UserDO {
        public Long userId;
        public String userName;
    }
}