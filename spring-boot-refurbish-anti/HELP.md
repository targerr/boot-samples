### springboot整合 redis 防刷

第一步：spring-boot-refurbish-anti

~~~xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
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

第三步： AntiRefreshInterceptor拦截器

~~~java
package com.example.interceptor;


@Component
@Slf4j
public class AntiRefreshInterceptor implements HandlerInterceptor {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIP = ServletUtil.getClientIP(request);
        String userAgent = request.getHeader("User-Agent");
        String key = "anti:refresh:" + DigestUtil.md5Hex(clientIP + "_" + userAgent);

        response.setContentType("text/html;charset=utf-8");

        if (redisTemplate.hasKey("anti:refresh:blacklist")) {
            if (redisTemplate.opsForSet().isMember("anti:refresh:blacklist", clientIP)) {
                return fail("检测到您的IP访问异常，已被加入黑名单", response);
            }
        }


        Integer num = (Integer) redisTemplate.opsForValue().get(key);
        if (num == null) {
            //第一次访问
            redisTemplate.opsForValue().set(key, 1, 60, TimeUnit.SECONDS);
        } else {

            if (num > 10 && num < 100) {
                fail("请求过于频繁，请稍后再试!", response);
                redisTemplate.opsForValue().increment(key, 1);
                return false;
            } else if (num >= 100) {
                fail("检测到您的IP访问异常，已被加入黑名单!", response);
                redisTemplate.opsForSet().add("anti:refresh:blacklist", clientIP);
                return false;
            } else {
                redisTemplate.opsForValue().increment(key, 1);
            }
        }
        return true;
    }

    private boolean fail(String msg, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");

            JSONObject json = new JSONObject();
            json.put("code", 30000);
            json.put("msg", msg);

            response.getWriter().write(json.toJSONString());
        } catch (IOException e) {
            log.error("请求写出失败！");
        }
        return false;
    }
}

~~~

第五步：创建测试

```java
package com.example.controller;

@RestController
@RequestMapping("/")
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "ok";
    }
}


```
