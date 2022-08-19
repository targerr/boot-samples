package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/8/18 17:04
 * @Classname TestController
 * @Description
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取实例:服务发现，证明内容中心总能找到用户中心
     *
     * @return {@link List}<{@link ServiceInstance}>
     * http://127.0.0.1:8080/test/getInstance
     */
    @GetMapping("/getInstance")
    public List<ServiceInstance> getInstance() {
        return discoveryClient.getInstances("user-center");
    }

}
