package com.example.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/5/30 10:03
 * @Classname TaskJob
 * @Description
 */
@Component
@Slf4j
public class MyJob {
    @Scheduled(cron = "0/5 * * * * ?")
    public void process1() {
        log.info("【process1】开始执行:{}", DateUtil.now());
    }

    //initialDelay 延迟启动
    //fixedDelay 固定延迟，时间间隔是前次任务的结束到下次任务的开始
    @Scheduled(fixedDelay = 3000, initialDelay = 5000)
    public void process2() {
        log.info("【process2】开始执行:{}", DateUtil.now());
        ThreadUtil.sleep(2000);
        log.info("【process2】结束执行:{}", DateUtil.now());
    }

    //initialDelay 延迟启动
    //fixedRate 时间间隔是前次任务和下次任务的开始
    @Scheduled(fixedRate = 3000, initialDelay = 5000)
    public void process3() {
        log.info("【process3】开始执行:{}", DateUtil.now());
        ThreadUtil.sleep(2000);
        log.info("【process3】结束执行:{}", DateUtil.now());
    }
}
