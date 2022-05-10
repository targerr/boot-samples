package com.example.service.impl;

import com.example.service.RetryService;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * @Author: wgs
 * @Date 2022/5/9 16:49
 * @Classname RetryServiceImpl
 * @Description
 */
@Service
public class RetryServiceImpl implements RetryService {
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    @Override
    public Integer check(Integer code) throws Exception {
        System.out.println("被调用,时间：" + LocalTime.now());
        if (code == 0) {
            throw new Exception("故意抛出异常！");
        }
        System.out.println("被调用,情况对头了！");

        return 200;
    }


    @Recover
    public Integer recover(Exception e, int code) {
        System.err.println("回调方法执行！！！！");
        //记日志到数据库 或者调用其余的方法
        return 400;
    }

    public Integer checkCall(Integer code) throws Exception {
        int retryCount = 0;
        for (int i = 0; i < 4; i++) {
            if (retryCount > 3) {
                System.out.println("重试次数达到上限!");
                throw new Exception("重试次数达到上限！");
            }
            if (code == 0) {
                retryCount++;
            } else {
                return 200;
            }

        }

        return 200;
    }
}
