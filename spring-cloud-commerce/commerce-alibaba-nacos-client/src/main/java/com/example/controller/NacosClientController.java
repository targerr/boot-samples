package com.example.controller;

import com.example.service.NacosClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/11/7 14:34
 * @Classname NacosClientController
 * @Description
 */
@RestController
@RequestMapping(value = "/nacos-client")
@Slf4j
public class NacosClientController {
    private final NacosClientService nacosClientService;

    public NacosClientController(NacosClientService nacosClientService) {
        this.nacosClientService = nacosClientService;
    }

    /**
     * <h2>根据 service id 获取服务所有的实例信息</h2>
     * */
    @GetMapping("/service-instance")
    public List<ServiceInstance> logNacosClientInfo(@RequestParam(defaultValue = "commerce-nacos-client") String serviceId){
        //log.info("查询指定实例信息: [{}]", serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }
}
