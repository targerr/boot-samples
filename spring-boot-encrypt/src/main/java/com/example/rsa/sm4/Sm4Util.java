package com.example.rsa.sm4;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

/**
 * @Author: wgs
 * @Date 2024/8/3 10:42
 * @Classname Sm4Util
 * @Description
 */
public class Sm4Util {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final String ALGORITHM_NAME = "SM4";
    public static final String DEFAULT_KEY = "random_seed";
    public static final String DEFAULT_ALGORITHM = "SM4/ECB/PKCS5PADDING";

    /**
     * 128-32位16进制；256-64位16进制
     */
    public static final Integer DEFAULT_KEY_SIZE = 128;

    public static byte[] generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        return generateKey(DEFAULT_KEY, DEFAULT_KEY_SIZE);
    }

    public static byte[] generateKey(String seed) throws NoSuchAlgorithmException, NoSuchProviderException {
        return generateKey(seed, DEFAULT_KEY_SIZE);
    }

    public static String getKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        return Base64.getEncoder().encodeToString(generateKey(DEFAULT_KEY, DEFAULT_KEY_SIZE));
    }

    public static byte[] generateKey(String seed, Integer keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        if (null != seed && !"".equals(seed)) {
            random.setSeed(seed.getBytes());
        }
        kg.init(keySize, random);
        return kg.generateKey().getEncoded();
    }

    /**
     * 加密
     *
     * @param algorithmName 算法名称
     * @param key           加密key
     * @param iv            加密向量
     * @param data          家居数据
     * @return byte[]
     * @throws Exception 异常
     */
    public static byte[] encrypt(String algorithmName, byte[] key, byte[] iv, byte[] data) throws Exception {
        return sm4core(algorithmName, Cipher.ENCRYPT_MODE, key, iv, data);
    }

    /**
     * 默认ECB加密
     *
     * @param key
     * @param data
     * @return
     */
    public static String encrypt(String key, String data) throws Exception {
        return encryptToBase64String(DEFAULT_ALGORITHM, Base64.getDecoder().decode(key), null, data.getBytes());
    }

    /**
     * 加密
     *
     * @param algorithmName 算法名称
     * @param key           加密key
     * @param iv            加密向量
     * @param data          家居数据
     * @return String Base64String
     * @throws Exception 异常
     */
    public static String encryptToBase64String(String algorithmName, byte[] key, byte[] iv, byte[] data) throws Exception {
        byte[] bytes = sm4core(algorithmName, Cipher.ENCRYPT_MODE, key, iv, data);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 解密
     *
     * @param algorithmName 算法名称
     * @param key           加密key
     * @param iv            加密向量
     * @param data          加密数据
     * @return byte[]
     * @throws Exception 异常
     */
    public static byte[] decrypt(String algorithmName, byte[] key, byte[] iv, byte[] data) throws Exception {
        return sm4core(algorithmName, Cipher.DECRYPT_MODE, key, iv, data);
    }

    /**
     * ECB解密
     *
     * @param key
     * @param data
     * @return
     */
    public static String decrypt(String key, String data) throws Exception {
        return new String(decryptFromBase64String(DEFAULT_ALGORITHM, Base64.getDecoder().decode(key), null, data));
    }

    /**
     * 解密
     *
     * @param algorithmName 算法名称
     * @param key           加密key
     * @param iv            加密向量
     * @param base64String  加密数据base64
     * @return String
     * @throws Exception 异常
     */
    public static byte[] decryptFromBase64String(String algorithmName, byte[] key, byte[] iv, String base64String) throws Exception {
        byte[] decode = Base64.getDecoder().decode(base64String);
        return sm4core(algorithmName, Cipher.DECRYPT_MODE, key, iv, decode);
    }

    private static byte[] sm4core(String algorithmName, Integer type, byte[] key, byte[] iv, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        if (algorithmName.contains("/ECB/")) {
            cipher.init(type, sm4Key);
        } else {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(type, sm4Key, ivParameterSpec);
        }
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {
        String json = "{\n" +
                "\t\"dataInfo\":{\n" +
                "\t\t\"contactName\":\"茅全勇\",\n" +
                "\t\t\"files\":[\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"fileName\":\"测试文件.pdf\",\n" +
                "\t\t\t\t\"fileUrl\":\"http://172.16.200.202:9000/nmg/dev/330300/2024/01/22/8e95100dab334115a6e7027c44393b99.pdf\",\n" +
                "\t\t\t\t\"fileId\":1809035302853783552\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"fileName\":\"cs.pdf\",\n" +
                "\t\t\t\t\"fileUrl\":\"http://172.16.200.202:9000/nmg/dev/330300/2024/01/22/26f2f9855c0141d5965df7cad62d24e5.pdf\",\n" +
                "\t\t\t\t\"fileId\":1749318552873357313\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"contactPhone\":\"13202630638\",\n" +
                "\t\t\"socialCreditCode\":\"24427218FX8FQTDMBE\",\n" +
                "\t\t\"enterpriseName\":\"测试企业\"\n" +
                "\t},\n" +
                "\t\"appKey\":\"viti1sy1pza7zv2d\",\n" +
                "\t\"nonce\":\"1c5914e1b64045b69c9e25a2bdfea1d8\",\n" +
                "\t\"timestamp\":1722653646396\n" +
                "}";
//        String key = RandomUtil.randomString(16);

//        Sm4算法名称：SM4/CTR/PKCS5PADDING
//        Sm4加密密钥:mbnfq0dwgzltcilw
//        Sm4加密向量:viti1sy1pza7zv2d

        String key = "mbnfq0dwgzltcilw";
        String iv = "viti1sy1pza7zv2d";
        String algorithmName = "SM4/CTR/PKCS5PADDING";

        String encrypt = Sm4Util.encryptToBase64String(
                algorithmName, key.getBytes(), iv.getBytes(),  json.getBytes()
        );
//        String encrypt = encrypt(key,json);

        System.out.println(encrypt);

                   // vqCNze6DBXtBLTEKwnkycE985yVQ3TJDLOGTGVKUPz7KsKofjHCFiUMnpIn12wJ2eQVsA+8im1JVCH4drhvI8NsrOs/UMTIaAlMvRrI59kOeYbC3eQm70Wc/MHI5c3lcIjXrkdVZYSVuVEkQUDcRrpn34jJc0CUoY9ORxvFOw92VRl1ipXIo8QmpQzDiyy80M89ZI378i+Ve5Dchg9TMQS5IMphL9vi6biPIwjxYz29cGrtX8XTe8nZ4vaVKFUWf9XCY6hgyWdaSAER/PPWINOR2m1JuJUYhAFpLYKn/aOp/escJGP6hDjFZgJEpBZiqUU+T8U2holbWVf0u2+OO0StLPgobMXcoIwTOy6DBOJcT0yo6nmjcqfHObFwCrG0cGHrM57Tf6abjOV8p9K7OXJA13oq3zrjzcgdb5KGft/tchKlqbGTHwGMlUQ6mm0BhvPuP2vj+aznwDbMKeajUDaSckh0xAILEpXNG9k8XEs7nZ8DtgkIJWHt11m6rBsQ+JluaYTiTi1k8+77sKc6ltOxA8RT+UA9mIHLcMTL3Bw4l1IzZaxmugBgYnQodmkSjvSpErk88xhIhJZzsUHIw/3qXfZIETG8rC/DZF4y0VhMXVo/LJCYhFflgGpNM/Sb/2Eo8MLUwXHjNUzPTxbmOeUz+Vhnpa7r6NClcfdhuNrhla3KdhC2lDINWTBcBZ/IUvpYo+lfbt7ObS+ImmhM4yD9L/hZnN51nuGw2uSBg8MA2ZSuFTU9qpuQsXXGwBgZJrw5A6sPKjSKycZeZyhO0nuAj/CF3ykioFpB77Ip/WMXHLybiouKxe870pMoCKLJAnT4CCCrznW75sSJjFHdyNQ==
        String data = "vqCNze6DBXtBLTEKwnkycE985yVQ3TJDLOGTGVKUPz7KsKofjHCFiUMnpIn12wJ2eQVsA+8im1JVCH4drhvI8NsrOs/UMTIaAlMvRrI59kOeYbC3eQm70Wc/MHI5c3lcIjXrkdVZYSVuVEkQUDcRrpn34jJc0CUoY9ORxvFOw92VRl1ipXIo8QmpQzDiyy80M89ZI378i+Ve5Dchg9TMQS5IMphL9vi6biPIwjxYz29cGrtX8XTe8nZ4vaVKFUWf9XCY6hgyWdaSAER/PPWINOR2m1JuJUYhAFpLYKn/aOp/escJGP6hDjFZgJEpBZiqUU+T8U2holbWVf0u2+OO0StLPgobMXcoIwTOy6DBOJcT0yo6nmjcqfHObFwCrG0cGHrM57Tf6abjOV8p9K7OXJA13oq3zrjzcgdb5KGft/tchKlqbGTHwGMlUQ6mm0BhvPuP2vj+aznwDbMKeajUDaSckh0xAILEpXNG9k8XEs7nZ8DtgkIJWHt11m6rBsQ+JluaYTiTi1k8+77sKc6ltOxA8RT+UA9mIHLcMTL3Bw4l1IzZaxmugBgYnQodmkSjvSpErk88xhIhJZzsUHIw/3qXfZIETG8rC/DZF4y0VhMXVo/LJCYhFflgGpNM/Sb/2Eo8MLUwXHjNUzPTxbmOeUz+Vhnpa7r6NClcfdhuNrhla3KdhC2lDINWTBcBZ/IUvpYo+lfbt7ObS+ImmhM4yD9L/hZnN51nuGw2uSBg8MA2ZSuFTU9qpuQsXXGwBgZJrw5A6sPKjSKycZeZyhO0nuAj/CF3ykioFpB77Ip/WMXHLybiouKxe870pMoCKLJAnT4CCCrznW75sSJjFHdyNQ==";

//        final byte[] decrypt = Sm4Util.decrypt(algorithmName, key.getBytes(), iv.getBytes(), data.getBytes());
         byte[] decrypt = Sm4Util.decryptFromBase64String(algorithmName, key.getBytes(), iv.getBytes(), "vqCNze6DBXtBLTEKwnkycE985yVQ3TJDLOGTGVKUPz7KsKofjHCFiUMnpIn12wJ2eQVsA+8im1JVCH4drhvI8NsrOs/UMTIaAlMvRrI59kOeYbC3eQm70Wc/MHI5c3lcIjXrkdVZYSVuVEkQUDcRrpn34jJc0CUoY9ORxvFOw92VRl1ipXIo8QmpQzDiyy80M89ZI378i+Ve5Dchg9TMQS5IMphL9vi6biPIwjxYz29cGrtX8XTe8nZ4vaVKFUWf9XCY6hgyWdaSAER/PPWINOR2m1JuJUYhAFpLYKn/aOp/escJGP6hDjFZgJEpBZiqUU+T8U2holbWVf0u2+OO0StLPgobMXcoIwTOy6DBOJcT0yo6nmjcqfHObFwCrG0cGHrM57Tf6abjOV8p9K7OXJA13oq3zrjzcgdb5KGft/tchKlqbGTHwGMlUQ6mm0BhvPuP2vj+aznwDbMKeajUDaSckh0xAILEpXNG9k8XEs7nZ8DtgkIJWHt11m6rBsQ+JluaYTiTi1k8+77sKc6ltOxA8RT+UA9mIHLcMTL3Bw4l1IzZaxmugBgYnQodmkSjvSpErk88xhIhJZzsUHIw/3qXfZIETG8rC/DZF4y0VhMXVo/LJCYhFflgGpNM/Sb/2Eo8MLUwXHjNUzPTxbmOeUz+Vhnpa7r6NClcfdhuNrhla3KdhC2lDINWTBcBZ/IUvpYo+lfbt7ObS+ImmhM4yD9L/hZnN51nuGw2uSBg8MA2ZSuFTU9qpuQsXXGwBgZJrw5A6sPKjSKycZeZyhO0nuAj/CF3ykioFpB77Ip/WMXHLybiouKxe870pMoCKLJAnT4CCCrznW75sSJjFHdyNQ==");
      String  dataParam = new String(decrypt);

        System.out.println("解密: " + dataParam);
        System.out.println("解密1: " + StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
        System.out.println( "解密2: " +cn.hutool.core.codec.Base64.encode(decrypt));
    }
}