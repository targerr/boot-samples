package com.example.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.enums.FilterEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @Author: wgs
 * @Date 2022/8/29 09:53
 * @Classname TokenFilter
 * @Description Ordered 负责filter的顺序，数字越小越优先，越靠前。
 * <p>
 * GatewayFilter：
 * 需要通过spring.cloud.routes.filters 配置在具体路由下，
 * 只作用在当前路由上或通过spring.cloud.default-filters配置在全局，作用在所有路由上。
 * 需要用代码的形式，配置一个RouteLocator，里面写路由的配置信息。
 * <p>
 * GlobalFilter：
 * 全局过滤器，不需要在配置文件中配置，作用在所有的路由上，最终通过GatewayFilterAdapter包装成GatewayFilterChain可识别的过滤器，
 * 它为请求业务以及路由的URI转换为真实业务服务的请求地址的核心过滤器，不需要配置，系统初始化时加载，并作用在每个路由上。
 * 代码配置需要声明一个GlobalFilter对象。
 * <p>
 * <p>
 * 对一个应用来说，GatewayFilter和GlobalFilter是等价的，order也会按照顺序进行拦截。所以两个order不要写一样！
 */
@Slf4j
public class TokenFilter implements GlobalFilter, Ordered {
    @Override
    public Mono filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("【GatewayFilter】进入token过滤器.....");
        ServerHttpRequest request = exchange.getRequest();
        // 请求地址
        String reqPath = request.getURI().getPath();
        // 放行
        if (FilterEnum.isRelease(reqPath)) {
            return chain.filter(exchange);
        }

        HttpHeaders headerNames = request.getHeaders();
        String token = headerNames.getFirst("token");
        if (StringUtils.isBlank(token)) {
            log.error("【GatewayFilter】token不能为空,path:{}", reqPath);
            return unAuth(exchange);
        }

        log.info("【GatewayFilter】token:{},path:{}", token, reqPath);
        if ("eaa1929451cd43efb3f4668eed25e3f9".equals(token)) {
            return chain.filter(exchange);
        }

        // 验证token,顺延；权限校验....

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
        return 0;
    }
}
