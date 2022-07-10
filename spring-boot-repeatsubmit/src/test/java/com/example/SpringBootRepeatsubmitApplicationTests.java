package com.example;

import cn.hutool.core.convert.Convert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class SpringBootRepeatsubmitApplicationTests {
    @Autowired
    private RedissonClient redissonClient;

    @Test
   public void contextLoads() {
        String cacheRepeatKey = "121341431";
        String key = Convert.toStr(redissonClient.getBucket(cacheRepeatKey).get());
        if (key == null) {
            final RBucket<String> result = redissonClient.getBucket(cacheRepeatKey);
            result.set("");
            result.expire(Duration.ofMillis(20000));
        }
    }

}
