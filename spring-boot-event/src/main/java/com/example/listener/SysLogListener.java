package com.example.listener;

import com.example.dto.OptLogDTO;
import com.example.evnet.SysLogEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/5/5 10:09
 * @Classname SysLogListener
 * @Description
 */
@Component
public class SysLogListener {

    @Async
    @EventListener(SysLogEvent.class)
    public void saveLogEvent(SysLogEvent event) {
        OptLogDTO sysLog = (OptLogDTO) event.getSource();
        long id = Thread.currentThread().getId();
        System.out.println("监听到日志操作事件：" + sysLog + " 线程id：" + id);
        //日志信息落盘...

    }
}
