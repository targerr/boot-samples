package com.itlaoqi.springai;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/qwen/chat")
public class QwenChatController {

    private final ChatClient chatClient;

    // 预置的「知乎高赞」风格系统提示词模板
    private final PromptTemplate zhihuStyleTemplate = new PromptTemplate(
            """
            你是一位知乎高赞答主，请按以下规范用中文回答：
            - 先给出1-2句话的明确结论（置顶）。
            - 然后分点阐述，每点包含：观点 -> 逻辑链 -> 例证或数据。
            - 如存在争议，简要列出不同观点及适用场景。
            - 最后给出可执行建议或检查清单。
            - 风格：专业、克制、结构清晰，避免堆砌术语。
            - 允许使用 Markdown 标题（##）与列表，但避免过度装饰。
            - 禁止杜撰事实；不确定处请标注假设或推测。
            - 回答必须围绕用户问题展开，不进行无关延伸。

            用户问题：{question}
            """
    );
    // 构造函数注入 ChatClient.Builder
    public QwenChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    // 原始基础调用保留
    @GetMapping("/base")
    public String chat(@RequestParam(name = "message") String message) {
        //prompt() 方法用于创建一个新的聊天请求规范
        ChatClientRequestSpec prompt = this.chatClient.prompt();
        if (!StringUtils.hasText(message)) {
            message = "你好";
        }
        //user() 方法用于设置用户输入的问题
        //call() 方法用于执行聊天请求并返回响应
        //content() 方法用于提取响应体中的文本内容
        return prompt.user(message).call().content();
    }

    // 知乎高赞风格回答生成器：固定系统提示词，问题必填，模型可选
    @GetMapping("/answer")
    public String zhihuAnswer(@RequestParam(name = "question") String question,
                               @RequestParam(name = "model", required = false) String model) {
        if (!StringUtils.hasText(question)) {
            question = "如何评价微服务对团队协作效率的影响？";
        }
        String system = zhihuStyleTemplate.render(Map.of("question", question));
        ChatClientRequestSpec prompt = this.chatClient
                .prompt()
                .system(system)
                .user(question);

        // 通用 ChatClient 选项设置模型，避免引用不存在的 QianwenChatOptions
        String effectiveModel = StringUtils.hasText(model) ? model : "qwen-plus";
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel(effectiveModel)
                .build();
        prompt = prompt.options(options);
        
        return prompt.call().content();
    }

    // 结构化抽取：引导模型返回符合 LogisticsInfo 的 JSON，并直接映射为 POJO
    @GetMapping("/extract")
    public LogisticsInfo extractLogistics(@RequestParam(name = "text") String text,
                                          @RequestParam(name = "model", required = false) String model) {
        if (!StringUtils.hasText(text)) {
            text = "订单号 2025-10-21-8899，快件已到达杭州滨江转运中心，预计今晚19:00送达；派送员张三，联系电话 13800138000。";
        }
        String effectiveModel = StringUtils.hasText(model) ? model : "qwen-plus";
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel(effectiveModel)
                .build();

        String system = """
                你是一名物流信息抽取助手。请从用户提供的中文物流状态描述中提取以下字段，并以 JSON 返回：
                - orderId：订单号
                - currentLocation：当前位置
                - estimatedArrival：预计到达时间
                - courierName：快递员姓名
                - courierPhone：快递员联系方式
                输出要求：
                - 只返回一个 JSON 对象，不要附加任何解释或额外文本；
                - 若缺失某字段，请给出合理的空值或最可能的估计；
                - 时间字段使用可解析字符串（如 ISO-8601 或 "明晚" 这类自然语言）。
                """;

        return this.chatClient
                .prompt()
                .options(options)
                .system(system)
                .user(text)
                .call()
                // 直接将响应体映射为 LogisticsInfo POJO
                // 根据实体中的 @JsonProperty与@JsonPropertyDescription注解
                // 自动将 JSON 字段映射到 POJO 字段
                .entity(LogisticsInfo.class);
    }
}