package com.github.common.log.event;

import com.github.common.log.pojo.LogOperateDo;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: wgs
 * @Date 2024/9/26 16:37
 * @Classname SysLogOperateEvent
 * @Description
 * 系统日志事件
 */
public class SysLogOperateEvent extends ApplicationEvent {

    public SysLogOperateEvent(LogOperateDo source) {
        super(source);
    }
}
