package com.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wgs
 * @Date 2024/10/16 14:03
 * @Classname ExternalRequestParam
 * @Description 三方请求参数
 */
@Data
public class ExternalRequestParam implements Serializable {
    private static final long serialVersionUID = -819604128249901601L;
    private String param;
    private String result;
}
