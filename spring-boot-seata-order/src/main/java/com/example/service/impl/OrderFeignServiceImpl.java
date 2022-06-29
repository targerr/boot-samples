package com.example.service.impl;

import com.example.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/6/29 09:46
 * @Classname OrderFeignServiceImpl
 * @Description
 */
@Service
@Slf4j
public class OrderFeignServiceImpl implements OrderService {
    @Override
    public Boolean create(Integer count) {
        return null;
    }
}
