package com.itlaoqi.springai;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;

import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class QwenModelRouter {
    // 定义任务类型枚举,SUMMARY:总结,SWOT:SWOT分析,AUTO:自动选择模型
    public enum TaskType { SUMMARY, SWOT, AUTO }

    private final ChatClient qwenPlusClient;
    private final ChatClient qwenMaxClient;

    // 构造函数，注入 Qwen Plus 和 Qwen Max 模型的 ChatClient
    public QwenModelRouter(@Qualifier("qwenPlusClient") ChatClient qwenPlusClient,
                           @Qualifier("qwenMaxClient") ChatClient qwenMaxClient) {
        this.qwenPlusClient = qwenPlusClient;
        this.qwenMaxClient = qwenMaxClient;
    }
    // 分析方法，根据任务类型和强制模型选择合适的 ChatClient 进行分析
    // text:输入文本,task:任务类型,forceModel:强制模型（可选）
    public String analyze(String text, TaskType task, String forceModel) {
        //select client,根据任务类型和输入文本选择合适的 ChatClient
        ChatClient client = selectClient(text, task);
        String system = buildSystem(task);
        ChatClientRequestSpec prompt = client
                .prompt()
                .system(system)
                .user(text);
        // 如果强制模型存在，添加到请求选项中
        if (StringUtils.hasText(forceModel)) {
            DashScopeChatOptions opts = DashScopeChatOptions.builder()
                    .withModel(forceModel)
                    .build();
            prompt = prompt.options(opts);
        }
        
        return prompt.call().content();
    }
    // 根据任务类型和输入文本选择合适的 ChatClient
    // text:输入文本,task:任务类型
    private ChatClient selectClient(String text, TaskType task) {
        if (task == TaskType.SWOT) return qwenMaxClient;
        if (task == TaskType.SUMMARY) return qwenPlusClient;
        // AUTO：根据输入长度与关键词选择
        int len = text != null ? text.length() : 0;
        // 如果包含深度分析关键词或文本长度超过600字符，选择 Qwen Max 模型
        if (containsDeepKeywords(text) || len > 600) {
            return qwenMaxClient;
        }
        // 否则选择 Qwen Plus 模型
        return qwenPlusClient;
    }
    // 检查文本是否包含深度分析关键词
    // text:输入文本
    private boolean containsDeepKeywords(String text) {
        String s = text == null ? "" : text.toLowerCase();
        return s.contains("swot") || s.contains("深入") || s.contains("深度") || s.contains("全面分析") || s.contains("系统性");
    }
    // 根据任务类型构建系统提示
    // task:任务类型
    private String buildSystem(TaskType task) {
        switch (task) {
            case SWOT:
                return """
                        你是一名商业分析顾问，请输出针对输入内容的SWOT分析：
                        - 结构包含四部分：Strengths、Weaknesses、Opportunities、Threats；
                        - 每部分给出3-5条要点，简洁且具体；
                        - 禁止杜撰事实，必要时标注假设；
                        - 用中文回答，可使用Markdown二级标题（##）与列表。
                        """;
            case SUMMARY:
                return """
                        你是一名报告压缩助手，请生成中文摘要：
                        - 先给出一句话结论；
                        - 然后列出3-5条要点；
                        - 风格简洁、信息密度高，避免空话；
                        """;
            default:
                return """
                        你是一名智能报告分析助手：
                        - 简短文本请生成摘要（一句话结论 + 3-5要点）；
                        - 若文本包含“SWOT/深入/深度/全面分析/系统性”等关键词或篇幅较长，请输出SWOT分析；
                        - 禁止杜撰事实，必要时标注假设；
                        - 中文回答，可使用Markdown结构，但避免过度装饰。
                        """;
        }
    }
}