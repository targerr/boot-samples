package com.example.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import com.example.dto.OptLogDTO;
import com.example.evnet.SysLogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/4/20 11:05
 * @Classname TestController
 * @Description
 */
@RestController
@RequestMapping("/event")
public class TestController {
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/index")
    public Dict index() {
        long threadId = Thread.currentThread().getId();
        //构造操作日志信息
        OptLogDTO logInfo = new OptLogDTO();
        logInfo.setIp("127.0.0.1");
        logInfo.setThreadId(Convert.toStr(threadId));
        logInfo.setThreadName(ThreadUtil.currentThreadGroup().getName());
        logInfo.setDescription("操作日志");

        //构造事件对象
        ApplicationEvent event = new SysLogEvent(logInfo);

        //发布事件
        applicationContext.publishEvent(event);

        System.out.println("发布事件,线程id：" + threadId);
        return Dict.create().set("result", logInfo);
    }
}
