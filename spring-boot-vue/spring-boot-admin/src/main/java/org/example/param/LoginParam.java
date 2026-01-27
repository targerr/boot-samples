package org.example.param;

import lombok.Data;

/**
 * @author
 * @description TODO
 * @date 2024-10-23 21:33
 */
@Data
public class LoginParam {

    /**
     * 账户
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
