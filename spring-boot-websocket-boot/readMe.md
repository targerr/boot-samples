# WebSocket学习

# 环境设置

* JDK8
* Maven3.5
* springboot 2.6.8

# 简介

Java使用websocket有两种方式：

​	①使用 Tomcat 7 的 @ServerEndpoint 进行 WebSocket 开发

​	②使用 SpringBoot 的形式构建 WebSocket 应用程序

**<font color='red'>注：不管使用那种方式，都必须引用【spring-boot-starter-websocket】包（如果只是简单Java项目可以不用引用；如果使用springboot项目，使用@ServerEndpoint不引用会有bug！）</font>**

# Maven依赖

```xml
<!--webSocket-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

# 原生Tomcat格式

## WebSocket配置类

```java
package com.maben.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * websocket
 * 的配置信息
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }
}
```

## 测试页面入口类

```java
package com.maben.websocket.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 进入websocket测试页面
 */
@RestController
@RequestMapping("WebSocketController")
public class WebSocketController {
    @RequestMapping("init")
    public ModelAndView init(){
        return new ModelAndView("webSocket/webSocket");
    }
}
```

## WebSocketServer类

```java
package com.maben.websocket.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

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
        if (StringUtils.isNotBlank(message)) {
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
        if (StringUtils.isNotBlank(id) && webSocketMap.containsKey(id)) {
            webSocketMap.get(id).sendMessage(message);
        } else {
            log.error("用户" + id + ",不在线！");
        }
    }
}
```

## 前端页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>websocket学习</title>

    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <style>
    </style>
</head>
<body style="background-color: #FFFFFF">
    <h1>websocket学习</h1>
    <div style="width: 100%;height: 100%;">
        <table>
            <tr>
                <td>
                    <input id="msg" type="text" value="" placeholder="请填写信息" onkeydown="enterLis()">
                </td>
                <td>
                    <input type="button" value="提交" onclick="sendMessage()" class="oa_btn">
                </td>
            </tr>
        </table>
    </div>
    <div id="cons" style="border: 1px solid red; font-size: 12px;"></div>
</body>
<script>
    var socket;
    var step = 1;
    function openSocket() {
        var host= window.location.host;

        var socketUrl = "ws://"+host+"/WebSocketServer/pushMessage/" + new Date().getTime();
        console.log(socketUrl);
        if(socket!=null){
            socket.close();
            socket=null;
        }
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function() {
            console.log("websocket已打开");
        };
        //获得消息事件
        socket.onmessage = function(msg) {
            console.log(msg.data);
            //发现消息进入,开始处理前端触发逻辑
            $("#cons").append("<p style='margin-left: 15px;'>"+msg.data+"</p>");
        };
        //关闭事件
        socket.onclose = function() {
            console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function() {
            console.log("websocket发生了错误");
        }
    }
    $(function () {
        openSocket();
        setInterval(function () {
            var lastDom = $("#cons p:last");
            var content = lastDom.text();
            if (content.endsWith("。。。")) {
                lastDom.text(content.substr(0,content.length-1));
            }else if (content.endsWith("。。")){
                lastDom.text(content.substr(0,content.length-1));
            } else if (content.endsWith("。")){
                lastDom.text(content+"。。");
            }
        },1000)
    });
    function sendMessage() {
        var msg = {
            step: step,
            msg : $("#msg").val()
        };
        socket.send(JSON.stringify(msg));
        step+=1;
        $("#msg").val('');
    }
    function enterLis(event) {
        var oEvent = event || window.event || arguments.callee.caller.arguments[0];
        var keyCode = oEvent.keyCode;
        if (keyCode === 13) {
            sendMessage();
        }
    }

</script>
</html>
```

# springboot推荐方式

## WebSocket配置类

```java
package com.maben.websocket.config;

import com.maben.websocket.Handler.MyWebSocketHandler;
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
 * springboot 整合websocket 配置类
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(), "/ws/serverTwo")//设置连接路径和处理
                .setAllowedOrigins("*")
                .addInterceptors(new MyWebSocketInterceptor());//设置拦截器
    }

    /**
     * 自定义拦截器拦截WebSocket请求
     */
    class MyWebSocketInterceptor implements HandshakeInterceptor {
        //前置拦截一般用来注册用户信息，绑定 WebSocketSession
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            System.out.println("前置拦截~~");
            if (!(request instanceof ServletServerHttpRequest)) return true;
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String username = servletRequest.getParameter("username");
            attributes.put("username", username);
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {
            System.out.println("后置拦截~~");
        }
    }
}
```

## 测试页面入口类

```java
package com.maben.websocket.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 进入websocket测试页面
 */
@RestController
@RequestMapping("WebSocketController")
public class WebSocketController {
    @RequestMapping("init")
    public ModelAndView init(){
        return new ModelAndView("webSocket/webSocket");
    }
}
```

## WebSocket处理器

```java
package com.maben.websocket.Handler;
import org.springframework.web.socket.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket处理器
 */
public class MyWebSocketHandler implements WebSocketHandler {
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    /**
     * 连接成功之后执行
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userName = session.getAttributes().get("username").toString();
        SESSIONS.put(userName, session);
        System.out.println(String.format("成功建立连接~ username: %s", userName));
    }

    /**
     * 处理信息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String msg = message.getPayload().toString();
        final String username = session.getAttributes().get("username").toString();
        System.out.println(username+"::"+msg);
        //给前端实时发送数据
        MyWebSocketHandler.sendMessage(username,msg);
    }

    /**
     * 连接报错处理
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("连接出错");
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
        System.out.println("连接已关闭,username:"+username+";status:" + closeStatus);
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
```

## 前端页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>websocket学习</title>

    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <style>
    </style>
</head>
<body style="background-color: #FFFFFF">
    <h1>websocket学习</h1>
    <div style="width: 100%;height: 100%;">
        <table>
            <tr>
                <td>
                    <input id="msg" type="text" value="" placeholder="请填写信息" onkeydown="enterLis()">
                </td>
                <td>
                    <input type="button" value="提交" onclick="sendMessage()" class="oa_btn">
                </td>
            </tr>
        </table>
    </div>
    <div id="cons" style="border: 1px solid red; font-size: 12px;"></div>
</body>
<script>
    var socket;
    var step = 1;
    function openSocket() {
        var host= window.location.host;

        var socketUrl = "ws://"+host+"/ws/serverTwo?username=" + new Date().getTime();
        console.log(socketUrl);
        if(socket!=null){
            socket.close();
            socket=null;
        }
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function() {
            console.log("websocket已打开");
        };
        //获得消息事件
        socket.onmessage = function(msg) {
            console.log(msg.data);
            //发现消息进入,开始处理前端触发逻辑
            $("#cons").append("<p style='margin-left: 15px;'>"+msg.data+"</p>");
        };
        //关闭事件
        socket.onclose = function() {
            console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function() {
            console.log("websocket发生了错误");
        }
    }
    $(function () {
        openSocket();
        setInterval(function () {
            var lastDom = $("#cons p:last");
            var content = lastDom.text();
            if (content.endsWith("。。。")) {
                lastDom.text(content.substr(0,content.length-1));
            }else if (content.endsWith("。。")){
                lastDom.text(content.substr(0,content.length-1));
            } else if (content.endsWith("。")){
                lastDom.text(content+"。。");
            }
        },1000)
    });
    function sendMessage() {
        var msg = {
            step: step,
            msg : $("#msg").val()
        };
        socket.send(JSON.stringify(msg));
        step+=1;
        $("#msg").val('');
    }
    function enterLis(event) {
        var oEvent = event || window.event || arguments.callee.caller.arguments[0];
        var keyCode = oEvent.keyCode;
        if (keyCode === 13) {
            sendMessage();
        }
    }

</script>
</html>
```

### 测试

![image.png](https://upload-images.jianshu.io/upload_images/4994935-e46cd42c28510a53.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### [聊天](https://gitee.com/huanzi-qch/springBoot/blob/master/springboot-websocket/src/main/java/cn/huanzi/qch/springbootwebsocket/WebSocketServer.java)
#### [聊天文档](https://huanzi-qch.gitee.io/spring-boot/#/docs/%E3%80%8ASpringBoot%E7%B3%BB%E5%88%97%E2%80%94%E2%80%94WebSocket%E3%80%8B)