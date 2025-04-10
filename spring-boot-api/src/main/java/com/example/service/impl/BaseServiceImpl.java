package com.example.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.example.annotation.Url;
import com.example.configuration.ApiConfig;
import com.example.response.ResponseBody;
import com.example.service.AuthService;
import com.example.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * 基础类实现
 */
@Slf4j
public class BaseServiceImpl<T> implements BaseService<T>  {

    ApiConfig apiConfig = SpringUtil.getBean(ApiConfig.class);
    AuthService authService = SpringUtil.getBean(AuthService.class);

    @Override
    public Boolean save(T object) {
        return submit(object, "保存");
    }

    @Override
    public Boolean update(T object) {
        return submit(object, "更新");
    }

    private Boolean submit(T object, String action) {
        Url url = Objects.requireNonNull(object.getClass().getDeclaredAnnotation(Url.class), "对象缺少 @Url 注解");
        String requestUrl = apiConfig.getUrl() + url.value() + "/submit";
        HttpHeaders headers = createHeaders();
        String requestBody = JSON.toJSONString(object);

        log.info("{}请求 URL: [{}], 请求体: [{}]", action, requestUrl, requestBody);
        ResponseEntity<String> response = sendRequest(requestUrl, HttpMethod.POST, requestBody, headers);

        ResponseBody<?> responseBody = JSON.parseObject(response.getBody(), ResponseBody.class);
        log.info("{}响应: {}", action, response.getBody());

        // ResponseBody<?> responseBody = sendPostRequest(requestUrl, requestBody);

        return Objects.requireNonNull(responseBody, action + "失败").getSuccess();
    }

    @Override
    public Boolean delete(String guid) {
        Class<T> clazz = getEntityClass();
        Url url = Objects.requireNonNull(clazz.getAnnotation(Url.class), "类缺少 @Url 注解");

        String deleteUrl = apiConfig.getUrl() + url.value() + "/remove?ids=" + guid;
        HttpHeaders headers = createHeaders();

        log.info("删除请求 URL: [{}]", deleteUrl);
        ResponseEntity<String> response = sendRequest(deleteUrl, HttpMethod.POST, null, headers);

        ResponseBody<?> responseBody = JSON.parseObject(response.getBody(), ResponseBody.class);
        log.info("删除响应: {}", response.getBody());
        return Objects.requireNonNull(responseBody, "删除失败").getSuccess();
    }

    private ResponseEntity<String> sendRequest(String url, HttpMethod method, String body, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("请求失败: URL [{}], 响应: {}", url, response.getBody());
            throw new RuntimeException("请求失败: " + response.getBody());
        }
        return response;
    }

    private ResponseBody<?> sendPostRequest(String url,  String body) {
        log.info("请求地址: {}", url);
        HttpResponse response = cn.hutool.http.HttpRequest.post(url)
                .header(createHeaders())
                .body(body)
                .execute();

        log.info("请求结果: {}", response.body());
        ResponseBody<?> responseBody = JSON.parseObject(response.body(), ResponseBody.class);
        return responseBody;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Blade-Auth", "Bearer " + authService.getToken().getAccessToken());
        return headers;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEntityClass() {
        return (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
