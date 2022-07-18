package com.example.param.getui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/7/18 14:22
 * @Classname SendPushParam
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendPushParam {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String body;
}
