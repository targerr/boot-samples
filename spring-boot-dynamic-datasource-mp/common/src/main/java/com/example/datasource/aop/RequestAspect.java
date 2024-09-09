package com.example.datasource.aop;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.example.datasource.util.DynamicSourceTtl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wgs
 * @Date 2024/9/4 16:31
 * @Classname DataSourceAspect
 * @Description
 */
@Aspect
@Order(1)
@Component
@Slf4j
public class RequestAspect {

    @Pointcut("within(@com.example.datasource.annotations.RequestRequired *)")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before(JoinPoint point) throws Exception {
        HttpServletRequest request = getHttpServletRequest();
        if (request != null) {
            //数据源
            String dataSource = getDataSource(request, point);
            //配置使用数据源
            DynamicDataSourceContextHolder.push(dataSource);

        }

    }

    private String getDataSource(HttpServletRequest request, JoinPoint point) {
        //配置使用数据源
        String dataSource = request.getHeader("X-Datasource");
        return dataSource;
    }

    @After("pointcut()")
    public void after(JoinPoint joinPoint) throws Exception {
        //移除数据源变量，防止堆积
        DynamicSourceTtl.clear();
    }


    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = getHttpServletRequest();
        Object o = proceedingJoinPoint.proceed();


        return o;
    }

    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterThrow(JoinPoint joinPoint, Throwable e) throws Exception {
        //移除数据源变量，防止堆积
        DynamicSourceTtl.clear();
    }


    private HttpServletRequest getHttpServletRequest() {
        //获得请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }
}
