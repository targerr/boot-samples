package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/display")
public class DisplayController {

    /**
     * 127.0.0.1:9527/api/display/controller?name=&requestId=
     * requestId 参数化并不好, 这样业务和日志耦合在一起
     * */
    @GetMapping("/controller")
    public void print(@RequestParam("name") String name,
                      @RequestParam(name = "requestId", required = false) String requestId) {

        log.info("coming in 9527 with args: [{}]", name);
    }
}
