package com.github.log.interceptor;

import com.github.log.annoation.MyLog;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: wgs
 * @Date 2024/9/19 16:39
 * @Classname MyLogInterceptor
 * @Description
 */
public class MyLogInterceptor extends HandlerInterceptorAdapter {
    private static final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 检查 handler 是否是 HandlerMethod 类型
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod(); // 获得被拦截的方法对象
            MyLog myLog = method.getAnnotation(MyLog.class); // 获得方法上的注解
            if (myLog != null) {
                // 方法上加了 MyLog 注解，需要进行日志记录
                long startTime = System.currentTimeMillis();
                startTimeThreadLocal.set(startTime);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        // 检查 handler 是否是 HandlerMethod 类型
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod(); // 获得被拦截的方法对象
            MyLog myLog = method.getAnnotation(MyLog.class); // 获得方法上的注解
            if (myLog != null) {
                // 方法上加了 MyLog 注解，需要进行日志记录
                long endTime = System.currentTimeMillis();
                Long startTime = startTimeThreadLocal.get();
                if (startTime != null) {
                    long optTime = endTime - startTime;

                    String requestUri = request.getRequestURI();
                    String methodName = method.getDeclaringClass().getName() + "." + method.getName();
                    String methodDesc = myLog.desc();

                    System.out.println("请求URI：" + requestUri);
                    System.out.println("请求方法名：" + methodName);
                    System.out.println("方法描述：" + methodDesc);
                    System.out.println("方法执行时间：" + optTime + "ms");
                }
            }
        }
    }
}