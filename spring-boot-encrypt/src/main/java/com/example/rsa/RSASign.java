package com.example.rsa;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSONObject;
import com.example.rsa.hutool.SplicingUtil;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.TreeMap;

/**
 * 非hutool
 */
public class RSASign {
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE_RSA = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA256withRSA";

    /**
     * 使用私钥生成签名字符串
     *
     * @param signContent 待签名参数集合
     * @param privateKey  私钥原始字符串
     * @return 签名结果字符串
     * @throws Exception
     */
    public static String sign(String signContent, String privateKey) throws Exception {
        //参数值
        StringBuffer param = new StringBuffer();
        //循环拼接参数
        SplicingUtil.mapToStringByJackson(JSONObject.parseObject(signContent, TreeMap.class), param);

        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(getPrivateKeyPKCS8(privateKey));
        signature.update(param.toString().getBytes(CHARSET));
        byte[] signed = signature.sign();

        return new String(Base64.getEncoder().encode(signed));
    }

    /**
     * 使用公钥校验签名
     *
     * @param json      入参数据
     * @param signData  签名
     * @param publicKey 公钥原始字符串
     * @return true 验签通过 | false 验签不通过
     * @throws Exception
     */
    public static boolean checkSign(String json, String signData, String publicKey) throws Exception {

        //参数值
        StringBuffer param = new StringBuffer();
        //循环拼接参数
        SplicingUtil.mapToStringByJackson(JSONObject.parseObject(json, TreeMap.class), param);
        // verify
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initVerify(getPublicKeyX509(publicKey));
        signature.update(param.toString().getBytes(CHARSET));

        return signature.verify(Base64.getDecoder().decode(signData.getBytes(CHARSET)));
    }


    /**
     * 将公钥字符串进行Base64 decode之后，生成X509标准公钥
     *
     * @param publicKey 公钥原始字符串
     * @return X509标准公钥
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private static PublicKey getPublicKeyX509(String publicKey) throws InvalidKeySpecException,
            NoSuchAlgorithmException, UnsupportedEncodingException {
        if (StringUtils.isEmpty(publicKey)) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
        byte[] decodedKey = Base64.getDecoder().decode(publicKey.getBytes(CHARSET));
        return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
    }

    /**
     * 将私钥字符串进行Base64 decode之后，生成PKCS #8标准的私钥
     *
     * @param privateKey 私钥原始字符串
     * @return PKCS #8标准的私钥
     * @throws Exception
     */
    private static PrivateKey getPrivateKeyPKCS8(String privateKey) throws Exception {
        if (StringUtils.isEmpty(privateKey)) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance(SIGN_TYPE_RSA);
        byte[] decodedKey = Base64.getDecoder().decode(privateKey.getBytes(CHARSET));
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    }


    /**
     * 公钥解密
     *
     * @param publicKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String publicKeyText, String text) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyText.getBytes(CHARSET));
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(org.apache.tomcat.util.codec.binary.Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyText.getBytes(CHARSET));
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String privateKeyText, String text) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyText.getBytes(CHARSET));
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.getDecoder().decode(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyText
     * @param text
     * @return
     */
    public static String encryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }

    private static void test2(String  privateKey, String  publicKey, String source) throws Exception {
        System.out.println("***************** 私钥加密公钥解密开始 *****************");
        String text1 = encryptByPrivateKey(privateKey, source);
        String text2 = decryptByPublicKey(publicKey, text1);
        System.out.println("加密前：" + source);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (source.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 私钥加密公钥解密结束 *****************");
    }
// 加密后：CWzU4ma0NhV73AA6wOl1pp0wnhLS2+8SjbXZE5up+vH2wbit4JqpcgfNFMO7vmnsUILaJ05MaatEDYb22CpXKCWsw1Hlz4ym84jbqSCYGy6gffCRc69RdhMoJQVFL2PyjTSWTiuXSBvmz3TDaYIyepDiwGZIcIm2ckM9oVjt0/U=

    private static final String src = "加密文件数据1414313414";

    // 公钥
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF3BobSZKBdvfsaCiVH9WZ95F45XxVMr51jmH/YKhB2kIawGaSW0oujnTXF3WcpT1iyIBWWNKEpakvqQaXKOz/8UVB3IS7XfD0QeiBO0hHuSAtAiyfALqMjszusxh+8x2SdQc82IruT9nIj5VeXKG4WdZT21bxNzj35/TogUmxSQIDAQAB";
    // 私钥
    private static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIXcGhtJkoF29+xoKJUf1Zn3kXjlfFUyvnWOYf9gqEHaQhrAZpJbSi6OdNcXdZylPWLIgFZY0oSlqS+pBpco7P/xRUHchLtd8PRB6IE7SEe5IC0CLJ8AuoyOzO6zGH7zHZJ1BzzYiu5P2ciPlV5cobhZ1lPbVvE3OPfn9OiBSbFJAgMBAAECgYBnlfLR6PYpn4GtfNDizXbDJfR55MR5PcJrQBqPwCGUjCyZfQgi++gE7RJ3nFzUMn61YckGiMeoBHZPPnKuKOp057P2LjBngye+DAGDMg1YBbhvKXeLd3nVmuZ1KQsND/KJ3BKag410JbeyE+CNWIXFdMStDL2+gAhYkslGT9xjgQJBANWXHiAcQ5rGHhK0lmykotyE+kd1CgLFEkqkTwZ63qH9rjlI2CZTj0PJmswn2OVPzgUTVDBCZrrkJsjxCP3xsfECQQCgcD+4Zaw/KmgVKxuWIE89iDgOKLQykNLgf9RZy45H8x0DX1cKgLUcjNc17JvtshtNYt0sqKHwXw0Ast7Lq5zZAkAgcbMGx6RRR1TcHIhY+m0uuBhYcGPT5eGQawMCBfn5e1JSq0kCEWFKt74G0uq0cnnl9XitV/jI3M0zasGl6WfRAkEAixJiGcyAb8ypplfUtODS72qp9JQSB5cERLhp8WCCuBmkN60oPSFuXQ406zhrvbJa+kzNng2bOkqacdWbTP84mQJAYo0PaJBYprBhHi22ewA7CD/K+/NkndPTanKt1n/0yertQyC2wl41fo+B+sh5pFNqP2P5HV4YgxP3iawklpb1bw==";


    public static void main(String[] args) throws Exception {
        System.out.println("\n");
//        RSAKeyPair keyPair = generateKeyPair();

        test2(PRIVATE_KEY,PUBLIC_KEY, src);
        System.out.println("\n");



        // hutool加密
        RSA rsa = SecureUtil.rsa(PRIVATE_KEY, PUBLIC_KEY);
        // 私钥加密，公钥解密
        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("加密文件数据1414313414", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);

        System.out.println("加密后：" +   org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(encrypt2));
        System.out.println( StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
        System.out.println( cn.hutool.core.codec.Base64.encode(decrypt2));

    }

}
