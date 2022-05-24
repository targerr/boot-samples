package com.example.service;

import com.example.enums.ServiceStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/5/24 13:54
 * @Classname HandlerHolder
 * @Description service->taskHandler的映射关系
 */
@Component
public class HandlerHolder {
    private Map<ServiceStrategy, TaskHandler> TASK_HANDLER_MAP = new HashMap<>(128);

    public HandlerHolder(List<TaskHandler> taskHandlerList) {
        taskHandlerList.forEach(taskHandler -> TASK_HANDLER_MAP.put(taskHandler.getTaskType(), taskHandler));
    }

    public TaskHandler route(ServiceStrategy serviceStrategy) {
        return TASK_HANDLER_MAP.get(serviceStrategy);
    }
}
