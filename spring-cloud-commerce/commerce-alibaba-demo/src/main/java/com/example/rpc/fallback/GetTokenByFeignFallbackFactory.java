package com.example.rpc.fallback;

import com.example.rpc.AuthorityFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * OpenFeign Fallback Factory书写规则
 * 1. 实现FallbackFactory接口,且泛型为对应的FeignClient
 * 2. 增加@Component让Spring对其实例化并IOC管理
 * 3. 在create方法中实现FeignClient,说明限流/熔断时的操作
 */
@Component
public class GetTokenByFeignFallbackFactory implements FallbackFactory<AuthorityFeignClient> {

    @Override
    public AuthorityFeignClient create(Throwable throwable) {
        System.err.println("--------");
        return new AuthorityFeignClient() {
            @Override
            public Map<String, Object> getTokenByFeign(Map<String, Object> usernameAndPassword) {
                Map<String,Object> result = new HashMap<>();
                result.put("msg","触发失败");

                return result;
            }
        };
    }
}
