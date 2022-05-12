### 整合JWT

##### 示例

第一步：创建starter工程spring-boot-token-jwt并配置pom.xml文件

~~~xml

<dependencies>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>

</dependencies>

~~~

第二步：测试

~~~java
package com.example;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.User;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/5/12 14:37
 * @Classname JwtTest
 * @Description
 */
public class JwtTest {
    /**
     * 默认30分钟
     */
    private static final long EXPIRE_TIME = 30 * 60 * 1000;
    /**
     * 密钥 -- 根据实际项目，这里可以做成配置
     */
    public static final String KEY = "19921210";

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("user", new User(1, "tom"));
                put("expire_time", EXPIRE_TIME);
            }
        };
        final String token = JWTUtil.createToken(map, KEY.getBytes(StandardCharsets.UTF_8));
        System.out.println(token);

        boolean verify = JWTUtil.verify(token, KEY.getBytes(StandardCharsets.UTF_8));
        Assert.assertTrue(verify);

        JWT user = JWTUtil.parseToken(token);
        System.out.println(JSONObject.toJSONString(user));
        System.out.println(JSONObject.toJSONString(user.getPayload("user")));
    }
}


~~~
