package com.github.common.log.config;

import com.github.common.log.aspect.RequestLogAspect;
import com.github.common.log.aspect.SysLogAspect;
import com.github.common.log.event.RequestLogListener;
import com.github.common.log.event.SysLogOperateListener;
import com.github.common.log.holder.SpringContextHolder;
import com.github.common.log.service.RemoteLogOperateService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: wgs
 * @Date 2024/9/26 16:41
 * @Classname LogAutoConfiguration
 * @Description 日志自动配置
 */
@EnableAsync
@Configuration
@AllArgsConstructor
@EnableAspectJAutoProxy
@ConditionalOnWebApplication
public class LogAutoConfiguration {
    private final RemoteLogOperateService logOperateService;

    @Bean
    @ConditionalOnMissingBean
    public SysLogOperateListener sysLogOperateListener() {
        return new SysLogOperateListener(logOperateService);
    }


    @Bean
    public SysLogAspect loggingAspect() {
        return new SysLogAspect();
    }

    @Bean
    public SpringContextHolder springContextHolder (){
        return new SpringContextHolder();
    }

    @Bean
    public RequestLogAspect requestLogAspect() {
        return new RequestLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestLogListener requestLogListener() {
        return new RequestLogListener(log -> {
        });
    }

}
