package com.example.datasource.aop;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wgs
 * @Date: 2024/9/8
 */
@Aspect
@Order(1)  // 确保 AOP 切面在其他 AOP 切面之前执行
@Component
public class DataSourceAspect {
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    @Before("restController()")
    public void beforeSwitchDataSource() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String dataSource = request.getHeader("X-Datasource");
            if (dataSource != null) {
                DynamicDataSourceContextHolder.push(dataSource);
            } else {
                // 默认数据源
                DynamicDataSourceContextHolder.push("master");
            }
        }
    }
}
