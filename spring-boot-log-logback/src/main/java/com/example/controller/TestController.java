package com.example.controller;

import cn.hutool.core.lang.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/4/27 17:26
 * @Classname TestController
 * @Description
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @PostMapping("/get")
    public Dict get(@RequestBody Map<String, Object> map) {
        log.trace("trace...");
        log.debug("debug...");
        log.info("info...");
        log.warn("warn...");
        log.error("error...");

        return Dict.create().set("json", map);
    }

}
