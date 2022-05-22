package com.example.controller;

import cn.hutool.core.lang.Dict;
import com.google.common.util.concurrent.RateLimiter;
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
public class IndexController {
    // 创建令牌桶每秒一个
    private RateLimiter rl = RateLimiter.create(1);

    @GetMapping("/rate")
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
}
