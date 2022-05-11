package com.example;

import com.alibaba.fastjson.JSONObject;
import com.example.pojo.User;
import com.example.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;

/**
 * @Author: wgs
 * @Date 2022/5/11 10:55
 * @Classname RedisTest
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootCacheRedisApplication.class)
public class RedisTest {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    @Test
    public void redisUtilTest() {
        String key = "user-1";
        redisUtil.set(key, JSONObject.toJSONString(new User("张三", 22)));

    }

    @Test
    public void redisCacheTest() {
        String key = "user-2";
        redisCacheTemplate.opsForValue().set(key, new User("张三", 22));

        User user = (User) redisCacheTemplate.opsForValue().get(key);
        System.out.println(JSONObject.toJSONString(user));
    }
}
