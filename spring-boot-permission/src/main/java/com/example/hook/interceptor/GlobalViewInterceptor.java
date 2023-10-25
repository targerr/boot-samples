package com.example.hook.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wgs
 * @Date 2023/10/24
 * @Classname GlobalViewInterceptor
 * @since 1.0.0
 *注入全局的配置信息：
 *- thymleaf 站点信息，基本信息，在这里注入
 */

@Slf4j
@Component
public class GlobalViewInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
        }

        return true;
    }
}
