package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wgs
 * @Date 2023/9/20 10:57
 * @Classname WebSocketServer
 * @Description
 */
@Component
@ServerEndpoint("/WebSocketServer/pushMessage/{id}")
@Slf4j(topic = "m.webSocketServer")
public class WebSocketServer {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * id
     */
    private String id = "";


    /**
     * 连接建立成
     * 功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;
        this.id = id;
        if (webSocketMap.containsKey(id)) {
            webSocketMap.remove(id);
            //加入set中
            webSocketMap.put(id, this);
        } else {
            //加入set中
            webSocketMap.put(id, this);
        }
        sendMessage("初始化连接成功,请输入要挂载的组织节点id。。。");
    }
    /**
     * 连接关闭
     * 调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(id)) {
            webSocketMap.remove(id);
        }
        log.info("导入结束");
    }
    /**
     * 收到客户端消
     * 息后调用的方法
     *
     * @param message 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("消息:" + id + ",报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        if (StrUtil.isNotBlank(message)) {
            try {
                //解析发送的报文
                final JSONObject jsonObject = JSON.parseObject(message);
                final String msg = jsonObject.getString("msg");
                final String step = jsonObject.getString("step");
                sendMessage("现在进行第" + step + "步，传递的信息为：" + msg);
            } catch (Exception e) {
                e.printStackTrace();
                sendMessage("程序出现错误，报错信息为："+e.getMessage());
                sendMessage("请刷新后重试。。。");
            }
        }
    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        log.error("代码错误:" + this.id + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务
     * 器主动推送
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送自定
     * 义消息
     **/
    public static void sendInfo(String message, String id) {
        log.info("发送消息到:" + id + "，报文:" + message);
        if (StrUtil.isNotBlank(id) && webSocketMap.containsKey(id)) {
            webSocketMap.get(id).sendMessage(message);
        } else {
            log.error("用户" + id + ",不在线！");
        }
    }
}
