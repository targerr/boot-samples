package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/6/14 16:51
 * @Classname WxH5Model
 * @Description 微信H5
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxH5Model extends PayReqModel{
    private String body;
    private String attach;
    private String ip;
}
