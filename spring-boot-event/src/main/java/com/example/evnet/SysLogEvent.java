package com.example.evnet;

import com.example.dto.OptLogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: wgs
 * @Date 2022/5/5 10:08
 * @Classname SysLogEvent
 * @Description 自定义日志事件
 */
public class SysLogEvent extends ApplicationEvent {
    public SysLogEvent(OptLogDTO optLogDTO) {
        super(optLogDTO);
    }
}
