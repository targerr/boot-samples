package com.example.service.communication;

/**
 * @Author: wgs
 * @Date 2022/11/25 16:24
 * @Classname AuthorityFeignClient
 * @Description
 */

import com.example.vo.JwtToken;
import com.example.vo.UserNameAndPassword;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * <h1>与 Authority 服务通信的 Feign Client 接口定义</h1>
 * */
@FeignClient(
        contextId = "AuthorityFeignClient", value = "commerce-authority-center"
)
public interface AuthorityFeignClient {

    /**
     * <h2>通过 OpenFeign 访问 Authority 获取 Token</h2>
     * */
    @RequestMapping(value = "/ecommerce-authority-center/authority/token",
            method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    JwtToken getTokenByFeign(@RequestBody UserNameAndPassword usernameAndPassword);
}
