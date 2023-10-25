package com.example.aop;

import com.example.annotation.Permission;
import com.example.enums.ResultEnum;
import com.example.exception.PreException;
import com.example.service.SysCoreService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wgs
 * @Date 2023/10/24
 * @Classname PermissionCheckAop
 * @since 1.0.0
 */
@Aspect
@Component
@Slf4j
@Setter(onMethod_ = {@Autowired})
public class PermissionCheckAop {
    private SysCoreService sysCoreService;
    @Pointcut(value = "@annotation(permission)")
    public void pointcut(Permission permission) {
    }

    @Around(value = "pointcut(permission)", argNames = "proceedingJoinPoint,permission")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Permission permission) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //通过 Servlet 容器对象获取 request 请求对象
        HttpServletRequest request = attributes.getRequest();

            String requestUrl = request.getRequestURI();
            //校验权限
            if (!sysCoreService.hasUrlAcl(requestUrl)){
                throw new PreException(ResultEnum.USER_ACL_NOT_EXIST);
            }

        return proceedingJoinPoint.proceed();
    }
}
