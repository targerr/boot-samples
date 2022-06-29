package com.example.service;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: wgs
 * @Date 2022/6/28 16:32
 * @Classname OrderService
 * @Description
 */
@Service
@Slf4j
public class OrderService {

    // RestTemplate必须注入spring管理负责分布式事务不生效
    @Autowired
    private RestTemplate restTemplate;

    @GlobalTransactional
    public Boolean create(Integer count) {
        //调用product 扣库存
        String url = "http://localhost:8086/deduct?productId=5001&count=" + count;
        Boolean result = restTemplate.getForObject(url, Boolean.class);

        if (result != null && result) {
            //可能抛出异常
            if (5 == count) {
                throw new RuntimeException("order发生异常了");
            }
            log.info("数据库开始创建订单");
            return true;
        }
        return false;
    }
}
