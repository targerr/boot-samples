package com.example.controller;

import cn.hutool.core.lang.Dict;
import com.example.annotation.RateLimiter;
import com.example.enums.LimitType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RateLimiter(count = 2, time = 10, limitType = LimitType.IP)
    @GetMapping("/test")
    public Dict test() {
        return Dict.create().set("msg", "hello,world!").set("description", "别想一直看到我，不信你快速刷新看看~");
    }

    @RateLimiter(count = 2, time = 10)
    @GetMapping("/test1")
    public Dict test1() {
        return Dict.create().set("msg", "hello,world!").set("description", "别想一直看到我，不信你快速刷新看看~");
    }

}
