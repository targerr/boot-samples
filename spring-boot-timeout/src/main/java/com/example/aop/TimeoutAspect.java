package com.example.aop;

import com.example.annotation.Timeout;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @Author: wgs
 * @Date 2025/8/5 09:59
 * @Classname TimeoutAspect
 * @Description
 */
@Aspect
@Component
@Slf4j
public class TimeoutAspect implements BeanFactoryAware {
    private BeanFactory beanFactory;

    // 切面核心逻辑
    @Around("@annotation(timeout)")
    public Object timeoutAround(ProceedingJoinPoint pjp, Timeout timeout) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        // 根据配置注解上的超时属性value，解析SpEL表达式
        long timeoutMs = resolveTimeout(method, timeout);
        // 获取重试次数
        int retry = timeout.retry();
        // 获取降级方法名称
        String fallbackMethod = timeout.fallback();
        // 获取指定线程池
        Executor executor = getExecutor(timeout.executor());
        // 进入重试逻辑
        int attempt = 0;
        do {
            try {
                // 通过线程池对象提交有返回值的任务
                Future<Object> future = ((ExecutorService) executor).submit(() -> {
                    try {
                        return pjp.proceed();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
                // 获取结果数据时，设置超时时间
                return future.get(timeoutMs, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                // 只有发生超时异常时才会进入到重试的逻辑
                log.warn("{} - 调用超时, {}", method, e.getMessage());
                // 超过重试的次数后，调用降级方法
                if (attempt++ >= retry) {
                    return handleFallback(pjp, fallbackMethod, e);
                }
                long pow = (long) Math.pow(2, attempt - 1);
                long waitTime = timeout.retryDelay() * pow;
                TimeUnit.MILLISECONDS.sleep(waitTime);
                log.warn("第 {} 次, 重试 - {}", attempt, waitTime);
            } catch (Exception e) {
                throw e.getCause();
            }
        } while (true);
    }

    // 获取线程池对象bean
    private Executor getExecutor(String executorBean) {
        Executor executor = null;
        if (StringUtils.hasLength(executorBean)) {
            try {
                // 根据注解@Timeout中配置的线程池beanName获取线程池对象
                executor = this.beanFactory.getBean(executorBean, ExecutorService.class);
            } catch (Exception e) {
                // 无法获取bean时，创建默认的线程池对象，核心线程为当前CPU的核数
                int core = Runtime.getRuntime().availableProcessors();
                executor = new ThreadPoolExecutor(core, core, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024));
            }
        }
        return executor;
    }

    // 调用降级方法（当发放执行超时时）
    private Object handleFallback(ProceedingJoinPoint pjp, String fallbackMethod, Exception e) throws Exception {
        Method method = getFallbackMethod(pjp, fallbackMethod);
        if (method == null) {
            log.error("未找到降级方法 [{}]，异常信息: {}", fallbackMethod, e.getMessage());
            return "服务超时，且未配置降级方法";
        }
        try {
            return method.invoke(pjp.getTarget(), getParamValues(e, method, pjp.getArgs()));
        } catch (Exception ex) {
            throw new RuntimeException("执行降级方法失败: " + fallbackMethod, ex);
        }
    }

    // 解析降级方法
    private Method getFallbackMethod(ProceedingJoinPoint pjp, String fallback) {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Method fallbackMethod = null;
        try {
            // 获取与业务方法完全一样参数的降级方法
            fallbackMethod = method.getDeclaringClass().getDeclaredMethod(fallback, parameterTypes);
            fallbackMethod.setAccessible(true);
        } catch (Exception e) {
            // 获取不到方法，则获取最后一个参数带有异常对象的方法
            int leng = parameterTypes.length;
            Class<?>[] types = new Class<?>[leng + 1];
            for (int i = 0; i < leng; i++) {
                types[i] = parameterTypes[i];
            }
            types[leng] = Throwable.class;
            try {
                fallbackMethod = method.getDeclaringClass().getDeclaredMethod(fallback, types);
            } catch (Exception ex) {
                log.error("获取降级方法错误: {}", ex.getMessage());
            }
        }
        return fallbackMethod;
    }

    // 解析执行的业务方法参数；同时会在最后拼如一个异常参数
    private Object[] getParamValues(Throwable e, Method method, Object... args) {
        int count = method.getParameterCount();
        Object[] params = args;
        int len = args.length;
        if (count == len + 1) {
            params = new Object[count];
            for (int i = 0; i < len; i++) {
                params[i] = args[i];
            }
            params[count - 1] = e;
        }
        return params;
    }

    // 解析 SpEL 表达式获取动态超时时间
    private long resolveTimeout(Method method, Timeout timeout) {
        String timeoutExpr = timeout.value();
        try {
            DefaultListableBeanFactory bf = (DefaultListableBeanFactory) beanFactory;
            String embeddedValue = bf.resolveEmbeddedValue(timeoutExpr);
            Object value = bf.getBeanExpressionResolver().evaluate(embeddedValue, new BeanExpressionContext(bf, null));
            Long tm = bf.getConversionService().convert(value, Long.class);
            return timeout.unit().toMillis(tm != null ? tm : 5000L);
        } catch (Exception e) {
            log.error("解析超时时间失败，使用默认值: {}", e.getMessage());
            return timeout.unit().toMillis(5000);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}