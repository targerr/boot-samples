package com.example.service.impl;

import com.example.rpc.ProductFeignClient;
import com.example.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    //Feign
    @Autowired
    private ProductFeignClient productFeignClient;
    @GlobalTransactional
    public Boolean create(Integer count) {
        //调用product 扣库存
        Boolean result = productFeignClient.deduct(5001L, count);
        if (result != null && result) {
            //可能抛出异常
            if (5 == count) {
                throw new RuntimeException("order故意发生异常!");
            }
            log.info("数据库开始创建订单");
            return true;
        }

        return false;
    }
}
