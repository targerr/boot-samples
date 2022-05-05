
### Spring Event

####  Spring Event介绍

Spring Event是Spring的事件通知机制，可以将相互耦合的代码解耦，从而方便功能的修改与添加。Spring Event是监听者模式的一个具体实现。

监听者模式包含了监听者Listener、事件Event、事件发布者EventPublish，过程就是EventPublish发布一个事件，被监听者捕获到，然后执行事件相应的方法。

Spring Event的相关API在spring-context包中。

####  Spring Event入门案例

第一步：创建maven工程spring-boot-event并配置pom.xml文件

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.example</groupId>
        <artifactId>boot-samples</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <artifactId>spring-boot-event</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-boot-event</name>
    <description>spring-boot-event</description>
    <properties>
        <java.version>11</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
    </dependencies>


</project>

~~~

第二步：创建OptLogDTO类，用于封装操作日志信息

~~~java
package com.example.dto;

import lombok.Data;

@Data
public class OptLogDTO {
    /**
     * 线程id
     */
    private String threadId;
    /**
     * 线程名称
     */
    private String threadName;
    /**
     * 线程名称
     */
    private String ip;
    /**
     * url
     */
    private String url;
    /**
     * 描述
     */
    private String description;
}

~~~

第三步：创建事件类SysLogEvent

~~~java
package com.example.evnet;


/**
 * @Description 自定义日志事件
 */
public class SysLogEvent extends ApplicationEvent {
    public SysLogEvent(OptLogDTO optLogDTO) {
        super(optLogDTO);
    }
}

~~~

第四步：创建监听器类SysLogListener

~~~java
package com.example.listener;

@Component
public class SysLogListener {

    @Async
    @EventListener(SysLogEvent.class)
    public void saveLogEvent(SysLogEvent event) {
        OptLogDTO sysLog = (OptLogDTO) event.getSource();
        long id = Thread.currentThread().getId();
        System.out.println("监听到日志操作事件：" + sysLog + " 线程id：" + id);
        //日志信息落盘...

    }
}

~~~

第五步：创建Controller，用于发布事件

~~~java
package com.example.controller;


@RestController
@RequestMapping("/event")
public class TestController {
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/index")
    public String index() {
        long threadId = Thread.currentThread().getId();
        //构造操作日志信息
        OptLogDTO logInfo = new OptLogDTO();
        logInfo.setIp("127.0.0.1");
        logInfo.setThreadId(Convert.toStr(threadId));
        logInfo.setThreadName(ThreadUtil.currentThreadGroup().getName());
        logInfo.setDescription("操作日志");

        //构造事件对象
        ApplicationEvent event = new SysLogEvent(logInfo);

        //发布事件
        applicationContext.publishEvent(event);

        System.out.println("发布事件,线程id：" + threadId);
        return "OK";
    }
}

~~~

第六步：创建启动类

~~~java

@SpringBootApplication
@EnableAsync// 启用异步处理
public class SpringBootEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootEventApplication.class, args);
    }

}

~~~

启动项目并访问Controller可以发现监听器触发了。
> http://127.0.0.1:8080/event/index