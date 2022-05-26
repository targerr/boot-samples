### springboot整合 dynamic

#### 案例

第一步：spring-boot-async-dynamic

~~~xml

<dependencies>
    <!--日志-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-logging</artifactId>
    </dependency>

    <!--动态线程池引入-->
    <dependency>
        <groupId>com.alibaba.boot</groupId>
        <artifactId>nacos-config-spring-boot-starter</artifactId>
        <version>0.2.7</version>
    </dependency>

    <!--nacos-->
    <dependency>
        <groupId>io.github.lyh200</groupId>
        <artifactId>dynamic-tp-spring-boot-starter-nacos</artifactId>
        <version>1.0.5</version>
    </dependency>

</dependencies>

~~~

第二步：创建application.yml文件

~~~yaml
nacos:
  config:
    server-addr: 172.16.200.249:8848
    type: yaml
    data-ids: dynamic-tp-nacos.yml
    auto-refresh: true
    group: DEFAULT_GROUP
    bootstrap:
      enable: true
      log-enable: true


logging:
  level:
    #root: DEBUG
    com.alibaba.nacos.client.config.impl: WARN

~~~

第三步： dynamic-tp-nacos.yml配置文件

```yaml
# 动态线程池配置文件，建议单独开一个文件放到配置中心，字段详解看readme介绍
spring:
  dynamic:
    tp:
      enabled: true
      enabledBanner: true        # 是否开启banner打印，默认true
      enabledCollect: false      # 是否开启监控指标采集，默认false
      collectorType: logging     # 监控数据采集器类型（JsonLog | MicroMeter），默认logging
      logPath: /home/logs        # 监控日志数据路径，默认 ${user.home}/logs
      monitorInterval: 5         # 监控时间间隔（报警判断、指标采集），默认5s
      nacos:
        dataId: dynamic-tp-nacos-demo-dtp-dev.yml
        group: DEFAULT_GROUP
      configType: yml
      platforms: # 通知报警平台配置
        - platform: wechat
          urlKey: 3a7500-1287-4bd-a798-c5c3d8b69c  # 替换
          receivers: test1,test2                   # 接受人企微名称
        - platform: lark
          urlKey: 0d944ae7-b24a-4059-a49e-eca623e8fbc7     # 替换
          receivers: test1,test2                           # 接受人飞书名称/openid
        - platform: ding
          urlKey: f80dad441fcd655438f4a08dcd6a     # 替换
          secret: SECb5441fa6f375d5b9d21           # 替换，非sign模式可以没有此值
          receivers: 15810119805                   # 钉钉账号手机号
      tomcatTp: # tomcat web server线程池配置
        minSpare: 100
        max: 400
      jettyTp: # jetty web server线程池配置
        min: 100
        max: 400
      undertowTp: # undertow web server线程池配置
        coreWorkerThreads: 100                     # 核心线程数
        maxWorkerThreads: 400                      # 最大线程数
        workerKeepAlive: 40
      executors: # 动态线程池配置，都有默认值，采用默认值的可以不配置该项，减少配置量
        - threadPoolName: dtpExecutor1
          executorType: common                     # 线程池类型common、eager：适用于io密集型
          corePoolSize: 6
          maximumPoolSize: 8
          queueCapacity: 200
          queueType: VariableLinkedBlockingQueue   # 任务队列，查看源码QueueTypeEnum枚举类
          rejectedHandlerType: CallerRunsPolicy    # 拒绝策略，查看RejectedTypeEnum枚举类
          keepAliveTime: 50
          allowCoreThreadTimeOut: false
          threadNamePrefix: test                         # 线程名前缀
          waitForTasksToCompleteOnShutdown: false        # 参考spring线程池设计
          awaitTerminationSeconds: 5                     # 单位（s）
          preStartAllCoreThreads: false                  # 是否预热核心线程，默认false
          runTimeout: 200                                # 任务执行超时阈值，目前只做告警用，单位（ms）
          queueTimeout: 100                              # 任务在队列等待超时阈值，目前只做告警用，单位（ms）
          taskWrapperNames: [ "ttl" ]                      # 任务包装器名称，集成TaskWrapper接口

        - threadPoolName: dtpExecutor2
          executorType: eager                          # 线程池类型common、eager：适用于io密集型
          corePoolSize: 2
          maximumPoolSize: 4
          queueCapacity: 200
          queueType: VariableLinkedBlockingQueue       # 任务队列，查看源码QueueTypeEnum枚举类
          rejectedHandlerType: CallerRunsPolicy        # 拒绝策略，查看RejectedTypeEnum枚举类
          keepAliveTime: 50
          allowCoreThreadTimeOut: false
          threadNamePrefix: test                         # 线程名前缀
          waitForTasksToCompleteOnShutdown: false        # 参考spring线程池设计
          awaitTerminationSeconds: 5                     # 单位（s）
          preStartAllCoreThreads: false                  # 是否预热核心线程，默认false
          runTimeout: 200                                # 任务执行超时阈值，目前只做告警用，单位（ms）
          queueTimeout: 100                              # 任务在队列等待超时阈值，目前只做告警用，单位（ms）
          taskWrapperNames: [ "ttl" ]                      # 任务包装器名称，集成TaskWrapper接口
          notifyItems: # 报警项，不配置自动会按默认值配置（变更通知、容量报警、活性报警、拒绝报警、任务超时报警）
            - type: capacity               # 报警项类型，查看源码 NotifyTypeEnum枚举类
              enabled: true
              threshold: 80                # 报警阈值
              platforms: [ ding,wechat ]     # 可选配置，不配置默认拿上层platforms配置的所以平台
              interval: 120                # 报警间隔（单位：s）
            - type: change
              enabled: true
            - type: liveness
              enabled: true
              threshold: 80
            - type: reject
              enabled: true
              threshold: 1
            - type: run_timeout
              enabled: true
              threshold: 1
            - type: queue_timeout
              enabled: true
              threshold: 1
```

第四步： ThreadPoolConfiguration配置类

~~~java
package com.example.config;



/**
 * @Author: wgs
 * @Date 2022/5/25 14:27
 * @Classname ThreadPoolConfiguration
 * @Description
 */
/**
 * @author Redick01
 */
@Configuration
public class ThreadPoolConfiguration {

    /**
     * 通过{@link DynamicTp} 注解定义普通juc线程池，会享受到该框架监控功能，注解名称优先级高于方法名
     *
     * @return 线程池实例
     */
    @DynamicTp("commonExecutor")
    @Bean
    public ThreadPoolExecutor commonExecutor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    }

    /**
     * 通过{@link ThreadPoolCreator} 快速创建一些简单配置的动态线程池
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     *
     * @return 线程池实例
     */
    @Bean
    public DtpExecutor dtpExecutor1() {
        return ThreadPoolCreator.createDynamicFast("dtpExecutor1");
    }

    /**
     * 通过{@link ThreadPoolBuilder} 设置详细参数创建动态线程池（推荐方式），
     * ioIntensive，参考tomcat线程池设计，实现了处理io密集型任务的线程池，具体参数可以看代码注释
     *
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     * @return 线程池实例
     */
    @Bean
    public DtpExecutor ioIntensiveExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("ioIntensiveExecutor")
                .corePoolSize(20)
                .maximumPoolSize(50)
                .queueCapacity(2048)
                .ioIntensive(true)
                .buildDynamic();
    }

    /**
     * tips: 建议直接在配置中心配置就行，不用@Bean声明
     * @return 线程池实例
     */
    @Bean
    public ThreadPoolExecutor dtpExecutor2() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName("dtpExecutor2")
                .corePoolSize(10)
                .maximumPoolSize(15)
                .keepAliveTime(15000)
                .timeUnit(TimeUnit.MILLISECONDS)
                .workQueue(QueueTypeEnum.SYNCHRONOUS_QUEUE.getName(), null, false)
                .waitForTasksToCompleteOnShutdown(true)
                .awaitTerminationSeconds(5)
                .buildDynamic();
    }
}

~~~

第五步：硬代码配置方式类 SupportThreadPoolConfig

~~~java
package com.example.config;



/**
 * @Author: wgs
 * @Date 2022/5/25 14:44
 * @Classname SupportThreadPoolConfig
 * @Description
 */
public class SupportThreadPoolConfig {

    /**
     * 接收到xxl-job请求的线程池名
     */
    public static final String EXECUTE_XXL_THREAD_POOL_NAME = "execute-xxl-thread-pool";

    /**
     * 业务：实现pending队列的单线程池
     * 配置：核心线程可以被回收，当线程池无被引用且无核心线程数，应当被回收
     * 动态线程池且被Spring管理：false
     */
    public static ExecutorService getPendingSingleThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(ThreadPoolConstant.SINGLE_CORE_POOL_SIZE)
                .setMaxPoolSize(ThreadPoolConstant.SINGLE_MAX_POOL_SIZE)
                .setWorkQueue(ThreadPoolConstant.BIG_BLOCKING_QUEUE)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setAllowCoreThreadTimeOut(true)
                .setKeepAliveTime(ThreadPoolConstant.SMALL_KEEP_LIVE_TIME, TimeUnit.SECONDS)
                .build();
    }

    public static DtpExecutor getXxlCronExecutor() {
        return ThreadPoolBuilder.newBuilder()
                .threadPoolName(EXECUTE_XXL_THREAD_POOL_NAME)
                .corePoolSize(ThreadPoolConstant.COMMON_CORE_POOL_SIZE)
                .maximumPoolSize(ThreadPoolConstant.COMMON_MAX_POOL_SIZE)
                .keepAliveTime(ThreadPoolConstant.COMMON_KEEP_LIVE_TIME)
                .timeUnit(TimeUnit.SECONDS)
                .rejectedExecutionHandler(RejectedTypeEnum.CALLER_RUNS_POLICY.getName())
                .allowCoreThreadTimeOut(false)
                .workQueue(QueueTypeEnum.VARIABLE_LINKED_BLOCKING_QUEUE.getName(), ThreadPoolConstant.COMMON_QUEUE_SIZE, false)
                .buildDynamic();
    }

}

~~~

第六步：常量类
```java
package com.example.constant;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: wgs
 * @Date 2022/5/25 14:45
 * @Classname ThreadPoolConstant
 * @Description
 */
public interface ThreadPoolConstant {
    /**
     * small
     */
    public static final Integer SINGLE_CORE_POOL_SIZE = 1;
    public static final Integer SINGLE_MAX_POOL_SIZE = 1;
    public static final Integer SMALL_KEEP_LIVE_TIME = 10;

    /**
     * medium
     */
    public static final Integer COMMON_CORE_POOL_SIZE = 2;
    public static final Integer COMMON_MAX_POOL_SIZE = 2;
    public static final Integer COMMON_KEEP_LIVE_TIME = 60;
    public static final Integer COMMON_QUEUE_SIZE = 128;


    /**
     * big
     */
    public static final Integer BIG_QUEUE_SIZE = 1024;
    public static final BlockingQueue BIG_BLOCKING_QUEUE = new LinkedBlockingQueue(BIG_QUEUE_SIZE);
}

```
第七步：创建TestController

~~~java
package com.example.controller;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import com.example.config.SupportThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: wgs
 * @Date 2022/5/25 14:30
 * @Classname TestController
 * @Description
 */
@Slf4j
@RestController
public class TestController {
    @Resource
    private ThreadPoolExecutor dtpExecutor1;

    @GetMapping("/dtp-nacos-example/test")
    public String test() {
        new Thread(() -> {
            try {
                task();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return "success";
    }

    public void task() throws InterruptedException {
        DtpExecutor dtpExecutor2 = DtpRegistry.getDtpExecutor("dtpExecutor2");
        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
            dtpExecutor1.execute(() -> {
                log.info("i am dynamic-tp-test-1 task");
                System.err.println("task1: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());
            });
            dtpExecutor2.execute(() -> {
                log.info("i am dynamic-tp-test-2 task");
                System.err.println("task2: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());

            });

            ExecutorService executorService = SupportThreadPoolConfig.getPendingSingleThreadPool();
            executorService.execute(() -> {
                log.info("i am dynamic-tp-test-3 task");
                System.err.println("task3: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());
            });
            executorService.shutdown();


            ExecutorService executorService1 = SupportThreadPoolConfig.getXxlCronExecutor();
            executorService1.execute(() -> {
                log.info("i am dynamic-tp-test-4 task");
                System.err.println("task4: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());


            });

        }
    }
}

~~~

第八步：创建启动类SpringBootAsyncDynamicApplication
- 开启注解 @EnableDynamicTp
~~~java

@SpringBootApplication
@EnableDynamicTp
public class SpringBootAsyncDynamicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAsyncDynamicApplication.class, args);
    }

}


~~~

- [参考](https://github.com/dromara/dynamic-tp)