package com.example.config;

import com.example.handler.MyWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //设置连接路径和处理
        registry.addHandler(new MyWebSocketHandler(), "/ws/serverTwo")
                .setAllowedOrigins("*")
                //设置拦截器
                .addInterceptors(new MyWebSocketInterceptor());
    }

    /**
     * 自定义拦截器拦截WebSocket请求
     */
    class MyWebSocketInterceptor implements HandshakeInterceptor {
        //前置拦截一般用来注册用户信息，绑定 WebSocketSession
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            log.info("【websocket】前置拦截");
            if (!(request instanceof ServletServerHttpRequest)) {
                return true;
            }
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String username = servletRequest.getParameter("username");
            attributes.put("username", username);
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {
            log.info("【websocket】后置拦截");
        }
    }
}
