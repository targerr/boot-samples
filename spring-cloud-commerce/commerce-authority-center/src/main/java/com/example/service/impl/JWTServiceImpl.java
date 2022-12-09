package com.example.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.example.constant.AuthorityConstant;
import com.example.constant.CommonConstant;

import com.example.dao.EcommerceUserDao;
import com.example.entity.EcommerceUser;
import com.example.service.IJWTService;
import com.example.vo.LoginUserInfo;
import com.example.vo.UserNameAndPassword;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: wgs
 * @Date 2022/11/8 15:44
 * @Classname JWTServiceImpl
 * @Description JWT 相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JWTServiceImpl implements IJWTService {
    private final EcommerceUserDao ecommerceUserDao;

    public JWTServiceImpl(EcommerceUserDao ecommerceUserDao) {
        this.ecommerceUserDao = ecommerceUserDao;
    }

    @Override
    public String generateToken(String username, String password) throws Exception {
        return generateToken(username, password, 0);
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {
        EcommerceUser ecommerceUser = ecommerceUserDao.findByUsernameAndPassword(username, password);
        if (ecommerceUser == null) {
            log.error("【用户】没有找到:{},{}", username, password);
            return null;
        }

        // Token 中对象，即 JWT 中存储用户信息。
        LoginUserInfo loginUserInfo = new LoginUserInfo(
                ecommerceUser.getId(), ecommerceUser.getUsername()
        );

        if (expire <= 0) {
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }

        // 计算超时时间
        Date expireDate = DateUtil.offsetDay(new Date(), expire);

        return buildJwt(loginUserInfo, expireDate);
    }


    @Override
    public String registerUserAndGenerateToken(UserNameAndPassword usernameAndPassword) throws Exception {

        // 校验用户是否存在
        EcommerceUser oldUser = ecommerceUserDao.findByUsername(usernameAndPassword.getUsername());
        if (oldUser != null) {
            log.error("【用户注册】 该用户已经存在:{}", usernameAndPassword.getUsername());
            return null;
        }

        // 落盘用户信息
        EcommerceUser newUser = new EcommerceUser();
        newUser.setUsername(usernameAndPassword.getUsername());
        // 后期MD5加密
        newUser.setPassword(usernameAndPassword.getPassword());
        newUser.setExtraInfo("{}");

        newUser = ecommerceUserDao.save(newUser);
        log.info("【用户注册】 成功:{},{}", newUser.getUsername(), newUser.getId());

        // 生成Token并返回
        return generateToken(usernameAndPassword.getUsername(), usernameAndPassword.getPassword());
    }

    /**
     * 根据本地私钥，获取 PrivateKey 对象
     *
     * @return
     * @throws Exception
     */
    private PrivateKey getPrivateKey() throws Exception {

        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }

    /**
     * 构建 Token
     *
     * @param loginUserInfo
     * @param expireDate
     * @return
     * @throws Exception
     */
    private String buildJwt(LoginUserInfo loginUserInfo, Date expireDate) throws Exception {
        return Jwts.builder()
                // jwt payload --> KV
                .claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(expireDate)
                // jwt 签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }
}
