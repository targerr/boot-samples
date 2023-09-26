package com.example.controller;

import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <h1>日志链路追踪案例</h1>
 * */
@Slf4j
@RestController
@RequestMapping("/trace")
public class TraceRequestController {

    private final RestTemplate restTemplate;

    public TraceRequestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 127.0.0.1:8000/api/trace/request
     * */
    @GetMapping("/request")
    public User request() {

        String url = "http://127.0.0.1:9527/api/display/controller?name=qinyi";
        log.info("send request to 9527, status code is: [{}]",
                restTemplate.getForEntity(url, Void.class).getStatusCodeValue());
        return new User("qinyi", 19);
    }
}
