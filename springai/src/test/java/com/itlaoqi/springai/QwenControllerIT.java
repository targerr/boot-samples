package com.itlaoqi.springai;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QwenControllerIT {

    @LocalServerPort
    private int port;

    @Resource
    private RestTemplate restTemplate;


    @Test
    void testBaseMethod() {
        String url = String.format("http://localhost:%d/api/qwen/chat/base?message=%s", port, encode("什么是哥德巴赫猜想"));
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "HTTP 响应应为 2xx 成功");
        String body = response.getBody();
        log.info("响应体: {}", body);
    }


    @Test
    void testAnswerZhihuStyle() {
        String url = String.format("http://localhost:%d/api/qwen/chat/answer?question=%s&model=qwen3-max", port,
                encode("为什么DDD在电商系统中常见"));
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "HTTP 响应应为 2xx 成功");
        String body = response.getBody();
        log.info("/answer 响应体: {}", body);
    }

    @Test
    void testExtractLogisticsInfo() throws JsonProcessingException {
        String text = "订单号 2025-10-21-8899，快件已到达杭州滨江转运中心，预计今晚19:00送达；派送员张三，联系电话 13800138000。";
        String url = String.format("http://localhost:%d/api/qwen/chat/extract?text=%s", port, encode(text));
        ResponseEntity<LogisticsInfo> response = restTemplate.getForEntity(url, LogisticsInfo.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "HTTP 响应应为 2xx 成功");
        LogisticsInfo info = response.getBody();
        assertNotNull(info, "返回的 LogisticsInfo 不能为 null");
        log.info("原始文本: {}", new ObjectMapper().writeValueAsString(info));
        log.info("结构化抽取结果: orderId={}, location={}, eta={}, courier={}, phone={}",
                info.getOrderId(), info.getCurrentLocation(), info.getEstimatedArrival(), info.getCourierName(), info.getCourierPhone());
    }

    private String encode(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }
}