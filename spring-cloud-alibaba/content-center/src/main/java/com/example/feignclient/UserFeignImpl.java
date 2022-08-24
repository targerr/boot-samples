package com.example.feignclient;

import com.example.share.dto.user.UserDTO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/8/23 10:21
 * @Classname UserFeignImpl
 * @Description
 */
@Slf4j
@Component
public class UserFeignImpl implements FallbackFactory<UserCenterFeignClient> {
    @Override
    public UserCenterFeignClient create(Throwable throwable) {
        log.error("【用户服务】异常: msg{}", throwable.getMessage());
        return id -> UserDTO.builder()
                .wxNickname("无")
                .id(id)
                .build();
    }
}
