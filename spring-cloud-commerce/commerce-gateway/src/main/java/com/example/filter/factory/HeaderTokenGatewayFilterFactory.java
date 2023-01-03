package com.example.filter.factory;

import com.example.filter.HeaderTokenGatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/11/10 11:44
 * @Classname HeaderTokenGatewayFilterFactory
 * @Description
 */

@Component
public class HeaderTokenGatewayFilterFactory
        extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return new HeaderTokenGatewayFilter();
    }
}
