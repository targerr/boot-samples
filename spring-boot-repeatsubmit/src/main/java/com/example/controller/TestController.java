package com.example.controller;

import com.example.annotation.RepeatSubmit;
import jodd.util.ThreadUtil;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/7/6 16:10
 * @Classname TestController
 * @Description
 */
@RestController
@RequestMapping("/")
public class TestController {

    /**
     * 新增测试单表
     */
    @RepeatSubmit(interval = 2, timeUnit = TimeUnit.SECONDS)
    @GetMapping("/one")
    public String one() {
        ThreadUtil.sleep(2000);
       return "ok";
    }
}
