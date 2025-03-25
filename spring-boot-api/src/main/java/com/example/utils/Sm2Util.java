package com.example.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.alibaba.fastjson.JSON;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import java.util.HashMap;
import java.util.Map;

public class Sm2Util {
    /**
     * 生成秘钥对
     *
     * @return 公钥和私钥
     */
    public static Map<String, String> generator() {
        SM2 sm2 = SmUtil.sm2();
        String publicKey = HexUtil.encodeHexStr(((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false)).toUpperCase();
        String privateKey = HexUtil.encodeHexStr(BCUtil.encodeECPrivateKey(sm2.getPrivateKey())).toUpperCase();
        return new HashMap<String, String>(2) {{
            put("publicKey", publicKey);
            put("privateKey", privateKey);
        }};
    }

    /**
     * 加密
     *
     * @param publicKey 公钥
     * @param data      明文
     * @return 密文
     */
    public static String encrypt(String publicKey, String data) {
        return SmUtil.sm2(null, publicKey)
                .setMode(SM2Engine.Mode.C1C3C2)
                .encryptHex(data.getBytes(), KeyType.PublicKey)
                .substring(2);
    }

    /**
     * 解密
     *
     * @param privateKey 私钥
     * @param data       密文
     * @return 明文
     */
    public static String decrypt(String privateKey, String data) {

        data = "04" + data;
        return SmUtil.sm2(privateKey, null)
                .setMode(SM2Engine.Mode.C1C3C2)
                .decryptStr(data, KeyType.PrivateKey);
    }

    public static void main(String[] args) {
        String guid = IdUtil.fastSimpleUUID();
        System.out.println(guid);
        System.out.println(IdUtil.getSnowflakeNextIdStr());

        System.out.println(JSON.toJSONString(generator()));
    }

}
