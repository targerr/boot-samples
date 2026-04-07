package com.itlaoqi.springai;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QwenRouterControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 测试 /router/summary 端点
     */
    @Test
    void testSummaryEndpoint() {
        String text = "请用五条要点总结这份2页产品评审建议。";
        String url = String.format("http://localhost:%d/api/qwen/router/summary?text=%s", port, encode(text));
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "HTTP 响应应为 2xx 成功");
        String body = response.getBody();
        log.info("/router/summary 响应: {}", body);
        log.info("--------------------------------------------------");
    }
    /**
     * 测试 /router/swot 端点
     */
    @Test
    void testSwotEndpoint() {
        String text = "围绕跨境电商的市场与团队能力进行SWOT分析。";
        String url = String.format("http://localhost:%d/api/qwen/router/swot?text=%s", port, encode(text));
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "HTTP 响应应为 2xx 成功");
        String body = response.getBody();

        log.info("/router/swot 响应: {}", body);
        log.info("--------------------------------------------------");
    }
    /**
     * 测试 /router/analyze(auto) 端点
     */
    @Test
    void testAnalyzeAutoEndpoint() {
        String text = "请做全面深入的SWOT分析，考虑市场竞争与供应链风险。";
        String url = String.format("http://localhost:%d/api/qwen/router/analyze?text=%s&mode=auto", port, encode(text));
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "HTTP 响应应为 2xx 成功");
        String body = response.getBody();
        log.info("/router/analyze(auto) 响应: {}", body);
        log.info("--------------------------------------------------");
    }
    /**
     * 测试 /router/analyze(summary + forced model) 端点
     */
    @Test
    void testAnalyzeSummaryWithForcedModel() {
        String text = "将以下段落压缩为摘要，突出关键结论与三条要点。";
        String url = String.format("http://localhost:%d/api/qwen/router/analyze?text=%s&mode=summary&model=%s", port, encode(text), encode("qwen-plus"));
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "HTTP 响应应为 2xx 成功");
        String body = response.getBody();
        log.info("/router/analyze(summary + forced model) 响应: {}", body);
        log.info("--------------------------------------------------");
    }

    private String encode(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }
}