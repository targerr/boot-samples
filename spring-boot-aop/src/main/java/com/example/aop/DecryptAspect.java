package com.example.aop;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.analyze.ParamAnalyze;
import com.example.annotation.Decrypt;
import com.example.entity.ExternalRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author: wgs
 * @Date 2024/8/13 17:38
 * @Classname DecryptAspect
 * @Description
 */
@Aspect
@Component
@Slf4j
public class DecryptAspect {

    @Pointcut("@annotation(com.example.annotation.Decrypt)")
    public void pointcut() {
    }

    /**
     * 环绕
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        Decrypt api = method.getAnnotation(Decrypt.class);

        Object[] args = proceedingJoinPoint.getArgs();
        String appId = (String) args[0];
        if (StrUtil.isEmpty(appId)) {
            throw new RuntimeException("appId不能为空!");
        }
        ExternalRequestParam requestParam = (ExternalRequestParam) args[1];
        if (Objects.isNull(requestParam)) {
            throw new RuntimeException("requestParam不能为空!");
        }
        // 解密
        ParamAnalyze paramAnalyze = (ParamAnalyze) ReflectUtil.newInstance(api.handler());
        String decryptParam = paramAnalyze.analyzeParam(requestParam.getParam());

        requestParam.setResult(decryptParam);
        return proceedingJoinPoint.proceed(args);

    }

}
