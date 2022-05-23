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
