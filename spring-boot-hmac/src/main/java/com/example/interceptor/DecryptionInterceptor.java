package com.example.interceptor;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.config.EncryptProperties;
import com.example.manager.CryptoEndpointManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author lxy
 * 解密拦截器
 */
@Slf4j
@ConditionalOnProperty(name = "encrypt.interceptor.enabled", havingValue = "true")
@Component
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class DecryptionInterceptor implements Interceptor {
    @Autowired
    private EncryptProperties encryptProperties;
    @Autowired
    private CryptoEndpointManager cryptoEndpointManager;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();

        if (result instanceof List) {
            log.info("拦截查询结果 (List) - 处理批量数据解密");
            for (Object entity : (List<?>) result) {
                decryptAndVerifyData(entity);
            }
        } else if (result instanceof Page) {
            log.info("拦截查询结果 (Page) - 处理分页数据解密");
            Page<?> page = (Page<?>) result;
            if (page.getRecords() != null) {
                for (Object entity : page.getRecords()) {
                    decryptAndVerifyData(entity);
                }
            }
        } else if (result != null) {
            log.info("拦截查询结果 (单条数据) - 处理单条数据解密");
            decryptAndVerifyData(result);
        } else {
            log.warn("拦截查询结果为空，跳过解密");
        }
        return result;
    }

    private void decryptAndVerifyData(Object entity) {
        if (entity == null) {
            return;
        }

        String tableName = getTableName(entity.getClass());
        Map<String, List<String>> tableFieldMap = encryptProperties.getTableFieldMap();

        if (!tableFieldMap.containsKey(tableName)) {
            return;
        }

        List<String> encryptFields = tableFieldMap.get(tableName);
        if (CollectionUtils.isEmpty(encryptFields)) return;

        Map<String, String> hashMap = new TreeMap<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (encryptFields.contains(field.getName())) { // 只解密配置的字段
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    if (value instanceof String && !PhoneUtil.isPhone(value.toString()) && !IdcardUtil.isValidCard(value.toString())) {
                        log.info("解密字段: {}.{} -> {}", tableName, field.getName(), value);
                        String decryptedValue = cryptoEndpointManager.getDecryptedText(value.toString());

                        if (decryptedValue != null) {
                            field.set(entity, decryptedValue);
                            log.info("解密后: {}.{} -> {}", tableName, field.getName(), decryptedValue);
                            hashMap.put(field.getName(), decryptedValue);
                        } else {
                            log.warn("解密失败，保持原始值: {}", value);
                        }
                    }
                } catch (Exception e) {
                    log.error("解密字段 {}.{} 失败: {}", tableName, field.getName(), e.getMessage(), e);
                }
            }
        }

        // 完整性校验 (HMAC)
//        checkHmac(entity, hashMap, tableName);
    }


    private void checkHmac(Object entity, Map<String, String> hashMap, String tableName) {
        if (CollectionUtils.isEmpty(hashMap)) {
            return;
        }
        try {   // 获取表中的 HMAC 校验字段
            String hmacFieldName = "hmac";
            Field hmacField = entity.getClass().getDeclaredField(hmacFieldName);
            hmacField.setAccessible(true);
            Object dbHash = hmacField.get(entity);

            // 使用生成字典排序字符串的方法
            String hmacString = generateHmacString(hashMap);

            String hmac = cryptoEndpointManager.getHashValue(hmacString);

            if (dbHash instanceof String) {
                if (!StringUtils.equals(hmac, (String) dbHash)) {
                    log.error("完整性校验失败: {}", tableName);
                } else {
                    log.info("完整性校验通过: {}", tableName);
                }
            } else {
                log.warn("完整性校验字段 {} 为空，无法校验", hmacFieldName);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("HMAC 校验失败 {}: {}", tableName, e.getMessage(), e);
        }
    }


    /**
     * 获取表名
     */
    private String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(com.baomidou.mybatisplus.annotation.TableName.class)) {
            return clazz.getAnnotation(com.baomidou.mybatisplus.annotation.TableName.class).value();
        }
        return clazz.getSimpleName(); // 备用方案
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
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
