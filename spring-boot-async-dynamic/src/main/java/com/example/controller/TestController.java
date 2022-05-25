package com.example.controller;

import com.dtp.core.DtpRegistry;
import com.dtp.core.thread.DtpExecutor;
import com.example.config.SupportThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: wgs
 * @Date 2022/5/25 14:30
 * @Classname TestController
 * @Description
 */
@Slf4j
@RestController
public class TestController {
    @Resource
    private ThreadPoolExecutor dtpExecutor1;

    @GetMapping("/dtp-nacos-example/test")
    public String test() {
        new Thread(() -> {
            try {
                task();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return "success";
    }

    public void task() throws InterruptedException {
        DtpExecutor dtpExecutor2 = DtpRegistry.getDtpExecutor("dtpExecutor2");
        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
            dtpExecutor1.execute(() -> {
                log.info("i am dynamic-tp-test-1 task");
                System.err.println("task1: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());
            });
            dtpExecutor2.execute(() -> {
                log.info("i am dynamic-tp-test-2 task");
                System.err.println("task2: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());

            });

            ExecutorService executorService = SupportThreadPoolConfig.getPendingSingleThreadPool();
            executorService.execute(() -> {
                log.info("i am dynamic-tp-test-3 task");
                System.err.println("task3: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());
            });
            executorService.shutdown();


            ExecutorService executorService1 = SupportThreadPoolConfig.getXxlCronExecutor();
            executorService1.execute(() -> {
                log.info("i am dynamic-tp-test-4 task");
                System.err.println("task4: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());


            });

        }
    }
}
