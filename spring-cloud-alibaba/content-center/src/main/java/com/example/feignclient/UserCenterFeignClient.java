package com.example.feignclient;

import com.example.share.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: wgs
 * @Date 2022/8/23 09:26
 * @Classname UserCenterFeignClient
 * @Description
 */
@FeignClient("user-center")
public interface UserCenterFeignClient {
    /**
     * http://user-center/users/{id}
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public UserDTO findById(@PathVariable String id);
}
