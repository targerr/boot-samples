### springboot整合 websocket

#### 案例

第一步：spring-boot-websocket

~~~xml

<dependencies>
    <!-- socket -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
    <!-- freemarker视图渲染jar -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-freemarker</artifactId>
    </dependency>
</dependencies>
~~~

第二步：创建application.yml文件

~~~yaml
spring:
  freemarker:

    #后缀名freemarker默认后缀为.ftl，当然你也可以改成自己习惯的.html
    suffix: .ftl

    #设定模板的加载路径，多个以逗号分隔，默认: [“classpath:/templates/”]
    template-loader-path: classpath:/templates/
    #设定Template的编码

~~~

第三步： WebSocketConfig配置类

~~~java
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author: wgs
 * @Date 2022/5/13 16:34
 * @Classname WebSocketConfig
 * @Description
 */

@Component
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
~~~

第四步：创建webSocket

~~~java
package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: wgs
 * @Date 2022/5/13 16:34
 * @Classname WebSocket
 * @Description
 */

@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
    }

    public void sendMessage(String message) {
        for (WebSocket webSocket : webSocketSet) {
            log.info("【websocket消息】广播消息, message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

~~~

第五步：创建IndexController

~~~java
package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: wgs
 * @Date 2022/5/13 16:51
 * @Classname IndexController
 * @Description
 */
@Controller
@RequestMapping("/websocket")
public class IndexController {
    @Autowired
    private WebSocket webSocket;

    @ResponseBody
    @GetMapping("/send")
    public String index(String orderId) {
        /*webSocket推送*/
        webSocket.sendMessage(orderId);
        return "ok";
    }

    @GetMapping("/list")
    public String list() {
        return "order/list";
    }
}

~~~

第六步：创建启动类SpringBootWebsocketApplication

~~~java

@SpringBootApplication
public class SpringBootWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootUiSwaggerApplication.class, args);
    }

}

~~~

- 执行启动类main方法启动项目，访问地址：http://localhost:8008/webSocket/list
- 访问地址：http://localhost:8008/webSocket/send

- websocket 协议
  ![image.png](https://upload-images.jianshu.io/upload_images/4994935-3b8f9baa1d73089e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
