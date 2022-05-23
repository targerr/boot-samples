### springboot整合 guava 限流

#### 案例

```java
//create方法传入的是每秒生成令牌的个数
RateLimiter rateLimiter=RateLimiter.create(1);
        for(int i=0;i< 5;i++){
        //acquire方法传入的是需要的令牌个数，当令牌不足时会进行等待，该方法返回的是等待的时间
        double waitTime=rateLimiter.acquire(1);
        System.out.println(System.currentTimeMillis()/1000+" , "+waitTime);
        }
```

第一步：spring-boot-ratelimit-guava

~~~xml

<dependencies>
    <!-- 工具类大全 -->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
    <!-- google公司java工具包 -->
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
    </dependency>
    <!--切面-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
</dependencies>
~~~

第二步：创建application.yml文件

~~~yaml
server:
  port: 8080
~~~

第三步： 注解

~~~java
package com.example.annoation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/5/22 09:05
 * @Classname RateLimit
 * @Description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomRateLimiter {
    int NOT_LIMITED = 0;

    /**
     * qps
     */
    @AliasFor("qps") double value() default NOT_LIMITED;

    /**
     * qps
     */
    @AliasFor("value") double qps() default NOT_LIMITED;

    /**
     * 超时时长
     */
    int timeout() default 0;

    /**
     * 超时时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}

~~~

第四步：创建RateLimiterAspect

~~~java
package com.example.aspect;

import com.example.annoation.CustomRateLimiter;


/**
 * @Author: wgs
 * @Date 2022/5/22 14:04
 * @Classname RateLimiterAspect
 * @Description
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAspect {
    private static final ConcurrentHashMap<String, RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap();

    /**
     * 切点
     *
     * @param customRateLimiter
     */
    @Pointcut(value = "@annotation(customRateLimiter)")
    public void pointcut(CustomRateLimiter customRateLimiter) {
    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around(value = "pointcut(customRateLimiter)")
    public Object aroundLog(ProceedingJoinPoint point, CustomRateLimiter customRateLimiter) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        System.out.println(customRateLimiter);
        System.out.println(customRateLimiter.qps());


        if (customRateLimiter != null && customRateLimiter.qps() > CustomRateLimiter.NOT_LIMITED) {
            double qps = customRateLimiter.qps();
            // TODO 这个key可以根据具体需求配置,例如根据ip限制,或用户
            if (RATE_LIMITER_CACHE.get(method.getName()) == null) {
                // 初始化 QPS
                RATE_LIMITER_CACHE.put(method.getName(), RateLimiter.create(qps));
            }

            log.debug("【{}】的QPS设置为: {}", method.getName(), RATE_LIMITER_CACHE.get(method.getName()).getRate());
            // 尝试获取令牌
            if (RATE_LIMITER_CACHE.get(method.getName()) != null && !RATE_LIMITER_CACHE.get(method.getName()).tryAcquire(customRateLimiter.timeout(), customRateLimiter.timeUnit())) {
                throw new RateException(CommonEnum.ResultEnum.RATE_ERROR);
            }
        }
        return point.proceed();
    }
}

~~~

第五步：创建枚举

```java
package com.example.enums;

import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/5/22 14:38
 * @Classname CommonEnum
 * @Description
 */
public interface CommonEnum {
    @Getter
    enum ResultEnum implements Ienum<Integer> {

        /**
         * 成功
         */
        SUCCESS(0, "成功"),
        /**
         * 参数不正确
         */
        PARAM_ERROR(1, "参数不正确"),

        RATE_ERROR(10, "限流中"),
        ;
        private Integer code;
        private String message;

        ResultEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public boolean is(Integer code) {
            return get().equals(code);
        }

        @Override
        public Integer get() {
            return code;
        }
    }

    interface Ienum<T> {
        boolean is(T t);

        T get();
    }
}

```

第六步：创建异常

```java
package com.example.exception;

import com.example.enums.CommonEnum;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/5/22 14:45
 * @Classname RateException
 * @Description
 */
@Getter
public class RateException extends RuntimeException {
    private Integer code;

    public RateException(CommonEnum.ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public RateException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}


```

第七步：创建异常拦截

```java
package com.example.handle;

import cn.hutool.core.lang.Dict;
import com.example.exception.RateException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: wgs
 * @Date 2022/5/22 14:50
 * @Classname RateExcetionHandler
 * @Description
 */
@RestControllerAdvice
public class RateExceptionHandler {
    @ExceptionHandler(RateException.class)
    public Dict rateException(RateException rateException) {
        Dict dict = new Dict();
        dict.put("code", rateException.getCode());
        dict.put("msg", rateException.getMessage());
        return dict;

    }
}

```

第八步：创建测试

```java
package com.example.controller;

import cn.hutool.core.lang.Dict;
import com.example.annoation.CustomRateLimiter;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/5/22 09:11
 * @Classname IndexController
 * @Description
 */
@RestController
@RequestMapping("/limit")
@Slf4j
public class IndexController {
    // 创建令牌桶每秒一个
    private RateLimiter rl = RateLimiter.create(1);

    @GetMapping("/test1")
    public Dict rate() {
        //获取令牌，如果没有则等待至超时，本代码超时时间为0，立刻返回错误信息
        boolean flag = rl.tryAcquire(0, TimeUnit.SECONDS);
        Dict dict = new Dict();
        if (!flag) {
            dict.put("code", 777);
            dict.put("msg", "限流");
            return dict;
        }

        dict.put("code", 0);
        dict.put("msg", "成功");
        return dict;
    }

    @CustomRateLimiter(qps = 1, value = 1.0, timeout = 300)
    @GetMapping("/test2")
    public Dict test1() {
        return Dict.create().set("msg", "hello,world!").set("description", "别想一直看到我，不信你快速刷新看看~");
    }


    @CustomRateLimiter(value = 2.0, timeout = 300)
    @GetMapping("/test3")
    public Dict test3() {
        return Dict.create().set("msg", "hello,world!").set("description", "别想一直看到我，不信你快速刷新看看~");
    }
}

```

第九步：创建启动类SpringBootUiKnife4jApplication

~~~java

@SpringBootApplication
public class SpringBootRatelimitGuavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRatelimitGuavaApplication.class, args);
    }

}

~~~

执行启动类main方法启动项目，访问地址：http://localhost:8008/limit/test1

图示
![image.png](https://upload-images.jianshu.io/upload_images/4994935-c9da67f1d556bc27.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


> 通俗的理解就是，有一个固定大小的水桶，水龙头一直按照一定的频率往里面滴水。水满了，就不滴了。客户端每次进行请求之前，都要先尝试从水桶里面起码取出“一滴水”，才能处理业务。因为桶的大小固定，水龙头滴水频率固定。从而也就保证了数据接口的访问流量。

- [参考](https://blog.51cto.com/u_15067227/2603666)
- [参考](https://www.imooc.com/article/290964)