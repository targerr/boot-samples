package com.example.config;

//import com.example.handler.MyWebSocketHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * @Author: wgs
 * @Date 2023/9/20 11:19
 * @Classname WebSocketConfig
 * @Description springboot 整合websocket 配置类
 */

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private AbstractWebSocketHandler dispatcherHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(dispatcherHandler, "/ws/{bizType}")
                .setAllowedOrigins("*");
    }
}
