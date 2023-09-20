package com.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wgs
 * @Date 2023/9/20 11:17
 * @Classname MyWebSocketHandler
 * @Description websocket处理器
 */
@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    /**
     * 连接成功之后执行
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userName = session.getAttributes().get("username").toString();
        SESSIONS.put(userName, session);
        log.info("【websocket】建立连接 username: {} ",userName);
    }

    /**
     * 处理信息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String msg = message.getPayload().toString();
        final String username = session.getAttributes().get("username").toString();
        log.info("【websocket】 消息: {} ",msg);

        //给前端实时发送数据
        MyWebSocketHandler.sendMessage(username,msg);
    }

    /**
     * 连接报错处理
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("【websocket】连接出错 ");
        if (session.isOpen()) {
            session.close();
        }
    }

    /**
     * 连接关闭处理
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        final String username = session.getAttributes().get("username").toString();
        log.info("【websocket】连接已关闭,username:{}; status:{} ",username,closeStatus);
    }
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    /**
     * 指定发消息
     */
    public static void sendMessage(String userName, String message) {
        WebSocketSession webSocketSession = SESSIONS.get(userName);
        if (webSocketSession == null || !webSocketSession.isOpen()) return;
        try {
            webSocketSession.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 群发消息
     */
    public static void fanoutMessage(String message) {
        SESSIONS.keySet().forEach(us -> sendMessage(us, message));
    }
}
