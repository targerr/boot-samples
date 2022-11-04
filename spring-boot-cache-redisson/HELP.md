# spring-boot 整合 redission

## 第一步： 创建starter工程spring-boot-cache-redission xml文件

```xml
<dependencies>
    <!--redisson依赖-->
    <dependency>
        <groupId>org.redisson</groupId>
        <artifactId>redisson</artifactId>
        <version>3.17.4</version>
    </dependency>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
</dependencies>
```

## 第二步：配置文件
```yaml
redis:
  hostName: 127.0.0.1
  port: 6379
  database: 0
  password:
```

## 第三步：配置类
```java
package com.example.config;

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

```

## 第四步：工具类 LockUtil
```java
package com.example.redisson;

import java.util.concurrent.TimeUnit;

/**
 * @explain redis分布式锁工具类
 */
public class LockUtil {

    private static Locker locker;

    /**
     * 设置工具类使用的locker
     *
     * @param locker
     */
    public static void setLocker(Locker locker) {
        LockUtil.locker = locker;
    }

    /**
     * 获取锁
     *
     * @param lockKey
     */
    public static void lock(String lockKey) {
        locker.lock(lockKey);
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        locker.unlock(lockKey);
    }

    /**
     * 获取锁，超时释放
     *
     * @param lockKey
     * @param timeout
     */
    public static void lock(String lockKey, int timeout) {
        locker.lock(lockKey, timeout);
    }

    /**
     * 获取锁，超时释放，指定时间单位
     *
     * @param lockKey
     * @param unit
     * @param timeout
     */
    public static void lock(String lockKey, TimeUnit unit, int timeout) {
        locker.lock(lockKey, unit, timeout);
    }

    /**
     * 尝试获取锁，获取到立即返回true,获取失败立即返回false
     *
     * @param lockKey
     * @return
     */
    public static boolean tryLock(String lockKey) {
        return locker.tryLock(lockKey);
    }

    /**
     * 尝试获取锁，在给定的waitTime时间内尝试，获取到返回true,获取失败返回false,获取到后再给定的leaseTime时间超时释放
     *
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public static boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        return locker.tryLock(lockKey, waitTime, leaseTime, unit);
    }

    /**
     * 锁释放被任意一个线程持有
     *
     * @param lockKey
     * @return
     */
    public static boolean isLocked(String lockKey) {
        return locker.isLocked(lockKey);
    }
}

```

## 第五步：测试类
```java
package com.example;

@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootCacheRedissonApplication.class)
public class RedissionTest {
    
    public static final String KEY = "TESTLOCK_1";

    @Test
    public void redisUtilTest() {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(this::test3);
            threadList.add(thread);
        }
        // 启动十个线程
        for (Thread thread : threadList) {
            thread.start();
        }

        // 多线程测试redisson实现分布式锁出现org.redisson.RedissonShutdownException: Redisson is shutdown。
        // 原因：多线程还没跑完，主线程就跑完了。主线程走完，关闭了资源。redisson关闭，多线程操作redisson报错：Redisson is shutdown。
        ThreadUtil.sleep(5000);
    }

    public void test() {
        for (int i = 0; i < 10; i++) {
            System.out.println("执行后续代码。线程 ID：" + Thread.currentThread().getId() + " >> " + i);
            ThreadUtil.sleep(1000);
        }
    }

    // 要锁的方法
    public void test1() {
        try {
            LockUtil.lock(KEY, TimeUnit.SECONDS, 1);
            for (int i = 0; i < 10; i++) {
                System.out.println("执行后续代码。线程 ID：" + Thread.currentThread().getId() + " >> " + i);
                ThreadUtil.sleep(1000);
            }
        } finally {
            LockUtil.unlock(KEY);
        }

    }

    // 要锁的方法
    public void test2() {
        // 尝试加锁
        boolean lock = LockUtil.tryLock(KEY);
        if (!lock) {
            System.out.println("太火爆了。线程 ID：" + Thread.currentThread().getId());
            return;
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("执行后续代码。线程 ID：" + Thread.currentThread().getId() + " >> " + i);
            ThreadUtil.sleep(500);
        }

    }

    // 要锁的方法
    public void test3() {
        // 尝试加锁
        boolean lock = false;
        try {
            lock = LockUtil.tryLock(KEY, 0, 3, TimeUnit.SECONDS);

            if (!lock) {
                System.out.println("太火爆了。线程 ID：" + Thread.currentThread().getId());
                return;
            }

            for (int i = 0; i < 10; i++) {
                System.out.println("执行后续代码。线程 ID：" + Thread.currentThread().getId() + " >> " + i);
                ThreadUtil.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```

[参考](https://my.oschina.net/u/4499317/blog/5058309?_from=gitee_search)
[面试题](https://mp.weixin.qq.com/s?__biz=Mzg3NjU3NTkwMQ==&mid=2247505097&idx=1&sn=5c03cb769c4458350f4d4a321ad51f5a&source=41#wechat_redirect)