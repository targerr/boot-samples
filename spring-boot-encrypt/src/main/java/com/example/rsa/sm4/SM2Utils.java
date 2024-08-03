package com.example.rsa.sm4;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2024/8/3 11:27
 * @Classname SM2Utils
 * @Description
 * 原文链接：https://blog.csdn.net/thinkpet/article/details/131316040*
 */
public class SM2Utils {
    static final BouncyCastleProvider bc = new BouncyCastleProvider();

    public static Map<String,Object> generateKey(){
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        Map<String,Object> map = new HashMap<>();
        map.put("publicKey",pair.getPublic());
        map.put("privateKey",pair.getPrivate());
        return map;
    }

    public static String encrypt(String body, PublicKey aPublic){
        SM2 sm2 = SmUtil.sm2();
        sm2.setPublicKey(aPublic);
        String s = sm2.encryptBcd(body, KeyType.PublicKey);
        return s;
    }

    public static String decrypt(String data, PrivateKey privateKey){
        SM2 sm2obj = SmUtil.sm2();
        sm2obj.setPrivateKey(privateKey);
        String decStr = StrUtil.utf8Str(sm2obj.decryptFromBcd(data,KeyType.PrivateKey));
        return decStr;
    }


    /**
     * 从字符串中读取 私钥 key
     * @param privateKeyStr String
     * @return PrivateKey
     */
    public static PrivateKey strToPrivateKey(String privateKeyStr){
        PrivateKey privateKey = null;
        try {
            byte[] encPriv = Base64.decode(privateKeyStr);
            KeyFactory keyFact = KeyFactory.getInstance("EC", bc);
            privateKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(encPriv));

        }catch (Exception e){
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 从字符串中读取 公钥 key
     * @param publicKeyStr String
     * @return PublicKey
     */
    public  static PublicKey strToPublicKey(String publicKeyStr){
        PublicKey publicKey =  null;
        try {
            byte[] encPub = Base64.decode(publicKeyStr);
            KeyFactory keyFact = KeyFactory.getInstance("EC", bc);
            publicKey = keyFact.generatePublic(new X509EncodedKeySpec(encPub));
        }catch (Exception e){
            e.printStackTrace();
        }
        return publicKey;
    }


    /**
     * 公钥 key  转文件
     * @param publicKey PublicKey
     * @param path String
     */
    public static void exportPublicKey(PublicKey publicKey,String path){
        File file = new File(path);
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            byte[] encPub = publicKey.getEncoded();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(encPub);
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 私钥 key 转文件
     * @param privateKey PrivateKey
     * @param keyPath String
     */
    public static void exportPrivateKey(PrivateKey privateKey, String keyPath){
        File file = new File(keyPath);
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            byte[]  encPriv = privateKey.getEncoded();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(encPriv);
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static PublicKey importPublicKey(String path){
        File file = new File(path);
        try {
            if (!file.exists()){
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[16];
            int size;
            while ((size = fis.read(buffer)) != -1){
                baos.write(buffer,0,size);
            }
            fis.close();
            byte[] bytes = baos.toByteArray();
            String publicKeyStr = Base64.encode(bytes);

            return strToPublicKey(publicKeyStr);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }


    public static PrivateKey importPrivateKey(String keyPath){
        File file = new File(keyPath);
        try {
            if (!file.exists()){
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buffer[] = new byte[16];
            int size;
            while ((size = fis.read(buffer)) != -1){
                baos.write(buffer,0,size);
            }
            fis.close();

            byte[]  bytes = baos.toByteArray();
            String privateKeyStr = Base64.encode(bytes);

            return strToPrivateKey(privateKeyStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



}
