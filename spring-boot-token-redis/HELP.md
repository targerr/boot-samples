### springboot整合 token-redis

#### 案例

第一步：spring-boot-token-redis

~~~xml

<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>


</dependencies>
~~~

第二步：创建application.yml文件

~~~yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379


~~~

第三步：创建SessionController

~~~java
package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/5/13 10:30
 * @Classname SessionController
 * @Description
 */
@RestController
@RequestMapping("/token")
public class SessionController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password) {
        // 业务验证......
        // 账号密码正确
        String key = "token_" + UUID.randomUUID();
        stringRedisTemplate.opsForValue().set(key, name, 2, TimeUnit.DAYS);
        return "login success! token: " + key;
    }

    @GetMapping("/info")
    public String info(@RequestHeader String token) {
        return "当前登录的用户是: " + stringRedisTemplate.opsForValue().get(token);

    }
}


~~~

第四步：创建启动类SpringBootTokenRedisApplication

~~~java

@SpringBootApplication
@EnableRedisHttpSession
public class SpringBootTokenRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTokenRedisApplication.class, args);
    }

}


~~~

- 执行启动类main方法启动项目，访问地址：127.0.0.1:8080/token/login?name=tom&password=123456