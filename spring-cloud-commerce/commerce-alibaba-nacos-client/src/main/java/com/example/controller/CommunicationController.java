package com.example.controller;

import com.example.service.communication.AuthorityFeignClient;
import com.example.service.communication.UseFeignApi;
import com.example.service.communication.UseRibbonService;
import com.example.service.communication.UserRestTemplateService;
import com.example.vo.JwtToken;
import com.example.vo.UserNameAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/11/25 15:39
 * @Classname CommunicationController
 * @Description 微服务通信
 */
@RestController
@RequestMapping("/communication")
public class CommunicationController {


    private final UserRestTemplateService restTemplateService;
    private final UseRibbonService ribbonService;
    private final AuthorityFeignClient feignClient;
    private final UseFeignApi useFeignApi;


    public CommunicationController(UserRestTemplateService restTemplateService, UseRibbonService ribbonService,
                                   AuthorityFeignClient feignClient, UseFeignApi useFeignApi) {
        this.restTemplateService = restTemplateService;
        this.ribbonService = ribbonService;
        this.useFeignApi = useFeignApi;
        this.feignClient = feignClient;
    }

    @PostMapping("/rest-template")
    public JwtToken getTokenFromAuthorityService(
            @RequestBody UserNameAndPassword usernameAndPassword) {
        return restTemplateService.getTokenFromAuthorityService(usernameAndPassword);
    }

    @PostMapping("/rest-template-load-balancer")
    public JwtToken getTokenFromAuthorityServiceWithLoadBalancer(@RequestBody UserNameAndPassword userNameAndPassword){
        return restTemplateService.getTokenFromAuthorityServiceWithLoadBalancer(userNameAndPassword);
    }

    @PostMapping("/ribbon")
    public JwtToken getTokenFromAuthorityServiceByRibbon(
            @RequestBody UserNameAndPassword usernameAndPassword) {
        return ribbonService.getTokenFromAuthorityServiceByRibbon(usernameAndPassword);
    }

    @PostMapping("/thinking-in-ribbon")
    public JwtToken thinkingInRibbon(@RequestBody UserNameAndPassword usernameAndPassword) {
        return ribbonService.thinkingInRibbon(usernameAndPassword);
    }

    @PostMapping("/token-by-feign")
    public JwtToken getTokenByFeign(@RequestBody UserNameAndPassword usernameAndPassword) {
        return feignClient.getTokenByFeign(usernameAndPassword);
    }

    @PostMapping("/thinking-in-feign")
    public JwtToken thinkingInFeign(@RequestBody UserNameAndPassword usernameAndPassword) {
        return useFeignApi.thinkingInFeign(usernameAndPassword);
    }

}
