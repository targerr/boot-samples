package com.example.controller;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.example.annotation.BusinessLog;
import com.example.enums.CommonEnum;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/4/20 11:05
 * @Classname TestController
 * @Description
 */
@RestController
@RequestMapping("/aop")
public class TestController {
    @GetMapping("/test")
    @BusinessLog(behavior = CommonEnum.LoggerTypeEnum.SELECT)
    public String test(String result) {
        return result;
    }


    @PostMapping("/testJson")
    @BusinessLog(behavior = CommonEnum.LoggerTypeEnum.SELECT)
    public Dict testJson(@RequestBody Map<String, Object> map) {
        final String jsonStr = JSONUtil.toJsonStr(map);
        Console.error(jsonStr);
        return Dict.create().set("json", map);
    }
}
