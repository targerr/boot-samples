package com.example.webstater.supper;

import cn.hutool.core.convert.Convert;
import com.example.webstater.annotation.IgnoreResponseWrap;
import com.example.webstater.base.constant.BaseContextConstants;
import com.example.webstater.base.enums.ResponseCode;
import com.example.webstater.base.enums.ResultErrorCode;
import com.example.webstater.base.utils.RequestContextUtils;
import com.example.webstater.result.ApiResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: wgs
 * @Date 2024/10/18 16:30
 * @Classname ResponseResultAdvice
 * @Description 统一的结果响应格式、简单的数据封装
 * https://blog.csdn.net/smart_an/article/details/140901567
 */
@Slf4j
@ControllerAdvice
public class ResponseResultAdvice implements ResponseBodyAdvice<Object> {
    //不支持的类型列表
    private static final Set<Class<?>> NO_SUPPORTED_CLASSES = new HashSet<>(8);

    static {
        NO_SUPPORTED_CLASSES.add(byte[].class);
        NO_SUPPORTED_CLASSES.add(javax.xml.transform.Source.class);
        NO_SUPPORTED_CLASSES.add(ApiResult.class);
    }


    /**
     * 会先调用ResponseBodyAdvice的supports方法，如果方法返回true，则会进到其beforeBodyWrite方法中
     *
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //如果返回值是NO_SUPPORTED_CLASSES中的类型，则不会被当前类的beforeBodyWrite方法处理，即不会被包装为ResultDto类型
        if (NO_SUPPORTED_CLASSES.contains(returnType.getParameterType())) {
            return false;
        }
        // 判断是否有 IgnoreResponseWrap 注解
        boolean supported = returnType.getExecutable()
                .getDeclaredAnnotationsByType(IgnoreResponseWrap.class).length == 0;

        if (log.isDebugEnabled()) {
            log.debug("判断是否需要全局统一API响应：{}", supported ? "是" : "否");
        }
        return supported;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        HttpServletResponse servletResponse = RequestContextUtils.getResponse();
        setupResponseHeaders(servletResponse);

        if (body instanceof ApiResult) {
            return body;
        }

        // 根据不同条件返回不同的 ApiResult
        return createApiResponse(body, servletResponse);
    }

    /**
     * 设置响应头
     *
     * @param servletResponse HttpServletResponse
     */
    private void setupResponseHeaders(HttpServletResponse servletResponse) {
        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 根据 body 和 status 创建 ApiResult
     *
     * @param body            返回体
     * @param servletResponse HttpServletResponse
     * @return ApiResult 封装对象
     */
    private ApiResult<?> createApiResponse(Object body, HttpServletResponse servletResponse) {
        int status = servletResponse.getStatus();
        Boolean fallback = Convert.toBool(RequestContextUtils.getAttribute(BaseContextConstants.REMOTE_CALL), Boolean.FALSE);

        if (fallback) {
            return ApiResult.fail(ResultErrorCode.SERVICE_DEGRADATION.getErrorCode(),
                    ResultErrorCode.SERVICE_DEGRADATION.getErrorMsg());
        }
        if (body instanceof Boolean && !(Boolean) body) {
            return ApiResult.fail(ResultErrorCode.FAILURE.getErrorCode(),
                    ResultErrorCode.FAILURE.getErrorMsg());
        }
        if (status == ResponseCode.FAILURE.getCode()) {
            servletResponse.setStatus(ResponseCode.SUCCESS.getCode());
            return ApiResult.fail(ResultErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
                    ResultErrorCode.INTERNAL_SERVER_ERROR.getErrorMsg());
        }
        return ApiResult.success(body);
    }
}
