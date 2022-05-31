### 快速实现定时任务

##### 示例

第一步：创建starter工程spring-boot-timer-quartz并配置pom.xml文件

~~~xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-quartz</artifactId>
    </dependency>
</dependencies>

~~~

第二步：配置文件

- 可选

~~~yml

~~~

第三步: 创建定时任务

```java
package com.example.job;

@Slf4j
public class MyQuartzJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String printTime = new SimpleDateFormat("yy-MM-dd HH-mm-ss").format(new Date());
        log.info("开始执行 : {} ~~~~~~~ ", printTime);
    }
}

```

第四步:配置

```java
package com.example.config;

import com.example.job.MyQuartzJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/5/30 16:05
 * @Classname MyQuartJobConfig
 * @Description
 */
@Configuration
public class MyQuartJobConfig {
    @Bean
    public JobDetail jobDetail() {
        JobDetail detail = JobBuilder.newJob(MyQuartzJob.class)
                .withIdentity("job1", "group1")
                .storeDurably()
                .build();
        return detail;
    }

    @Bean
    public Trigger trigger() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
        return trigger;
    }
}

```
