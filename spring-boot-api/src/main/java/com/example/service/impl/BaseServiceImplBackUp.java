package com.example.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.example.annotation.Url;
import com.example.configuration.ApiConfig;
import com.example.response.ResponseBody;
import com.example.service.AuthService;
import com.example.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;

/**
 * 基础类实现
 * sa
 */
@Slf4j
public class BaseServiceImplBackUp<T> implements BaseService<T> {
    @Override
    public Boolean save(T object) {
        ApiConfig apiConfig = SpringUtil.getBean(ApiConfig.class);
        AuthService authService = SpringUtil.getBean(AuthService.class);
        Url url = object.getClass().getDeclaredAnnotation(Url.class);
        assert url != null;
        String UPLOAD_URL = apiConfig.getUrl() + url.value() + "/submit";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Blade-Auth", "Bearer " + authService.getToken().getAccessToken());
        String str1 = JSON.toJSONString(object);
        log.info("login request url and body: [{}], [{}]", UPLOAD_URL, str1);

        HttpEntity<String> entity = new HttpEntity<>(str1, headers);
        ResponseEntity<String> response = restTemplate.exchange(UPLOAD_URL, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ResponseBody responseBody = JSON.parseObject(response.getBody(), ResponseBody.class);
            log.info("响应报文: " + response.getBody());
            if (responseBody != null) {
                return responseBody.getSuccess();
            } else {
                throw new RuntimeException("数据上传失败" + response.getBody());
            }
        } else {
            System.out.println("数据上传失败: " + response.getBody());
            throw new RuntimeException("数据上传失败" + response.getBody());
        }
    }

    @Override
    public Boolean update(T object) {
        ApiConfig apiConfig = SpringUtil.getBean(ApiConfig.class);
        AuthService authService = SpringUtil.getBean(AuthService.class);
        Url url = object.getClass().getDeclaredAnnotation(Url.class);
        assert url != null;
        String UPDATE_URL = apiConfig.getUrl() + url.value() + "/submit";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Blade-Auth", "Bearer " + authService.getToken().getAccessToken());
        String str1 = JSON.toJSONString(object);

        HttpEntity<String> entity = new HttpEntity<>(str1, headers);
        ResponseEntity<String> response = restTemplate.exchange(UPDATE_URL, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ResponseBody responseBody = JSON.parseObject(response.getBody(), ResponseBody.class);
            System.out.println("数据更新成功: " + response.getBody());
            if (responseBody != null) {
                return responseBody.getSuccess();
            } else {
                throw new RuntimeException("数据更新失败" + response.getBody());
            }
        } else {
            System.out.println("数据更新失败: " + response.getBody());
            throw new RuntimeException("数据更新失败" + response.getBody());
        }
    }

    @Override
    public Boolean delete(String guid) {
        ApiConfig apiConfig = SpringUtil.getBean(ApiConfig.class);
        AuthService authService = SpringUtil.getBean(AuthService.class);

        // 获取泛型类T的Class对象
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Url url = clazz.getAnnotation(Url.class);
        assert url != null;
        String DELETE_URL = apiConfig.getUrl() + url.value() + "/remove" + "?ids=" + guid;
        System.out.println("url: " + DELETE_URL);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Blade-Auth", "Bearer " + authService.getToken().getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(DELETE_URL, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ResponseBody responseBody = JSON.parseObject(response.getBody(), ResponseBody.class);
            System.out.println("数据删除成功: " + response.getBody());
            if (responseBody != null) {
                return responseBody.getSuccess();
            } else {
                throw new RuntimeException("数据删除失败" + response.getBody());
            }
        } else {
            System.out.println("数据删除失败: " + response.getBody());
            throw new RuntimeException("数据删除失败" + response.getBody());
        }
    }
}
