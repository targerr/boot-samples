package com.github.common.log.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Author: wgs
 * @Date 2024/9/27 10:46
 * @Classname RequestLogEvent
 * @Description
 */
public class RequestLogEvent extends ApplicationEvent {
    public RequestLogEvent(String msg) {
        super(msg);
    }
}
