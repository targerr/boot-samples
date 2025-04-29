package com.example.manager;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.symmetric.SM4;
import com.example.config.EncryptProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2025/4/11 09:29
 * @Classname CryptoEndpointManager
 * @Description
 */
@Component
public class CryptoEndpointManager {
    @Autowired
    private EncryptProperties encryptProperties;

    public static final String HMAC_PASSWORD = "14afqwafa";
    public static String algorithmName = "SM4/CTR/PKCS5PADDING";
    public static String key = "mbnfq0dwgzltcilw";
    public static String iv = "viti1sy1pza7zv2d";

    /**
     * 明文加密
     *
     * @param plainText
     * @return
     */
    public String getEncryptedText(String plainText) {
        if (StrUtil.isBlank(plainText) || !isEnabled()) {
            return plainText;
        }

        SM4 sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, key.getBytes(),iv.getBytes());
        return sm4.encryptHex(plainText);
    }
    /**
     * 密文解密
     *
     * @param encryptedText
     * @return
     */
    public String getDecryptedText(String encryptedText) {
        if (StrUtil.isBlank(encryptedText) || !isEnabled() ) {
            return encryptedText;
        }
        if (encryptedText.length() != 32){
            return encryptedText;
        }
        SM4 sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, key.getBytes(),iv.getBytes());
        return sm4.decryptStr(encryptedText);
    }


    /**
     * hash值
     *
     * @param plainText
     * @return
     */
    public String getHashValue(String plainText) {
        if (StrUtil.isBlank(plainText) || !isEnabled()) {
            return plainText;
        }

        byte[] key = HMAC_PASSWORD.getBytes();
        HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);
        return  mac.digestHex(plainText);
    }

    public Boolean isEnabled() {
        return encryptProperties.getInterceptor() != null && encryptProperties.getInterceptor().isEnabled();
    }

    public Boolean isHmacEnabled() {
        return encryptProperties.getInterceptor() != null && encryptProperties.getInterceptor().isHmac();
    }


    /**
     * 判断是否是密文
     *
     * @param text
     * @return
     */
    public boolean isEncrypted(String text) {
        return StrUtil.containsAny(text, StrPool.COLON, StrPool.DASHED, StrPool.SLASH);

    }

    /**
     * 根据传入的字段和值进行字典排序
     *
     * @param fields 字段及对应值
     * @return 排序后拼接的字符串
     */
    public static String generateHmacString(Map<String, String> fields) {
        // 如果只有一个字段，不做排序
        if (fields.size() == 1) {
            return fields.values().iterator().next();
        }

        Map<String, String> sortedFields = new TreeMap<>(fields);
        return sortedFields.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }
}
