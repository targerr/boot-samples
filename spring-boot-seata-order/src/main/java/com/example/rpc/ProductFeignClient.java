package com.example.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: wgs
 * @Date 2022/6/28 16:33
 * @Classname ProductFeignClient
 * @Description
 */
@FeignClient(name = "seata-product", url = "http://localhost:8086")
public interface ProductFeignClient {

    @GetMapping("/deduct")
    Boolean deduct(@RequestParam Long productId,
                   @RequestParam Integer count);
}
