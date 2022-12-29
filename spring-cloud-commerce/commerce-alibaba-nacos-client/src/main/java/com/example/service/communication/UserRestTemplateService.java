package com.example.service.communication;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.constant.CommonConstant;
import com.example.vo.JwtToken;
import com.example.vo.UserNameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: wgs
 * @Date 2022/11/25 15:27
 * @Classname UserRestTemplateService
 * @Description
 */
@Service
@Slf4j
public class UserRestTemplateService {

    private final LoadBalancerClient loadBalancerClient;

    public UserRestTemplateService(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }


    /**
     * 从授权中心获取 token
     */
    public JwtToken getTokenFromAuthorityService(UserNameAndPassword userNameAndPassword) {

        // 第一种写死 url
        String requestUrl = "http://127.0.0.1:7001/ecommerce-authority-center" +
                "/authority/token";
        log.info("RestTemplate request url and body: [{}],[{}]",
                requestUrl, JSONObject.toJSONString(userNameAndPassword)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(
                requestUrl,
                new HttpEntity<>(JSONObject.toJSONString(userNameAndPassword), headers),
                JwtToken.class
        );

    }

    /**
     * 从授权服务中获取 JwtToken, 且带有负载均衡
     * @param userNameAndPassword
     * @return
     */
    public JwtToken getTokenFromAuthorityServiceWithLoadBalancer(UserNameAndPassword userNameAndPassword){

        // 第二种方式: 通过注册中心拿到服务的信息(是所有的实例), 再去发起调用
        ServiceInstance serviceInstance = loadBalancerClient.choose(
                CommonConstant.AUTHORITY_CENTER_SERVICE_ID
        );
        log.info("Nacos Client Info: [{}], [{}], [{}]",
                serviceInstance.getServiceId(), serviceInstance.getInstanceId(),
                JSON.toJSONString(serviceInstance.getMetadata()));

        String requestUrl = String.format(
                "http://%s:%s/ecommerce-authority-center/authority/token",
                serviceInstance.getHost(),
                serviceInstance.getPort()
        );
        log.info("login request url and body: [{}], [{}]", requestUrl,
                JSON.toJSONString(userNameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(userNameAndPassword), headers),
                JwtToken.class
        );

    }
}
