package com.example.flowcontrol;

import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/5/23 14:36
 * @Classname FlowControlParam
 * @Description
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowControlParam {
    /**
     * 限流器
     */
    protected ConcurrentHashMap<String, RateLimiter> rateLimiterConcurrentHashMap;
    /**
     * 限流器初始限流大小
     */
    protected Double rateInitValue = 3.0;
    /**
     * 限制依据（ip/用户）
     */
    protected String key;

    /**
     * 超时时长
     */
    int timeout = 0;

    /**
     * 超时时间单位
     */
    TimeUnit timeUnit = TimeUnit.MILLISECONDS;
}
