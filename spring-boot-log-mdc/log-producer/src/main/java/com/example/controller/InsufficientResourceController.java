package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <h1>使用线程堆栈日志定位资源不足问题</h1>
 * */
@Slf4j
@RestController
@RequestMapping("/insufficient")
public class InsufficientResourceController {

    /** 自定义线程池, 最好使能够给线程有意义的名字 */
    private final ExecutorService es = Executors.newCachedThreadPool(
            new BasicThreadFactory.Builder().namingPattern("Imooc-Qinyi-%d").build()
    );

    private final StringRedisTemplate redisTemplate;

    public InsufficientResourceController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{batch}")
    public DeferredResult<String> resource(@PathVariable int batch) {

        DeferredResult<String> result = new DeferredResult<>(10 * 1000L,
                "timeout");
        CompletableFuture[] futures = new CompletableFuture[batch];

        for (int i = 0; i != batch; ++i) {
            futures[i] = CompletableFuture.supplyAsync(this::getValue, es);
        }

        CompletableFuture.allOf(futures).thenRun(() -> result.setResult("success"));

        return result;
    }

    private String getValue() {

        try {
            return redisTemplate.execute((RedisCallback<String>) connection -> {
                sleep(5000);
                return "qinyi-" + connection;
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "error";
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
