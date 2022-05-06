package com.example.log;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * 日志拦截器
 *
 * @author wanggaoshuai
 * @date 2022/05/06
 */
public class MyLogInterceptor extends HandlerInterceptorAdapter {
    private static final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //获得被拦截的方法对象
        Method method = handlerMethod.getMethod();
        //获得方法上的注解
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            //方法上加了MyLog注解，需要进行日志记录
            long startTime = System.currentTimeMillis();
            startTimeThreadLocal.set(startTime);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //获得被拦截的方法对象
        Method method = handlerMethod.getMethod();
        //获得方法上的注解
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            //方法上加了MyLog注解，需要进行日志记录
            long endTime = System.currentTimeMillis();
            Long startTime = startTimeThreadLocal.get();
            long optTime = endTime - startTime;

            String requestUri = request.getRequestURI();
            String methodName = method.getDeclaringClass().getName() + "." +
                    method.getName();
            String methodDesc = myLog.desc();

            System.out.println("请求线程名称：" + Thread.currentThread().getName());
            System.out.println("请求线程id：" + Thread.currentThread().getId());
            System.out.println("请求uri：" + requestUri);
            System.out.println("请求方法名：" + methodName);
            System.out.println("方法描述：" + methodDesc);
            System.out.println("方法执行时间：" + optTime + "ms");
        }
    }
}