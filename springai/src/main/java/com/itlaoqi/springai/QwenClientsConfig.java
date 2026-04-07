package com.itlaoqi.springai;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class QwenClientsConfig {
    // 配置 Qwen Plus 模型的 ChatClient
    @Bean("qwenPlusClient")
    public ChatClient qwenPlusClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(DashScopeChatOptions.builder()
                        .withModel("qwen-plus")
                        .build())
                .build();
    }

    // 配置 Qwen3 Max 模型的 ChatClient
    @Bean("qwenMaxClient")
    public ChatClient qwenMaxClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(DashScopeChatOptions.builder()
                        .withModel("qwen3-max")
                        .build())
                .build();
    }
}