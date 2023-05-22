package com.example.rpc.fallback;

import com.example.rpc.TestClient;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/12/6 13:38
 * @Classname ClientFallback
 * @Description
 */
@Component
public class ClientFallback implements TestClient {
    @Override
    public String index() {
        System.err.println("===========");
        return "callback";
    }
}
