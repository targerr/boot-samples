### springboot整合 spring-session

#### 案例

第一步：spring-boot-spring-session

~~~xml

<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session-data-redis</artifactId>
    </dependency>

</dependencies>
~~~

第二步：创建application.yml文件

~~~yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
  session:
    store-type: redis
    timeout: 3600s

~~~

第三步：创建SessionController

~~~java
package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author: wgs
 * @Date 2022/5/13 10:30
 * @Classname SessionController
 * @Description
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    @GetMapping("/login")
    public String login(String name, String age, HttpSession session) {
        // 业务验证......
        session.setAttribute("user", name + ":" + age);

        return "login success!";
    }

    @GetMapping("/info")
    public String info(HttpSession session) {
        final Object user = session.getAttribute("user");

        return user.toString();
    }
}


~~~

第六步：创建启动类SpringBootTokenSpringSessionApplication

~~~java

@SpringBootApplication
@EnableRedisHttpSession
public class SpringBootTokenSpringSessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTokenSpringSessionApplication.class, args);
    }

}


~~~

- 执行启动类main方法启动项目，访问地址：127.0.0.1:8080/session/login?name=tom&age=12