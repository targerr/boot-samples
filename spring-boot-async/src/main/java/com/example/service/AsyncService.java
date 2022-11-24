package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/11/17 09:43
 * @Classname AsyncService
 * @Description 异步服务
 */
@Service
@Slf4j
public class AsyncService {
    @Async
    public void asyncImportGoods(String taskId) {
        log.info("任务 Id: {}", taskId);
        execute();
    }

    private void execute() {
        final StopWatch stopWatch = DateUtil.createStopWatch();
        stopWatch.start("异步线程进入");
        ThreadUtil.sleep(1000L);
        stopWatch.stop();

        stopWatch.start("异步线程执行");
        log.info("线程名称: {}", Thread.currentThread().getName());
        ThreadUtil.sleep(1000L);
        stopWatch.stop();


        log.info("线程信息: {}", stopWatch.prettyPrint());
    }
}
