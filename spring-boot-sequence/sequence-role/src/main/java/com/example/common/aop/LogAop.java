package com.example.common.aop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.aspectj.lang.reflect.MethodSignature;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: wgs
 * @Date 2024/9/26 15:23
 * @Classname LogAop
 * @Description
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class LogAop {
    private final ParameterNameDiscoverer PARAMETERNAMEDISCOVERER = new DefaultParameterNameDiscoverer();



    @Around(" (@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController))")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        //执行前，打印开始时间
        long startTime = System.currentTimeMillis();

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        //执行业务内容
        Object result = point.proceed();
        //执行后
        // 排除字段，敏感字段或太长的字段不显示
        String[] excludeProperties = {"previewSeal", "file", "imageByte", "imageBytes"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);

        log.info("请求token: {}", ServletUtil.getHeader(request, "x-utk", CharsetUtil.UTF_8));

        // 请求参数处理
        final Map<String, Object> paramMap = new HashMap<>(16);
        parseParams(point, paramMap);


        // 排除文件上传
        if (ServletUtil.isPostMethod(request) && !ServletUtil.isMultipart(request)) {
//            log.info("请求数据: {}", JSONObject.toJSONString(getNameAndValue(point)));
            // 请求参数
            if (MapUtil.isNotEmpty(paramMap)) {
                log.info("请求数据: {}", JSONObject.toJSONString(paramMap));
            }


        }
        log.info("响应数据: {}", JSONObject.toJSONString(result, excludeFilter));
        log.info("------------- 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
        return result;
    }


    /**
     * 获取名称和值
     *
     * @param joinPoint 连接点
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    private Map<String, Object> getNameAndValue(ProceedingJoinPoint joinPoint) {

        final Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        final String[] names = methodSignature.getParameterNames();
        final Object[] args = joinPoint.getArgs();

        if (ArrayUtil.isEmpty(names) || ArrayUtil.isEmpty(args)) {
            return Collections.emptyMap();
        }
        if (names.length != args.length) {
            log.warn("{}方法参数名和参数值数量不一致", methodSignature.getName());
            return Collections.emptyMap();
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], args[i]);
        }
        return map;
    }

    private void parseParams(ProceedingJoinPoint point, Map<String, Object> paramMap) {
        // 参数值
        Object[] argValues = point.getArgs();
        // 参数名称
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                // 读取方法参数
                MethodParameter methodParam = getMethodParameter(method, i);
                // PathVariable 参数跳过
                PathVariable pathVariable = methodParam.getParameterAnnotation(PathVariable.class);
                if (pathVariable != null) {
                    continue;
                }
                RequestBody requestBody = methodParam.getParameterAnnotation(RequestBody.class);
                Object value = argValues[i];
                // 如果是body的json则是对象
                if (requestBody != null && value != null) {
                    paramMap.putAll(BeanUtil.beanToMap(value));
                    continue;
                }
                // 处理 List
                if (value instanceof List) {
                    value = ((List) value).get(0);
                }
                // 处理 参数
                if (value instanceof HttpServletRequest) {
                    paramMap.putAll(((HttpServletRequest) value).getParameterMap());
                } else if (value instanceof WebRequest) {
                    paramMap.putAll(((WebRequest) value).getParameterMap());
                } else if (value instanceof MultipartFile) {
                    MultipartFile multipartFile = (MultipartFile) value;
                    String name = multipartFile.getName();
                    String fileName = multipartFile.getOriginalFilename();
                    paramMap.put(name, fileName);
                } else if (value instanceof HttpServletResponse) {
                } else if (value instanceof InputStream) {
                } else if (value instanceof InputStreamSource) {
                } else {
                    // 参数名
                    RequestParam requestParam = methodParam.getParameterAnnotation(RequestParam.class);
                    String paraName;
                    if (requestParam != null && StrUtil.isNotBlank(requestParam.value())) {
                        paraName = requestParam.value();
                    } else {
                        paraName = methodParam.getParameterName();
                    }
                    paramMap.put(paraName, value);
                }
            }
        }
    }

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     */
    public MethodParameter getMethodParameter(Method method, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETERNAMEDISCOVERER);
        return methodParameter;
    }

}
