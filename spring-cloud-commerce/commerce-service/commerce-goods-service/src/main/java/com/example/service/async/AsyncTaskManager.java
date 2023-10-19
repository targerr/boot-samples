package com.example.service.async;

import cn.hutool.core.util.IdUtil;
import com.example.enums.AsyncTaskStatusEnum;
import com.example.goods.GoodsInfo;
import com.example.service.AsyncService;
import com.example.vo.AsyncTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/11/18 14:40
 * @Classname AsyncTaskManager
 * @Description 异步任务管理类
 * 对异步任务进行包装管理，记录并塞入记录信息
 */
@Slf4j
@Component
public class AsyncTaskManager {

    private final AsyncService asyncService;

    public AsyncTaskManager(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    // 记录异步任务容器
    private static final Map<String, AsyncTaskInfo> taskContainer = new HashMap<>(2);

    public AsyncTaskInfo initAsyncTask() {

        AsyncTaskInfo info = new AsyncTaskInfo();
        // 设置任务唯一 ID
        info.setTaskId(IdUtil.randomUUID());
        info.setStatus(AsyncTaskStatusEnum.STARTED);
        info.setStartTime(new Date());

        // 初始化容器信息放入容器
        taskContainer.put(info.getTaskId(), info);

        return info;
    }

    /**
     * 提交任务信息
     *
     * @param goodsInfos
     * @return
     */
    public AsyncTaskInfo submitTask(List<GoodsInfo> goodsInfos) {
        // 初始化一个异步任务的监控信息
        AsyncTaskInfo asyncTaskInfo = initAsyncTask();

        asyncService.asyncImportGoods(goodsInfos, asyncTaskInfo.getTaskId());

        return asyncTaskInfo;
    }


    /**
     * 设置异步任务信息
     */
    public void setTaskInfo(AsyncTaskInfo taskInfo) {
        taskContainer.put(taskInfo.getTaskId(), taskInfo);
    }

    /**
     * 获取异步任务监控信息
     *
     * @param taskId
     * @return
     */
    public AsyncTaskInfo getTaskInfo(String taskId) {
        return taskContainer.get(taskId);
    }
}
