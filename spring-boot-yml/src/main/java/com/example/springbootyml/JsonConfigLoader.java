package com.example.springbootyml;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2024/4/19
 * @Classname JsonConfigLoader
 * @since 1.0.0
 */

@Component
public class JsonConfigLoader {

    @Autowired
    private ResourceLoader resourceLoader;

    private Map<String, Object> configMap;

    @PostConstruct
    public void init() {
        try {
            Resource resource = resourceLoader.getResource("classpath:taikang.json");
//            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            Reader reader = new InputStreamReader(resource.getInputStream());
            final String content = IoUtil.read(reader);

            System.out.println("读取配置文件: " + content);
//            final byte[] bytes = IoUtil.readBytes(inputStream);
//            String content = new String(bytes, StandardCharsets.UTF_8);

            configMap = JSONUtil.parseObj(content).toBean(Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON config file", e);
        }
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }
}
