package com.example.controller;

import com.example.rpc.AuthorityFeignClient;
import com.example.rpc.TestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/11/30 09:22
 * @Classname SentinelController
 * @Description
 */
@RestController
@RequestMapping("/sentinel")
public class SentinelController {

    @Autowired
    private AuthorityFeignClient feignClient;

    @Autowired
    private TestClient testClient;

    @GetMapping("/test")
    public String test(){
        return testClient.index();
    }

    @GetMapping("/index")
    public String index(){
        return  "success";
    }

    @PostMapping("/token-by-feign")
    public Map getTokenByFeign(@RequestBody Map usernameAndPassword) {
        return feignClient.getTokenByFeign(usernameAndPassword);
    }
}
