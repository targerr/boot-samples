package com.example.flowcontrol.impl;

import com.example.enums.CommonEnum;
import com.example.exception.RateException;
import com.example.flowcontrol.FlowControlParam;
import com.example.flowcontrol.FlowControlService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/5/23 14:40
 * @Classname FlowControlServiceImpl
 * @Description
 */
@Slf4j
@Service
public class FlowControlServiceImpl implements FlowControlService {
    @Override
    public void flowControl(FlowControlParam flowControlParam) {
        // 限流器
        RateLimiter rateLimiter = flowControlParam.getRateLimiterConcurrentHashMap().get(flowControlParam.getKey());
        if (rateLimiter == null) {
            rateLimiter = RateLimiter.create(flowControlParam.getRateInitValue());
            flowControlParam.getRateLimiterConcurrentHashMap().put(flowControlParam.getKey(), rateLimiter);
        }
        // acquire方法传入的是需要的令牌个数，当令牌不足时会进行等待，该方法返回的是等待的时间
        // double costTime = rateLimiter.acquire(1);
        final boolean tryAcquire = rateLimiter.tryAcquire(flowControlParam.getTimeout(), flowControlParam.getTimeUnit());
        if (!tryAcquire) {
            throw new RateException(CommonEnum.ResultEnum.RATE_ERROR);
        }
    }
}
