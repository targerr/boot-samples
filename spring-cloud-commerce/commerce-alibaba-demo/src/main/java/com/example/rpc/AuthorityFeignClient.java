package com.example.rpc;

/**
 * @Author: wgs
 * @Date 2022/11/25 16:24
 * @Classname AuthorityFeignClient
 * @Description
 */



import com.example.rpc.fallback.AuthorityFallback;
import com.example.rpc.fallback.GetTokenByFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * <h1>与 Authority 服务通信的 Feign Client 接口定义</h1>
 */
@FeignClient(
        value = "commerce-authority-center",
fallbackFactory = GetTokenByFeignFallbackFactory.class
//        fallback = AuthorityFallback.class
)
public interface AuthorityFeignClient {

    /**
     * <h2>通过 OpenFeign 访问 Authority 获取 Token</h2>
     */
    @RequestMapping(value = "/ecommerce-authority-center/authority/token",
            method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    Map<String, Object> getTokenByFeign(@RequestBody Map<String, Object> usernameAndPassword);
}
