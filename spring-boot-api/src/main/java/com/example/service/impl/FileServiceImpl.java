package com.example.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.configuration.ApiConfig;
import com.example.entity.FileEntity;
import com.example.response.ResponseBody;
import com.example.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final ApiConfig apiConfig;
    private final AuthService authService;
    private final RestTemplate restTemplate;

    public FileServiceImpl(ApiConfig apiConfig, AuthService authService) {
        this.apiConfig = apiConfig;
        this.authService = authService;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String uploadFile(FileEntity fileData) {
        String uploadUrl = apiConfig.getUrl() + "/zc/file/upload";
        HttpHeaders headers = createHeaders();
        String requestBody = JSON.toJSONString(fileData);

        ResponseEntity<String> response = sendRequest(uploadUrl, HttpMethod.POST, requestBody, headers);
        ResponseBody<String> responseBody = JSON.parseObject(response.getBody(), ResponseBody.class);

        log.info("数据上传结果: {}", response.getBody());
        return Objects.requireNonNull(responseBody, "数据上传失败").getData();
    }

    @Override
    public Boolean delete(String fileId) {
        String deleteUrl = apiConfig.getUrl() + "/zc/file/remove?id=" + fileId;
        HttpHeaders headers = createHeaders();

        ResponseEntity<String> response = sendRequest(deleteUrl, HttpMethod.POST, null, headers);
        ResponseBody<?> responseBody = JSON.parseObject(response.getBody(), ResponseBody.class);

        log.info("数据删除结果: {}", response.getBody());
        return Objects.requireNonNull(responseBody, "数据删除失败").getSuccess();
    }

    @Override
    public String download(String fileId) {
        String downloadUrl = apiConfig.getUrl() + "/zc/file/download?fileId=" + fileId;
        HttpHeaders headers = createHeaders();

        ResponseEntity<String> response = sendRequest(downloadUrl, HttpMethod.GET, null, headers);
        ResponseBody<String> responseBody = JSON.parseObject(response.getBody(), ResponseBody.class);

        log.info("数据下载结果: {}", response.getBody());
        return Objects.requireNonNull(responseBody, "数据下载失败").getData();
    }

    private ResponseEntity<String> sendRequest(String url, HttpMethod method, String body, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("请求失败: {}，响应: {}", url, response.getBody());
            throw new RuntimeException("请求失败: " + response.getBody());
        }
        return response;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Blade-Auth", "Bearer " + authService.getToken().getAccessToken());
        return headers;
    }
}
