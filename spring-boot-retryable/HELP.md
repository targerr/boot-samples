### 整合重试功能

##### 示例

第一步：创建starter工程spring-boot-retryable并配置pom.xml文件


~~~xml

<dependencies>
    <!--切面-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.retry</groupId>
        <artifactId>spring-retry</artifactId>
    </dependency>

</dependencies>

~~~

第二步：创建实现类

~~~java
package com.example.service.impl;

import com.example.service.RetryService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * @Author: wgs
 * @Date 2022/5/9 16:49
 * @Classname RetryServiceImpl
 * @Description
 */
@Service
public class RetryServiceImpl implements RetryService {
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    @Override
    public Integer check(Integer code) throws Exception {
        System.out.println("被调用,时间：" + LocalTime.now());
        if (code == 0) {
            throw new Exception("故意抛出异常！");
        }
        System.out.println("被调用,情况对头了！");

        return 200;
    }


    @Recover
    public Integer recover(Exception e, int code) {
        System.err.println("回调方法执行！！！！");
        //记日志到数据库 或者调用其余的方法
        return 400;
    }

    public Integer checkCall(Integer code) throws Exception {
        int retryCount = 0;
        for (int i = 0; i < 4; i++) {
            if (retryCount > 3) {
                System.out.println("重试次数达到上限!");
                throw new Exception("重试次数达到上限！");
            }
            if (code == 0) {
                retryCount++;
            } else {
                return 200;
            }

        }

        return 200;
    }
}

~~~

第三步：RetryController

~~~java

@RestController
@RequestMapping("/retry")
public class RetryController {
    @Autowired
    private RetryService retryService;

    @GetMapping("/info/{code}")
    public String info(@PathVariable("code") Integer code) throws Exception {
        retryService.check(code);

        return "ok";
    }
}


~~~

第五步：创建启动类SpringBootIdLeafApplication

- *EnableRetry开启注解*
~~~java
@SpringBootApplication
@EnableRetry
public class SpringBootRetryableApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRetryableApplication.class, args);
    }

}


~~~

执行启动类main方法， 号段:访问地址127.0.0.1:8080/retry/info/1 执行启动类main方法， 雪花:访问地址127.0.0.1:8080/segment

### [文档链接](https://blog.csdn.net/h254931252/article/details/109257998)
