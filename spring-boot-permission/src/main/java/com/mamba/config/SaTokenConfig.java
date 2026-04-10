package com.mamba.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 路由拦截配置
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 只做登录校验，不做细粒度权限检查
            // 细粒度权限检查在 Controller 方法上使用 @SaCheckPermission 注解
            SaRouter.match("/api/**")
                    .notMatch("/api/auth/login")
                    .notMatch("/api/auth/userInfo")
                    .notMatch("/api/auth/userPermissions")
                    .notMatch("/doc.html", "/webjars/**", "/v3/api-docs/**", "/swagger-resources/**", "/favicon.ico")
                    .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/api/**");
    }
}
