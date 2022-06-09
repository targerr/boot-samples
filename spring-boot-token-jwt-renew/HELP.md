### 整合JWT

##### 示例

第一步：创建starter工程spring-boot-token-jwt-renew并配置pom.xml文件

~~~xml

<dependencies>
    <!--redis-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <!--jwt依赖-->
    <dependency>
        <groupId>com.auth0</groupId>
        <artifactId>java-jwt</artifactId>
        <version>3.10.3</version>
    </dependency>
</dependencies>
~~~

第二步：配置文件

```yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
```

第三步: jwt工具类

```java
package com.example.utils;


@Slf4j
public class JWTUtil {
    //私钥
    private static final String TOKEN_SECRET = "123456";

    /**
     * 生成token，自定义过期时间 毫秒
     *
     * @param userTokenDTO
     * @return
     */
    public static String generateToken(UserTokenDTO userTokenDTO) {
        try {
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");

            return JWT.create()
                    .withHeader(header)
                    .withClaim("token", JSONObject.toJSONString(userTokenDTO))
                    //.withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("generate token occur error, error is:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 检验token是否正确
     *
     * @param token
     * @return
     */
    public static UserTokenDTO parseToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        String tokenInfo = jwt.getClaim("token").asString();
        return JSON.parseObject(tokenInfo, UserTokenDTO.class);
    }
}

```

**说明：**

- 生成的token中不带有过期时间，token的过期时间由redis进行管理
- UserTokenDTO中不带有敏感信息，如password字段不会出现在token中

第四步: 拦截器

```java
package com.example.interceptor;

import com.example.entity.dto.UserTokenDTO;
import com.example.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wgs
 * @Date 2022/6/6 08:56
 * @Classname LongInterceptor
 * @Description
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    public static final int expireTime = 1 * 60 * 30;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authToken = request.getHeader("Authorization");
        String token = authToken.substring("Bearer".length() + 1).trim();
        UserTokenDTO userTokenDTO = JWTUtil.parseToken(token);
        //1.判断请求是否有效

        String cacheToken = stringRedisTemplate.opsForValue().get(userTokenDTO.getId());
        if (StringUtils.isEmpty(cacheToken) || !cacheToken.equals(token)) {
            return false;
        }
        //2.判断是否需要续期
        if (stringRedisTemplate.getExpire(userTokenDTO.getId()) < expireTime) {
            stringRedisTemplate.opsForValue().set(userTokenDTO.getId(), token);
            log.error("update token info, id is:{}, user info is:{}", userTokenDTO.getId(), token);
        }
        return true;
    }

}


```

第五步: controller

```java

package com.example.entity.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.dto.UserTokenDTO;
import com.example.entity.vo.LoginUserVO;
import com.example.entity.vo.UpdatePasswordUserVO;
import com.example.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/6/9 09:06
 * @Classname UserController
 * @Description
 */
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    // 过期时间
    private final Long DURATION = 1 * 24 * 60 * 60 * 1000L;


    @PostMapping("/login")
    public String login(LoginUserVO loginUserVO) {
        //1.判断用户名密码是否正确 db操作
        // 查询用户信息，比对密码

        //2.用户名密码正确生成token
        String userId = "1234";
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setId(userId);
        userTokenDTO.setGmtCreate(System.currentTimeMillis());
        String token = JWTUtil.generateToken(userTokenDTO);

        //3.存入token至redis
        redisTemplate.opsForValue().set(userId, JSONObject.toJSONString(userTokenDTO), DURATION, TimeUnit.MILLISECONDS);
        return token;
    }

    @PostMapping("/loginOut")
    public boolean loginOut(@RequestParam("id") String id) {
        return Boolean.TRUE.equals(redisTemplate.delete(id));
    }

    @PostMapping("/updatePassword")
    public String updatePassword(UpdatePasswordUserVO updatePasswordUserVO) {
        //1.修改密码 DB操作
        String userId = "66666";
        String userName = "tom";
        //2.生成新的token
        UserTokenDTO userTokenDTO = UserTokenDTO.builder()
                .id(userId)
                .userName(userName)
                .gmtCreate(System.currentTimeMillis()).build();
        String token = JWTUtil.generateToken(userTokenDTO);
        //3.更新token
        redisTemplate.opsForValue().set(userId, JSONObject.toJSONString(userTokenDTO), DURATION, TimeUnit.MILLISECONDS);
        return token;
    }

}

```

[参考](https://mp.weixin.qq.com/s/N_q5A8LlgkqWNArCICSztQ)