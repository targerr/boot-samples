package com.example.config;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2023/7/4 17:57
 * @Classname CacheConfig
 * @Description
 */
@Configuration
@Slf4j
public class CacheConfig {
    @Bean("smsCache")
    public Cache<String, String> cache(){

        return CacheBuilder.newBuilder()
                //设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(5)
                //设置写缓存后5分钟过期
                .expireAfterWrite(5, TimeUnit.MINUTES)
                //设置缓存容器的初始容量为10
                .initialCapacity(10)
                //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
                //设置要统计缓存的命中率
                .recordStats()
                //设置缓存的移除通知 （注意：GuavaCache 并不保证在过期时间到了之后立刻删除该 Key，如果你此时去访问了这个 Key，它会检测是不是已经过期，过期就删除它，所以过期时间到了之后你去访问这个 Key 会显示这个 Key 已经被删除，但是如果你不做任何操作，那么在 10 min 到了之后也许这个 Key 还在内存中。GuavaCache 选择这样做的原）
                .removalListener(notification -> log.info("当前值已过期:" + JSON.toJSONString(notification))).build();
    }

}
