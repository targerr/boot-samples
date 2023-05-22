package com.example.rpc.fallback;

import com.example.rpc.TestClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/12/6 14:13
 * @Classname TestFeignFallbakFactory
 * @Description
 */
@Component
public class TestFeignFallbackFactory implements FallbackFactory<TestClient> {
    @Override
    public TestClient create(Throwable throwable) {
        return new TestClient() {
            @Override
            public String index() {
                return "~~~~~";
            }
        };
    }
}
