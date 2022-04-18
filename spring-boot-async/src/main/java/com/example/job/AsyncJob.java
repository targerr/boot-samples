package com.example.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/4/18 15:16
 * @Classname AsyncJob
 * @Description
 */
@Component
@Slf4j
public class AsyncJob {

    @Async
    public void job() throws InterruptedException {
        doTask("job", 1);
    }

    /**
     * 模拟5秒的异步任务
     */
    @Async
    public Future<Boolean> job1() throws InterruptedException {
        doTask("job1", 5);
        return new AsyncResult<>(Boolean.TRUE);
    }

    /**
     * 模拟2秒的异步任务
     */
    @Async
    public Future<Boolean> job2() throws InterruptedException {
        doTask("job2", 2);
        return new AsyncResult<>(Boolean.TRUE);
    }

    /**
     * 模拟3秒的异步任务
     */
    @Async
    public Future<Boolean> job3() throws InterruptedException {
        doTask("job3", 3);
        return new AsyncResult<>(Boolean.TRUE);
    }

    /**
     * 模拟5秒的同步任务
     */
    public void task1() throws InterruptedException {
        doTask("task1", 5);
    }

    /**
     * 模拟2秒的同步任务
     */
    public void task2() throws InterruptedException {
        doTask("task2", 2);
    }

    /**
     * 模拟3秒的同步任务
     */
    public void task3() throws InterruptedException {
        doTask("task3", 3);
    }

    private void doTask(String taskName, Integer time) throws InterruptedException {
        Long startTime = System.currentTimeMillis();
        log.info("{}开始执行，当前线程名称【{}】", taskName, Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(time);
        log.info("{}执行成功，当前线程名称【{}】耗时 {} ms", taskName, Thread.currentThread().getName(), System.currentTimeMillis() - startTime);
    }
}
