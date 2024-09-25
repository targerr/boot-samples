package com.github.echo.manager;

import com.github.echo.annotation.EchoField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description: 封装字段上标记了 EchoField 注解的字段
 *
 * @Author: wgs
 * @Date 2024/9/5 14:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldParam {

    /**
     * 当前字段上的注解
     */
    private EchoField echoField;
    /**
     * 从当前字段的值构造出的调用api#method方法的参数
     */
    private Serializable actualValue;
    /**
     * 当前字段的具体值
     */
    private Object originalValue;

    /**
     * 当前 字段名
     */
    private String fieldName;

    private LoadKey loadKey;
}
