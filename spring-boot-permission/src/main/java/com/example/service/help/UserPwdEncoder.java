package com.example.service.help;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @Author: wgs
 * @Date 2023/10/17 15:53
 * @Classname UserPwdEncoder
 * @Description 密码加密器
 */
@Component
public class UserPwdEncoder {
    @Value("${security.salt")
    private String salt;
    @Value("${security.salt-index}")
    private Integer saltIndex;


    public boolean match(String password, String encPwd) {
        return Objects.equals(encPwd(password), encPwd);

    }

    /**
     * 明文密码处理
     *
     * @param plainPwd
     * @return
     */
    public String encPwd(String plainPwd) {
        if (plainPwd.length() > saltIndex) {
            plainPwd = plainPwd.substring(0, saltIndex) + salt + plainPwd.substring(saltIndex);
        } else {
            plainPwd = plainPwd + salt;
        }
        return DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8));
    }

}
