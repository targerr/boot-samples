package com.example.webstater.result;

import cn.hutool.http.HttpStatus;
import com.example.webstater.base.enums.IErrorCode;
import com.example.webstater.base.enums.ResponseCode;
import com.example.webstater.base.enums.ResultErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2024/10/18 16:35
 * @Classname ApiResult
 * @Description web接口响应结果
 */
@Setter
@Getter
@Accessors(chain = true)
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -7545356835815831989L;

    /**
     * 响应编码
     */
    @JsonProperty(index = 1)
    private int code;

    /**
     * 响应信息
     */
    @JsonProperty(index = 2)
    private String msg;

    /**
     * 是否成功
     */
    @JsonProperty(index = 3)
    private boolean success;

    /**
     * 响应数据
     */
    @JsonProperty(index = 4)
    private T data;

    /**
     * 异常码
     */
    @JsonProperty(index = 5)
    private String errorCode;

    /**
     * 异常信息
     */
    @JsonProperty(index = 6)
    private String errorMsg;

    /**
     * 附加数据
     */
    @JsonProperty(index = 8)
    private Map<String, Object> extra;

    /**
     * 成功返回结果
     *
     * @param data 数据
     * @return ApiResult
     */
    public static <T> ApiResult<?> success(T data) {
        return response(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data, null, null, null);
    }

    /**
     * 成功返回结果
     *
     * @param data 数据
     * @return ApiResult
     */
    public static <T> ApiResult<?> success(String msg, T data) {
        return response(ResponseCode.SUCCESS.getCode(), msg, data, null, null, null);
    }

    /**
     * 异常返回结果
     *
     * @param errorCode 异常状态码
     * @param errorMsg  异常信息
     * @return ApiResult
     */
    public static ApiResult<?> fail(String errorCode, String errorMsg) {
        return response(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMessage(), null, errorCode, errorMsg, null);
    }

    /**
     * 异常返回结果
     *
     * @param errorCode 异常状态
     * @return ApiResult
     */
    public static ApiResult<?> fail(IErrorCode errorCode) {
        return response(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMessage(), null, errorCode.getErrorCode(),
                errorCode.getErrorMsg(), null);
    }

    /**
     * 请求失败消息，根据异常类型，获取不同的提供消息
     *
     * @param throwable 异常
     * @return RPC调用结果
     */
    public static ApiResult<?> fail(Throwable throwable) {
        String msg = throwable != null ? throwable.getMessage() : ResultErrorCode.FAILURE.getErrorMsg();
        return response(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getMessage(), null, ResultErrorCode.FAILURE.getErrorCode(),
                msg, null);
    }

    /**
     * 返回结果
     *
     * @param errorCode 异常状态码
     * @param errorMsg  异常信息
     * @return ApiResult
     */
    private static <T> ApiResult<?> response(int code, String msg, T data, String errorCode, String errorMsg, Map<String, Object> extra) {
        return ApiResult.builder()
                .code(code)
                .msg(msg)
                .data(data)
                .errorCode(errorCode)
                .errorMsg(errorMsg)
                .extra(extra)
                .build();
    }

    public static <T> ResponseBuilder<T> builder() {
        return new ResponseBuilder<>();
    }

    public ApiResult<?> put(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.put(key, value);
        return this;
    }

    public ApiResult<?> putAll(Map<String, Object> extra) {
        if (this.extra == null) {
            this.extra = new HashMap<>(16);
        }
        this.extra.putAll(extra);
        return this;
    }

    public static class ResponseBuilder<T> {

        private int code;
        private String msg;
        private T data;
        private String errorCode;
        private String errorMsg;
        private Map<String, Object> extra;

        public ResponseBuilder<T> code(int code) {
            this.code = code;
            return this;
        }

        public ResponseBuilder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        public ResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseBuilder<T> errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ResponseBuilder<T> errorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
            return this;
        }

        public ResponseBuilder<T> extra(Map<String, Object> extra) {
            this.extra = extra;
            return this;
        }

        public ApiResult<T> build() {
            Map<String, Object> map = this.extra == null ? new HashMap<>() : this.extra;
            return new ApiResult<T>()
                    .setCode(this.code)
                    .setSuccess(this.code == HttpStatus.HTTP_OK)
                    .setMsg(this.msg)
                    .setData(this.data)
                    .setErrorCode(this.errorCode)
                    .setErrorMsg(this.errorMsg)
                    .setExtra(map);
        }
    }
}
