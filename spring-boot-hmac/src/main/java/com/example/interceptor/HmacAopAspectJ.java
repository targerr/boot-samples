package com.example.interceptor;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.enums.EncryptField;
import com.example.manager.CryptoEndpointManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 处理加密
 */
@Slf4j
@Aspect
@Component
@Order(200)
public class HmacAopAspectJ {
    @Autowired
    private CryptoEndpointManager cryptoEndpointManager;

    /**
     * 环绕增强
     */
    @Around("@annotation(encryptField)")
    public Object around(ProceedingJoinPoint joinPoint, EncryptField encryptField) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (ArrayUtil.isEmpty(args) || args[0] == null) {
            return joinPoint.proceed();
        }

        String[] keys = encryptField.keys();
        if (ArrayUtil.isEmpty(keys)) {
            return joinPoint.proceed();
        }

        Set<String> keySet = Arrays.stream(keys)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        if (keySet.isEmpty()) {
            return joinPoint.proceed();
        }

        Object originalArg = args[0];
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(originalArg));

        // 加密字段
        keySet.forEach(key -> {
            String value = jsonObject.getString(key);
            if (StringUtils.isNotBlank(value)) {
                jsonObject.put(key, cryptoEndpointManager.getEncryptedText(value));
            }
        });

        // 构建 HMAC 参数 Map
        Map<String, String> hmacParamMap = keySet.stream()
                .filter(jsonObject::containsKey)
                .collect(Collectors.toMap(
                        t -> t,
                        key -> Optional.ofNullable(jsonObject.getString(key)).orElse("")
                ));

        // 生成并添加 HMAC
        String hmacString = CryptoEndpointManager.generateHmacString(hmacParamMap);
        String hmac = cryptoEndpointManager.getHashValue(hmacString);
        jsonObject.put("hmac", hmac);

        // 参数反序列化为原类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> paramClass = signature.getParameterTypes()[0];
        args[0] = JSONObject.toJavaObject(jsonObject, paramClass);

        return joinPoint.proceed(args);
    }


}
