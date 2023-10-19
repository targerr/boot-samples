package com.example.aop;

import com.example.enums.AsyncTaskStatusEnum;
import com.example.service.async.AsyncTaskManager;
import com.example.vo.AsyncTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: wgs
 * @Date 2022/11/18 15:23
 * @Classname AsyncTaskMonitor
 * @Description 异步任务监控切面
 */
@Component
@Aspect
@Slf4j
public class AsyncTaskMonitor {

    /** 注入异步任务管理器 */
    private final AsyncTaskManager asyncTaskManager;

    public AsyncTaskMonitor(AsyncTaskManager asyncTaskManager) {
        this.asyncTaskManager = asyncTaskManager;
    }

    /**
     * 异步任务环绕切面
     * 环绕切面可以让我们在方法执行前后做'额外'处理
     */
    @Around("execution(* com.example.service.async.AsyncServiceImpl.*(..))")
    public Object taskHandle(ProceedingJoinPoint proceedingJoinPoint) {

        // 任务 ID,调用异步任务传入的第二个参数
        String taskId = proceedingJoinPoint.getArgs()[1].toString();
        // 获取异步信息详情
        AsyncTaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);

        // 设置运行状态并放入容器中
        taskInfo.setStatus(AsyncTaskStatusEnum.RUNNING);
        asyncTaskManager.setTaskInfo(taskInfo);

        AsyncTaskStatusEnum status;
        Object result;

        try {
            // 异步任务执行
            result = proceedingJoinPoint.proceed();
            status = AsyncTaskStatusEnum.SUCCESS;
            taskInfo.setStatus(AsyncTaskStatusEnum.SUCCESS);
        } catch (Throwable ex) {
            // 异步任务出现了异常
            result = null;
            status = AsyncTaskStatusEnum.FAILED;
            log.error("异步任务切面异常: 任务 ID [{}] 错误信息: [{}]",
                    taskId, ex.getMessage(), ex);
        }
        // 设置异步任务其他的信息, 再次重新放入到容器中
        taskInfo.setEndTime(new Date());
        taskInfo.setStatus(status);
        taskInfo.setTotalTime(String.valueOf(
                taskInfo.getEndTime().getTime() - taskInfo.getStartTime().getTime()
        ));
        asyncTaskManager.setTaskInfo(taskInfo);

        return result;
    }
}
