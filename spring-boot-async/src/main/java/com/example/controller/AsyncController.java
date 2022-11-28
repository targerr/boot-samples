package com.example.controller;

import com.example.job.AsyncJob;
import com.example.service.AsyncTaskManager;
import com.example.vo.AsyncTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/4/18 15:19
 * @Classname AsyncController
 * @Description
 */
@RestController
@RequestMapping("/async")
@Slf4j
public class AsyncController {
    @Autowired
    private AsyncJob asyncJob;
    @Autowired
    private AsyncTaskManager asyncTaskManager;

    @GetMapping("/job")
    public String task() throws InterruptedException {
        Long startTime = System.currentTimeMillis();

        // 执行异步任务
        asyncJob.job1();
        asyncJob.job2();
        asyncJob.job3();

        // 模拟业务耗时
        TimeUnit.SECONDS.sleep(2);

        long endTime = System.currentTimeMillis();
        log.info("主线程耗时 {} ms", endTime - startTime);

        return "success";
    }


    @GetMapping("/executeTask")
    public String executeTask() {

        AsyncTaskInfo asyncTaskInfo=  asyncTaskManager.submit();

        return asyncTaskInfo.getTaskId();
    }

    @GetMapping("/task-info")
    public String getTaskInfo(@RequestParam String taskId) {
        return asyncTaskManager.getTaskInfo(taskId).getStatus().getStateInfo();
    }
}
