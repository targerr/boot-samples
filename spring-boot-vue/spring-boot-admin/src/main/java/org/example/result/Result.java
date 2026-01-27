package org.example.result;

import lombok.Data;

import java.io.Serializable;

/** 统一返回结果
 * @Desc:
 * @Author: 
 * @date: 下午9:44 2023/3/10
 */
@Data
public class Result<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    private Result(ResultCodeEnum resultCodeEnum) {
        this(resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }

    private Result(ResultCodeEnum resultCodeEnum, T data) {
        this(resultCodeEnum);
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    /**
     * 成功构造器，无返回数据
     */
    public Result() {
        this(ResultCodeEnum.SUCCESS);
    }

    /**
     * 成功构造器，自定义返回数据
     *
     * @param data
     */
    private Result(T data) {
        this(ResultCodeEnum.SUCCESS, data);
    }


    /**
     * 成功构造器，自定义返回信息和返回数据
     *
     * @param msg
     * @param data
     */
    private Result(String msg, T data) {
        this(ResultCodeEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * 默认成功
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return new Result<>();
    }

    /**
     * 成功,自定义返回数据
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    /**
     * 成功,自定义返回数据和消息
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(message, data);
    }

    /**
     * 失败，默认状态码和返回消息
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail() {
        return new Result<>(ResultCodeEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 失败，自定义返回消息，无返回数据
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(),message);
    }

    /**
     * 失败，自定义状态码和返回消息，无返回数据
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg);
    }

    /**
     * 失败，使用结果枚举
     * @param resultCodeEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        return new Result<>(resultCodeEnum);
    }

}
