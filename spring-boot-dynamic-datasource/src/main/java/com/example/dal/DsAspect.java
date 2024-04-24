package com.example.dal;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @Author: wgs
 * @Date 2024/4/23 14:24
 * @Classname DsAspect
 * @Description
 */
@Aspect
public class DsAspect {
    /**
     * 切入点, 拦截类上、方法上有注解的方法，用于切换数据源
     */
    @Pointcut("@annotation(com.example.dal.DsAno) || @within(com.example.dal.DsAno)")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DsAno ds = getDsAno(proceedingJoinPoint);
        try {
            if (ds != null && (StringUtils.isNotBlank(ds.ds()) || ds.value() != null)) {
                // 当上下文中没有时，则写入线程上下文，应该用哪个DB(全部不为空)
                DsContextHolder.set(StrUtil.isAllNotBlank(ds.ds()) ? ds.ds() : ds.value().name());
            }
            return proceedingJoinPoint.proceed();
        } finally {
            // 清空上下文信息
            if (ds != null) {
                DsContextHolder.reset();
            }
        }
    }

    private DsAno getDsAno(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        DsAno ds = method.getAnnotation(DsAno.class);
        if (ds == null) {
            // 获取类上的注解
            ds = (DsAno) proceedingJoinPoint.getSignature().getDeclaringType().getAnnotation(DsAno.class);
        }
        return ds;
    }
}
