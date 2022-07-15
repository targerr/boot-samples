package com.example.chain.event;

import com.example.chain.domain.TaskInfo;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: wgs
 * @Date 2022/7/15 10:40
 * @Classname BusinessEvent
 * @Description
 */
public class BusinessEvent extends ApplicationEvent {
    public BusinessEvent(TaskInfo info) {
        super(info);
    }
}
