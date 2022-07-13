package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/6/16 10:24
 * @Classname WxJSAPIModel
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxJSAPIModel extends PayReqModel{
    private String body;
    private String attach;
    private String ip;
    private String openId;
}
