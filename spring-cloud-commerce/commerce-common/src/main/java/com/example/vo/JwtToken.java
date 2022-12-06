package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/11/7 11:16
 * @Classname JwtToken
 * @Description 授权返回客户端 Token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtToken {

    /** JWT */
    private String token;
}
