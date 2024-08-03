package com.example.rsa.sm4;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: wgs
 * @Date 2024/8/3 11:28
 * @Classname SM2Test
 * @Description
 * SM2算法的加密和解密过程类似于RSA加解密。*
 */
public class SM2Test {
    public static void test1(){
        String text = "测试aaaaaaaaa";

        SM2 sm2 = SmUtil.sm2();

        //第一种 使用随机密钥对
        String s = sm2.encryptBcd(text, KeyType.PublicKey);
        System.out.println(s);
        String s1 = StrUtil.utf8Str(sm2.decryptFromBcd(s, KeyType.PrivateKey));
        System.out.println(s1);
    }

    public static void test2(){
        String text = "测试aaaaaaaaa";

        System.err.println(text);

        //第二种  使用自定义密钥对
        KeyPair keyPair = SecureUtil.generateKeyPair("SM2");
        byte[] priKey = keyPair.getPrivate().getEncoded();
        byte[] pubKey = keyPair.getPublic().getEncoded();

//        System.out.println("公钥: "+ StrUtil.str(pubKey, CharsetUtil.CHARSET_UTF_8));
        System.out.println("公钥: "+ Base64.encode(pubKey));
        System.out.println("私钥: "+ Base64.encode(priKey));

        System.err.println("=====================================");
        SM2 sm2obj = SmUtil.sm2(priKey,pubKey);
        //公钥加密，私钥解密
        String encStr = sm2obj.encryptBcd(text,KeyType.PublicKey);
        System.err.println("公钥加密: " + encStr);

        String decStr = StrUtil.utf8Str(sm2obj.decryptFromBcd(encStr,KeyType.PrivateKey));
        System.err.println("私钥解密: " + decStr);
    }


    public static void main(String[] args) {
        //第一种 使用随机密钥对
        test1();

        //第二种  使用自定义密钥对
//        test2();

        //第三种 生成pem文件,从pem文件里 读取 公钥 和 私钥,再进行 公钥加密，私钥解密
//        test3();
    }

    /**
     * SM2非对称加密
     */
    public static void test3() {
        String text = "测试aaaaaaaaa";
        System.err.println(text);

        KeyPair pair = SecureUtil.generateKeyPair("SM2");

        PublicKey aPublic = pair.getPublic();
        PrivateKey aPrivate = pair.getPrivate();
        //公钥 key 和私钥 key 转文件
        SM2Utils.exportPublicKey(aPublic,"F:/sm2/public_key.pem");
        SM2Utils.exportPrivateKey(aPrivate,"F:/sm2/private_key.pem");

        //从pem文件里 读取 公钥 和 私钥
        PublicKey pubk2 = SM2Utils.importPublicKey("F:/sm2/public_key.pem");
        PrivateKey priK2 = SM2Utils.importPrivateKey("F:/sm2/private_key.pem");

        //公钥加密
        SM2 sm2 = SmUtil.sm2();
        sm2.setPublicKey(pubk2);
        String encStr = sm2.encryptBcd(text, KeyType.PublicKey);

        System.err.println(encStr);


        //私钥解密
        SM2 sm2obj = SmUtil.sm2();
        sm2obj.setPrivateKey(priK2);
        String decStr = StrUtil.utf8Str(sm2obj.decryptFromBcd(encStr,KeyType.PrivateKey));

        System.err.println(decStr);

    }

}
