package com.example.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wgs
 * @Date 2025/5/29 09:55
 * @Classname OrderWebSocketHandler
 * @Description
 */
@Component
public class OrderWebSocketHandler implements BizWebSocketHandler {

    // 用于存储业务会话，可以按需改为 ConcurrentMap<String, List<WebSocketSession>> 多用户支持
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public String supportedBizType() {
        return "order";
    }

    @Override
    public void handleMessage(WebSocketSession session, TextMessage message) throws IOException {
        String clientId = extractClientId(session); // 假设使用 URI 参数作为唯一标识
        sessionMap.put(clientId, session);

        session.sendMessage(new TextMessage("订单接收成功: " + message.getPayload()));
    }

    public void sendToClient(String clientId, String content) throws IOException {
        WebSocketSession session = sessionMap.get(clientId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage("服务端推送: " + content));
        }
    }

    private String extractClientId(WebSocketSession session) {
        // 示例：从 URI 查询参数中获取 ?clientId=xxx
        // 示例：客户端连接时携带 clientId const ws = new WebSocket("ws://localhost:8080/ws/order?clientId=user123");
        String uri = session.getUri().toString();
        int index = uri.indexOf("clientId=");
        return (index != -1) ? uri.substring(index + 9) : "default";
    }

}