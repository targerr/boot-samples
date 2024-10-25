package com.github.common.log.event;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

/**
 * @Author: wgs
 * @Date 2024/9/27 10:47
 * @Classname RequestLogListener
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
public class RequestLogListener {
    private final Consumer<String> consumer;

    @Async
    @EventListener(RequestLogEvent.class)
    public void saveRequestLog(RequestLogEvent event) {
        String requestInfoLog = (String) event.getSource();
        if (StrUtil.isEmpty(requestInfoLog)) {
            log.warn("忽略请求日志记录");
            return;
        }
        consumer.accept(requestInfoLog);
    }
}
