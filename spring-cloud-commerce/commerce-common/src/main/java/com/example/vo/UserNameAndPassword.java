package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/11/7 11:02
 * @Classname UserNameAndPassword
 * @Description 用户名和密码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNameAndPassword {

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;
}
