package com.framework.web.log;

import com.github.common.log.event.RequestLogListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2024/9/27 10:55
 * @Classname StartConfig
 * @Description
 */
@Slf4j
@Configuration
public class StartConfig {

    @Bean
    public RequestLogListener requestLogListener(){
        return new RequestLogListener(log -> {
            System.err.println("请求日志: " + log);
        });
    }
}
