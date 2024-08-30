#### 补充
### TransmittableThreadLocal和ThreadPoolTaskExecutor

1. pom依赖
```xml
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>transmittable-thread-local</artifactId>
      <version>2.14.2</version>
    </dependency>

```

2. 示例
```java
     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(8);
        executor.setCorePoolSize(3);
        executor.setThreadNamePrefix("prefix-");
        executor.setQueueCapacity(200);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 设置包装器
        executor.setTaskDecorator(TtlRunnable::get);
        executor.initialize();

```

> TTL的使用就是包装提交的Runnable或Callable或线程池，注意：包装Runnable或Callable时，每次提交都要重新包装，这样很麻烦，可以直接包装线程池，或者使用Spring提供的ThreadPoolTaskExecutor，该类会对提交的Runnable进行包装：


[转载](https://blog.csdn.net/weixin_41866717/article/details/130766802)