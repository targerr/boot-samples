package com.example.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @Author: wgs
 * @Date 2022/11/10 11:43
 * @Classname HeaderTokenGatewayFilter
 * @Description 请求头部携带 Token 验证过滤器
 */
@Slf4j
public class HeaderTokenGatewayFilter implements GatewayFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("【GatewayFilter】进入token过滤器.....");
        // 从 HTTP Header 中寻找 key 为 token, value 为 imooc 的键值对
        String name = exchange.getRequest().getHeaders().getFirst("token");
        if ("imooc".equals(name)) {
            return chain.filter(exchange);
        }

        return unAuth(exchange);
    }

    private Mono<Void> unAuth(ServerWebExchange serverWebExchange) {
        // 权限不够拦截
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "无权限");
        jsonObject.put("statusCode", HttpStatus.UNAUTHORIZED);
        //不允许访问，禁止访问
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(JSON.toJSONString(jsonObject).getBytes(StandardCharsets.UTF_8));
        ServerHttpResponse response = serverWebExchange.getResponse();
        //这个状态码是401
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }
}
