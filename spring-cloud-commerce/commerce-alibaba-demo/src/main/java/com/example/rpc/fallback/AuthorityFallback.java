package com.example.rpc.fallback;

import com.example.rpc.AuthorityFeignClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/11/30 13:59
 * @Classname AuthorityFallback
 * @Description
 */
@Component
public class AuthorityFallback implements AuthorityFeignClient {
    @Override
    public Map<String, Object> getTokenByFeign(Map<String, Object> usernameAndPassword) {
        Map<String,Object> result = new HashMap<>();
        result.put("msg","触发失败");

        return result;
    }
}
