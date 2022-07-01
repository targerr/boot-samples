package com.example.config;

import com.example.redisson.LockUtil;
import com.example.redisson.RedissonLocker;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/6/30 09:29
 * @Classname MyRedissonConfig
 * @Description
 */
@Configuration
public class MyRedissonConfig {

    /**
     * redis
     */
    @Value("${redis.hostName}")
    private String redisHostName;
    @Value("${redis.port}")
    private Integer redisPort;
    @Value("${redis.database}")
    private Integer redisDatabase;
    @Value("${redis.password}")
    private String redisPassword;

    /**
     * RedissonClient,分布式锁配置
     *
     * @return
     */
    @Bean
    public RedissonClient redisson() {
        // 1.创建配置
        Config config = new Config();
        // 集群模式
        // config.useClusterServers().addNodeAddress("127.0.0.1:7004", "127.0.0.1:7001");

        // 2.根据 Config 创建出 RedissonClient 示例。
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress("redis://" + redisHostName + ":" + redisPort)
                .setDatabase(redisDatabase);
        //有密码
        if (redisPassword != null && !"".equals(redisPassword)) {
            singleServerConfig.setPassword(redisPassword);
        }
        return Redisson.create(config);
    }

    /**
     * 分布式锁初始化
     *
     * @param redissonClient
     * @return
     */
    @Bean
    public RedissonLocker redissonLocker(RedissonClient redissonClient) {
        RedissonLocker locker = new RedissonLocker(redissonClient);
        //设置LockUtil的锁处理对象
        LockUtil.setLocker(locker);
        return locker;
    }
}
