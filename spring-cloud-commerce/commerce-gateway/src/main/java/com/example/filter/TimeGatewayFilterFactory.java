package com.example.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/11/10 16:16
 * @Classname TimeGatewayFilterFactory
 * @Description 耗时
 */
@Component
@Slf4j
public class TimeGatewayFilterFactory extends AbstractGatewayFilterFactory<TimeGatewayFilterFactory.Config> {

    private static final String BEGIN_TIME = "beginTime";

    //构造函数
    public TimeGatewayFilterFactory() {
        super(Config.class);
    }

    //读取配置文件中的参数 赋值到 配置类中
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("show");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                log.error("耗时局部过滤器执行~~~~");
                if (!config.show) {
                    // 如果配置类中的show为false，表示放行
                    return chain.filter(exchange);
                }
                exchange.getAttributes().put(BEGIN_TIME, System.currentTimeMillis());
                /**
                 *  pre的逻辑
                 * chain.filter().then(Mono.fromRunable(()->{
                 *     post的逻辑
                 * }))
                 */
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(BEGIN_TIME);
                    if (startTime != null) {
                        log.info("【Gateway】 接口地址:{} 耗时: {} ms", exchange.getRequest().getURI(), (System.currentTimeMillis() - startTime));
                    }
                }));
            }
        };
    }

    @Setter
    @Getter
    static class Config {
        private boolean show;
    }

}