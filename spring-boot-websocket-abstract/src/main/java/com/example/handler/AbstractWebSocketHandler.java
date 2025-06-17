package com.example.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wgs
 * @Date 2025/5/29 09:53
 * @Classname AbstractWebSocketHandler
 * @Description
 */
@Component
public class AbstractWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, BizWebSocketHandler> handlerMap = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>(); // 用于存储客户端连接

    public AbstractWebSocketHandler(List<BizWebSocketHandler> handlers) {
        for (BizWebSocketHandler handler : handlers) {
            handlerMap.put(handler.supportedBizType(), handler);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 提取 bizType 和 clientId
        String uri = session.getUri().toString();
        String bizType = uri.substring(uri.lastIndexOf("/") + 1, uri.indexOf("?"));
        String clientId = extractClientId(uri);

        // 存储会话及其 clientId 映射
        session.getAttributes().put("bizType", bizType);
        session.getAttributes().put("clientId", clientId);
        sessionMap.put(clientId, session); // 将会话与 clientId 关联

        System.out.println("连接建立: " + bizType + "，ClientId: " + clientId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String bizType = (String) session.getAttributes().get("bizType");
        BizWebSocketHandler handler = handlerMap.get(bizType);
        if (handler != null) {
            handler.handleMessage(session, message);
        } else {
            session.sendMessage(new TextMessage("不支持的业务类型: " + bizType));
        }
    }

    // 提取 clientId（从 URL 查询参数中获取）
    private String extractClientId(String uri) {
        int index = uri.indexOf("clientId=");
        if (index != -1) {
            return uri.substring(index + 9); // 获取 clientId 的值
        }
        return "default"; // 默认值
    }

    // 推送消息到指定的 clientId
    public void sendToClient(String clientId, String content) throws IOException {
        WebSocketSession session = sessionMap.get(clientId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(content));
        } else {
            System.out.println("客户端 " + clientId + " 不在线");
        }
    }
}
