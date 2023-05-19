package com.example.rpc;

import com.example.rpc.fallback.ClientFallback;
import com.example.rpc.fallback.TestFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/12/6 11:57
 * @Classname TestClient
 * @Description
 */
@FeignClient(name = "sentinel-client",value = "sentinel-client",
//        fallback = ClientFallback.class
        fallbackFactory = TestFeignFallbackFactory.class
)
public interface TestClient {
    @RequestMapping(value = "/sentinel-demo/test/index",
            method = RequestMethod.GET,
            consumes = "application/json", produces = "application/json")
  String index ();
}
