package com.example.aspectj;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.annotation.RepeatSubmit;
import com.example.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/7/6 14:08
 * @Classname RepeatSubmitAspect
 * @Description 防止表单重复提交（参考美团GTIS）
 */
@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {
    @Autowired
    private RedissonClient redissonClient;

    private static final ThreadLocal<String> KEY_CACHE = new ThreadLocal<>();

    /**
     * 防重提交 redis key
     */
    String REPEAT_SUBMIT_KEY = "repeat_submit:";

    @Before("@annotation(repeatSubmit)")
    public void doBefore(JoinPoint point, RepeatSubmit repeatSubmit){
        System.err.println("---------");
        // 如果注解不为0 则使用注解数值
        long interval = repeatSubmit.timeUnit().toMillis(repeatSubmit.interval());
        System.out.println(interval);
        if (interval < 1000) {
            System.out.println("=================");
            throw new ServiceException(300, "重复提交间隔时间不能小于'1'秒");
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String nowParams = argsArrayToString(point.getArgs());

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();
        // 唯一值（没有消息头则使用请求地址）
        String submitKey = StringUtils.trimToEmpty(request.getHeader("token"));

        submitKey = SecureUtil.md5(submitKey + ":" + nowParams);
        // 唯一标识（指定key + url + 消息头）
        String cacheRepeatKey = REPEAT_SUBMIT_KEY + url + submitKey;

        String key = Convert.toStr(redissonClient.getBucket(cacheRepeatKey).get());
        if (key == null) {
            final RBucket<String> result = redissonClient.getBucket(cacheRepeatKey);
            result.set("");
            result.expire(Duration.ofMillis(interval));
            KEY_CACHE.set(cacheRepeatKey);
        } else {
            throw new ServiceException(300, "操作太频繁了,稍后再试!!");
        }
    }


    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(repeatSubmit)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, RepeatSubmit repeatSubmit, Object jsonResult) {
        //  if (jsonResult instanceof R) {}
        redissonClient.getBucket(KEY_CACHE.get()).delete();
        KEY_CACHE.remove();

    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(repeatSubmit)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, RepeatSubmit repeatSubmit, Exception e) {
        redissonClient.getBucket(KEY_CACHE.get()).delete();
        KEY_CACHE.remove();
    }


    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        params.append(JSONObject.toJSONString(o)).append(" ");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }

}
