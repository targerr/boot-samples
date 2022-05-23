package com.example.aspect;

import com.example.annoation.CustomRateLimiter;
import com.example.enums.CommonEnum;
import com.example.exception.RateException;
import com.example.flowcontrol.FlowControlParam;
import com.example.flowcontrol.FlowControlService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wgs
 * @Date 2022/5/22 14:04
 * @Classname RateLimiterAspect
 * @Description
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAspect {
    @Autowired
    private FlowControlService flowControlService;

    private static final ConcurrentHashMap<String, RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap();

    /**
     * 切点
     *
     * @param customRateLimiter
     */
    @Pointcut(value = "@annotation(customRateLimiter)")
    public void pointcut(CustomRateLimiter customRateLimiter) {
    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around(value = "pointcut(customRateLimiter)")
    public Object aroundLog(ProceedingJoinPoint point, CustomRateLimiter customRateLimiter) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

       // doHandler(customRateLimiter, method);
        doFlowControl(customRateLimiter, method);
        return point.proceed();
    }

    private void doFlowControl(CustomRateLimiter customRateLimiter, Method method) {
        FlowControlParam param = FlowControlParam
                .builder()
                .rateLimiterConcurrentHashMap(RATE_LIMITER_CACHE)
                .rateInitValue(customRateLimiter.qps())
                .timeout(customRateLimiter.timeout())
                .key(method.getName())
                .timeUnit(customRateLimiter.timeUnit())
                .build();

        flowControlService.flowControl(param);
    }

    /**
     * 处理
     * @param customRateLimiter
     * @param method
     */
    private void doHandler(CustomRateLimiter customRateLimiter, Method method) {
        if (customRateLimiter != null && customRateLimiter.qps() > CustomRateLimiter.NOT_LIMITED) {
            double qps = customRateLimiter.qps();
            // TODO 这个key可以根据具体需求配置,例如根据ip限制,或用户
            if (RATE_LIMITER_CACHE.get(method.getName()) == null) {
                // 初始化 QPS
                RATE_LIMITER_CACHE.put(method.getName(), RateLimiter.create(qps));
            }

            log.debug("【{}】的QPS设置为: {}", method.getName(), RATE_LIMITER_CACHE.get(method.getName()).getRate());
            // 尝试获取令牌
            if (RATE_LIMITER_CACHE.get(method.getName()) != null && !RATE_LIMITER_CACHE.get(method.getName()).tryAcquire(customRateLimiter.timeout(), customRateLimiter.timeUnit())) {
                throw new RateException(CommonEnum.ResultEnum.RATE_ERROR);
            }
        }
    }
}
