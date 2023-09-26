package com.example.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Component
public class TraceIdInterceptor implements HandlerInterceptor {

    private static final String FLAG = "REQUEST_ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String traceId = request.getHeader(FLAG);

        if (StringUtils.isEmpty(traceId)) {
            if (null == MDC.get(FLAG)) {
                MDC.put(FLAG, UUID.randomUUID().toString());
            }
        } else {
            MDC.put(FLAG, traceId);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) {
        // 先清理掉之前的请求 id
        MDC.clear();
    }
}
