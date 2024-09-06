package com.example.rsa.sm4;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2024/8/3 10:43
 * @Classname Test
 * @Description
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("socialCreditCode", "24427218FX8FQTDMBE");
        params.put("enterpriseName", "测试企业");
        params.put("contactName", "茅全勇");
        params.put("contactPhone", "13202630638");
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> fileMap0 = new HashMap<>();
        fileMap0.put("fileId", 1809035302853783552L);
        fileMap0.put("fileName", "测试文件.pdf");
        fileMap0.put("fileUrl", "http://172.16.200.202:9000/nmg/dev/330300/2024/01/22/8e95100dab334115a6e7027c44393b99.pdf");
        mapList.add(fileMap0);
        Map<String, Object> fileMap1 = new HashMap<>();
        fileMap1.put("fileId", 1749318552873357313L);
        fileMap1.put("fileName", "cs.pdf");
        fileMap1.put("fileUrl", "http://172.16.200.202:9000/nmg/dev/330300/2024/01/22/26f2f9855c0141d5965df7cad62d24e5.pdf");
        mapList.add(fileMap1);
        params.put("files", mapList);
        Map<String, Object> finalParams = new HashMap<>();
        finalParams.put("appKey", "viti1sy1pza7zv2d");
        finalParams.put("timestamp", System.currentTimeMillis());
        finalParams.put("nonce", IdUtil.fastSimpleUUID());
        finalParams.put("dataInfo", params);
        System.out.println("加签参数：" + JSONObject.toJSONString(finalParams,true));
        String secret = "mbnfq0dwgzltcilw";


        //需要一个长度为16的字符串 16*8=128 bit
        String key = RandomUtil.randomString(16);
        System.err.println("生成1个128bit的加密key:"+key);
        String encrypt = Sm4Util.encryptToBase64String(
                "SM4/CTR/PKCS5PADDING", secret.getBytes(), key.getBytes(),  JSONObject.toJSONString(finalParams).getBytes()
        );
        System.out.println("sign:" + encrypt);

//        String sign = sign(finalParams, secret);
//        System.out.println("sign:" + sign);
//        boolean verified = verifySign(finalParams, secret, sign);
//        System.out.println("sign verify result:" + verified);
    }
}
