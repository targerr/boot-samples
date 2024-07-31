package com.example.rsa.hutool;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.TreeMap;

/**
 * hutool
 */
public class RsaUtils {

    /**
     * 验签
     *
     * @param orgData
     * @param signData
     * @param publicKey
     * @return
     */
    public static boolean checkRsa(String orgData, String signData, String publicKey) {
        RSA rsa = SecureUtil.rsa(null, publicKey);
        String data = new String(rsa.decrypt(signData, KeyType.PublicKey));
        if (StringUtils.hasText(data) && orgData.equals(data)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成签名
     *
     * @param json: 参数
     * @return java.lang.String
     * @author sunkailun
     * @DateTime 2018/5/9  下午2:01
     * @email 376253703@qq.com
     * @phone 13777579028
     */
    public static String generateSign(String json, String privateKey) {
        //签名规则
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, privateKey, null);
        //参数值
        StringBuffer param = new StringBuffer();
        //循环拼接参数
        SplicingUtil.mapToString(JSONObject.parseObject(json, TreeMap.class), param);
        //将String转换为byte
        byte[] data = Convert.toStr(param).getBytes();
        //签名
        byte[] signed = sign.sign(data);
        return Base64.encode(signed);
    }


    /**
     * 签名验证
     *
     * @param map:      参数
     * @param signData: 签名
     * @return boolean
     * @author sunkailun
     * @DateTime 2018/5/9  下午1:59
     * @email 376253703@qq.com
     * @phone 13777579028
     */
    public static Boolean verifySign(TreeMap<String, Object> map, String signData, String publicKey) {
        //签名规则
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, null, publicKey);
        //参数值
        StringBuffer param = new StringBuffer();
        //循环拼接参数
        SplicingUtil.mapToString(map, param);
        //将String转换为byte
        byte[] data = Convert.toStr(param).getBytes();
        //验证签名
        Boolean verify = sign.verify(data, Base64.decode(signData));
        //返回
        return verify;
    }


    /**
     * 签名验证
     *
     * @param json:     参数
     * @param signData: 签名
     * @return boolean
     * @author sunkailun
     * @DateTime 2018/5/9  下午1:59
     * @email 376253703@qq.com
     * @phone 13777579028
     */
    public static Boolean verifySign(String json, String signData, String publicKey) {
        //签名规则
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, null, publicKey);
        //参数值
        StringBuffer param = new StringBuffer();
        //循环拼接参数
        SplicingUtil.mapToString(JSONObject.parseObject(json, TreeMap.class), param);
        //将String转换为byte
        byte[] data = Convert.toStr(param).getBytes();
        //验证签名
        Boolean verify = sign.verify(data, Base64.decode(signData));
        //返回
        return verify;
    }


    /**
     * 私钥加密
     * final RSA rsa = new RSA();
     * <p>
     * // 公钥加密，私钥解密
     * byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
     * <p>
     * byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
     * Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
     * <p>
     * // 私钥加密，公钥解密
     * byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
     * byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);*
     *
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String publicKey, String privateKey, String text) throws Exception {
        RSA rsa = SecureUtil.rsa(privateKey, publicKey);
        // 私钥加密，公钥解密
        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes(text, CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);

        return Base64.encode(encrypt2);

    }

    /**
     * 私钥解密
     *
     * @param text
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String publicKey, String privateKey, String text) throws Exception {
        RSA rsa = SecureUtil.rsa(privateKey, publicKey);
        byte[] decrypt = rsa.decrypt(text, KeyType.PublicKey);

        return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);

    }


    /**
     * 公钥解密
     *
     * @param publicKey
     * @param text
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String publicKey, String privateKey, String text) throws Exception {
        // 公钥加密，私钥解密
        RSA rsa = SecureUtil.rsa(privateKey, publicKey);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(text, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);

        return Base64.encode(encrypt);
    }

    /**
     * 公钥解密
     *
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String publicKey, String privateKey, String encrypt) throws Exception {
        // 公钥加密，私钥解密
        RSA rsa = SecureUtil.rsa(privateKey, publicKey);

        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
        return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
    }


}
