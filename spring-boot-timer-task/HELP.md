### 快速实现定时任务

##### 示例

第一步：创建starter工程spring-boot-timer-task并配置pom.xml文件

~~~xml

<dependencies>
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>

</dependencies>

~~~

第二步：配置文件

- 可选

~~~yml
spring:
  task:
    scheduling:
      pool:
        size: 20
      thread-name-prefix: Job-Thread-
~~~

第三步: 开启定时任务

```java

@SpringBootApplication
@EnableScheduling
public class SpringBootTimerTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTimerTaskApplication.class, args);
    }

}
```

第四步:测试

```java
package com.example.job;


/**
 * @Author: wgs
 * @Date 2022/5/30 10:03
 * @Classname TaskJob
 * @Description
 */
@Component
@Slf4j
public class MyJob {
    @Scheduled(cron = "0/5 * * * * ?")
    public void process1() {
        log.info("【process1】开始执行:{}", DateUtil.now());
    }

    //initialDelay 延迟启动
    //fixedDelay 固定延迟，时间间隔是前次任务的结束到下次任务的开始
    @Scheduled(fixedDelay = 3000, initialDelay = 5000)
    public void process2() {
        log.info("【process2】开始执行:{}", DateUtil.now());
        ThreadUtil.sleep(2000);
        log.info("【process2】结束执行:{}", DateUtil.now());
    }

    //initialDelay 延迟启动
    //fixedRate 时间间隔是前次任务和下次任务的开始
    @Scheduled(fixedRate = 3000, initialDelay = 5000)
    public void process3() {
        log.info("【process3】开始执行:{}", DateUtil.now());
        ThreadUtil.sleep(2000);
        log.info("【process3】结束执行:{}", DateUtil.now());
    }
}

```