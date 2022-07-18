package com.example.param;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: wgs
 * @Date 2022/1/20 11:20
 * @Classname MessageParam
 * @Description
 */
@Data
@Builder
public class MessageParam {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String body;

}
