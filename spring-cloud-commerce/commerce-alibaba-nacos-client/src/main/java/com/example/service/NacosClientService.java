package com.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Service;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/11/7 15:15
 * @Classname NacosClientService
 * @Description
 */
@Service
@Slf4j
public class NacosClientService {
    private final DiscoveryClient discoveryClient;

    public NacosClientService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /** 根据 service id 获取服务实例信息 */
    @GetMapping("/service-instance")
    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
        //log.info("请求nacos客户端获取服务实例信息:{}", serviceId);
        return discoveryClient.getInstances(serviceId);
    }
}
