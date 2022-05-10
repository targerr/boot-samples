package com.example.service.controller;

import com.example.service.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wgs
 * @Date 2022/5/9 16:53
 * @Classname RetryController
 * @Description
 */
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
