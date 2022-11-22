package com.example.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: wgs
 * @Date 2022/4/18 15:14
 * @Classname AsyncConfig
 * @Description 自定义异步任务线程池, 异步任务异常捕获处理器
 */
@EnableAsync// 开启 Spring 异步任务支持
@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 设置核心线程数
        executor.setCorePoolSize(2);
        // 设置最大线程数
        executor.setMaxPoolSize(2);
        // 线程池所使用的缓冲队列
        executor.setQueueCapacity(2);
        executor.setThreadNamePrefix("Qinyi-Async-");   // 这个非常重要

        // 等待所有任务结果候再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        // 定义拒绝策略
        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        // 初始化线程池, 初始化 core 线程
        executor.initialize();

        return executor;
    }

    /**
     * 知道系统中异步线程出现异常所用到的处理器
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    /**
     * <h2>异步任务异常捕获处理器</h2>
     */
    @SuppressWarnings("all")
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method,
                                            Object... objects) {

            throwable.printStackTrace();
            log.error("线程池错误: [{}], 方法: [{}], 参数: [{}]",
                    throwable.getMessage(), method.getName(),
                    JSON.toJSONString(objects));

            // TODO 发送邮件或者是短信, 做进一步的报警处理
        }
    }
}
