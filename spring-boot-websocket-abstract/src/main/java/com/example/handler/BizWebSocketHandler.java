package com.example.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @Author: wgs
 * @Date 2025/5/29 09:54
 * @Classname BizWebSocketHandler
 * @Description
 */
public interface BizWebSocketHandler {
    String supportedBizType();

    void handleMessage(WebSocketSession session, TextMessage message) throws IOException;
}
