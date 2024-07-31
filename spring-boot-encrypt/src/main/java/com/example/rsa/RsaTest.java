package com.example.rsa;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.rsa.hutool.RsaUtils;
import com.example.rsa.hutool.SplicingUtil;

import java.util.TreeMap;

/**
 * @Author: wgs
 * @Date 2022/3/4 09:19
 * @Classname RsaTest
 * @Description
 */
@SuppressWarnings("all")
public class RsaTest {
    // 公钥
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF3BobSZKBdvfsaCiVH9WZ95F45XxVMr51jmH/YKhB2kIawGaSW0oujnTXF3WcpT1iyIBWWNKEpakvqQaXKOz/8UVB3IS7XfD0QeiBO0hHuSAtAiyfALqMjszusxh+8x2SdQc82IruT9nIj5VeXKG4WdZT21bxNzj35/TogUmxSQIDAQAB";
    // 私钥
    private static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIXcGhtJkoF29+xoKJUf1Zn3kXjlfFUyvnWOYf9gqEHaQhrAZpJbSi6OdNcXdZylPWLIgFZY0oSlqS+pBpco7P/xRUHchLtd8PRB6IE7SEe5IC0CLJ8AuoyOzO6zGH7zHZJ1BzzYiu5P2ciPlV5cobhZ1lPbVvE3OPfn9OiBSbFJAgMBAAECgYBnlfLR6PYpn4GtfNDizXbDJfR55MR5PcJrQBqPwCGUjCyZfQgi++gE7RJ3nFzUMn61YckGiMeoBHZPPnKuKOp057P2LjBngye+DAGDMg1YBbhvKXeLd3nVmuZ1KQsND/KJ3BKag410JbeyE+CNWIXFdMStDL2+gAhYkslGT9xjgQJBANWXHiAcQ5rGHhK0lmykotyE+kd1CgLFEkqkTwZ63qH9rjlI2CZTj0PJmswn2OVPzgUTVDBCZrrkJsjxCP3xsfECQQCgcD+4Zaw/KmgVKxuWIE89iDgOKLQykNLgf9RZy45H8x0DX1cKgLUcjNc17JvtshtNYt0sqKHwXw0Ast7Lq5zZAkAgcbMGx6RRR1TcHIhY+m0uuBhYcGPT5eGQawMCBfn5e1JSq0kCEWFKt74G0uq0cnnl9XitV/jI3M0zasGl6WfRAkEAixJiGcyAb8ypplfUtODS72qp9JQSB5cERLhp8WCCuBmkN60oPSFuXQ406zhrvbJa+kzNng2bOkqacdWbTP84mQJAYo0PaJBYprBhHi22ewA7CD/K+/NkndPTanKt1n/0yertQyC2wl41fo+B+sh5pFNqP2P5HV4YgxP3iawklpb1bw==";

    public static void main(String[] args) throws Exception {

//        buildSimple();

//        buildRequestBody();

        jia();
    }

    public static void jia(){

        RSA rsa = new RSA(PRIVATE_KEY,PUBLIC_KEY);

        String text = "我是一段测试aaaa";

        // 私钥加密，公钥解密
        String encrypt2 = rsa.encryptBcd(text, KeyType.PrivateKey);
        String decrypt2 = StrUtil.utf8Str(rsa.decryptFromBcd(encrypt2, KeyType.PublicKey));

        System.out.println("***************** 私钥加密公钥解密开始 *****************");

        System.out.println("加密前：" + text);
        System.out.println("加密后：" + encrypt2);
        System.out.println("解密后：" + decrypt2);
        if (text.equals(decrypt2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 私钥加密公钥解密结束 *****************");



            // 1. 利用空构造器的RSA获取Base64位的publicKey, privateKey
//            RSA rsa = new RSA();
            String privateKeyBase64 = rsa.getPrivateKeyBase64();
            String publicKeyBase64 = rsa.getPublicKeyBase64();

            // 2. 根据公钥生成密文
            RSA rsa1 = new RSA(null, publicKeyBase64);
            byte[] encrypt = rsa1.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
            String hexStr = HexUtil.encodeHexStr(encrypt);

            // 3. 根据私钥解密密文
            RSA rsa2 = new RSA(privateKeyBase64, null);
            byte[] decodeHex = HexUtil.decodeHex(hexStr);
            byte[] decrypt1 = rsa2.decrypt(decodeHex, KeyType.PrivateKey);
            System.out.println(StrUtil.str(decrypt1, CharsetUtil.CHARSET_UTF_8));
            System.out.println(StrUtil.str(decodeHex, CharsetUtil.CHARSET_UTF_8));

    }

    private static void buildSimple() throws Exception {
        // 基础参数
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("mobile", "18806513871");
//        paramMap.put("contactName", "刘备");
//        paramMap.put("contactPhone", "18325648012");
//        paramMap.put("identifyNumber", "91330100MA2KH64H27");
//        paramMap.put("managerId", "340824199001012223");
//        paramMap.put("managerName", "张三");

        StringBuffer param = new StringBuffer();
        SplicingUtil.mapToString(paramMap, param);
        // 拼接参数
        String urlParams = param.toString();
        System.out.println("拼接参数: " + urlParams);
        String jsonParams = JSON.toJSONString(paramMap);

        // ↓↓↓↓↓↓ 引用hutool工具类  ↓↓↓↓↓↓
        // 加签
        String sign = RsaUtils.generateSign(jsonParams, PRIVATE_KEY);
        paramMap.put("sign", sign);
        System.out.println("签名: " + sign);
        boolean doCheck = RsaUtils.verifySign(jsonParams, sign, PUBLIC_KEY);
        System.out.println("验签: " + doCheck);


        // ↓↓↓↓↓↓ 非hutool工具类  ↓↓↓↓↓↓
        String rasSign = RSASign.sign(jsonParams, PRIVATE_KEY);
        paramMap.put("sign", rasSign);
        System.out.println("签名: " + rasSign);
        final boolean checkSign = RSASign.checkSign(jsonParams, sign, PUBLIC_KEY);
        System.out.println("验签: " + checkSign);

    }

    private static void buildRequestBody() {
        JSONObject json = JSONObject.parseObject(initParams());
        JSONObject reqParams = json;
        System.out.println("待签字符串: " + reqParams);

        StringBuffer param = new StringBuffer();
        SplicingUtil.mapToString(JSONObject.parseObject(reqParams.toJSONString(), TreeMap.class), param);

        System.out.println("待签字符串:"+param);
        json.put("sign", RsaUtils.generateSign(reqParams.toJSONString(), PRIVATE_KEY));

        // 工保网请求保险公司最终报文
        String url = "保司接口";
        // 请求数据
        System.out.println("请求报文: " + json);
        //System.out.println("相应报文: " + HttpUtil.post(url, json));
        System.out.println("========结束=======");

        // ↓↓↓↓↓↓ 保险公司接受工保网请求报文，并且验签  ↓↓↓↓↓↓
        System.out.println(JSONObject.toJSONString(json, true));
        JSONObject body = json.getJSONObject("body");
        String sign = json.getString("sign");

        System.out.println("验签结果: " + RsaUtils.verifySign(body.toJSONString(), sign, PUBLIC_KEY));
    }


    /**
     * 投保待签名数据
     *
     * @return
     */
    private static String initParams() {
        String json = "{\"OnBlacklist\":false,\"RecThrYrExecuted\":true,\"RecThrYrJudgementDebtor\":true,\"auditResult\":\"自动审核\",\"auditState\":\"2\",\"executedInformation\":true,\"insurances\":[{\"childPolicyNumber\":\"1597774203344457730\",\"insuranceCode\":\"C011\",\"insuranceName\":\"天安财产保险股份有限公司\",\"pdfUrl\":\"pdf/zl/20221202_ce585ba4ea2641ecb8723874f2d861ed.pdf\",\"serialVersionUID\":1}],\"isTouchInsuranceAuto\":false,\"penaltiesRecords\":true,\"policyNumber\":\"1597774203344457730\",\"qyxzgxf\":true,\"riskControlNumber\":\"GBJZ-01-01-03-2022-12-02-005709\",\"serialVersionUID\":1}";
        return json;
    }
}
