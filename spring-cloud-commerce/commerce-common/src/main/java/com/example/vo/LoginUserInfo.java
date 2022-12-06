package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/11/7 10:58
 * @Classname LoginUserInfo
 * @Description 登录用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserInfo {
    /** 用户 id */
    private Long id;
    /** 用户名 */
    private String username;
}
