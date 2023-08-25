package com.example.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: wgs
 * @Date 2023/8/25 10:10
 * @Classname AliyuncsTokenController
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/")
class AliTokenController {

    private static final String DEFAULT_DIR = "default";

    @Resource
    private OSS oss;

    @PostMapping("/get-token")
    public JSONObject getToken() {

        // 过期时间默认5分钟
        Date expiration = DateUtil.offsetMinute(new Date(),5);


        try {
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem("bucket", "bucket值");
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1024 * 1024 * 1024);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, "dir文件夹");
            String postPolicy = oss.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = oss.calculatePostSignature(postPolicy);

        }catch (Exception e) {
            log.error("生成临时签名异常", e);
            throw new RuntimeException( "生成临时签名异常");
        }


        return new JSONObject();
    }

}
