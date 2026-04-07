# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在此仓库中工作时提供指导。

## 构建与测试命令

```bash
# 构建项目（跳过测试）
mvn clean package -DskipTests

# 运行所有测试
mvn test

# 运行单个测试类
mvn test -Dtest=QwenChatControllerTest

# 启动应用
mvn spring-boot:run
```

## 架构概览

这是一个 Spring Boot 3.4 应用，通过 Spring AI Alibaba（DashScope）集成阿里通义千问大模型。

### 核心组件

**ChatClient 配置** (`QwenClientsConfig.java`)
- 定义两个 ChatClient Bean：`qwenPlusClient` 和 `qwenMaxClient`
- 分别预配置不同模型：qwen-plus 响应快、qwen3-max 能力强
- 通过 `@Qualifier("qwenPlusClient")` 或 `@Qualifier("qwenMaxClient")` 注入

**模型路由模式** (`QwenModelRouter.java`)
- 根据任务类型自动选择合适的 ChatClient
- `TaskType` 枚举：SUMMARY（摘要，用 qwen-plus）、SWOT（分析，用 qwen-max）、AUTO（智能选择）
- 自动选择逻辑：文本超过 600 字或包含"深度"、"SWOT"等关键词时使用 qwen-max

**控制器**
- `QwenChatController` - 基础对话、知乎风格回答、结构化数据抽取
- `QwenStreamController` - SSE 流式响应，打字机效果
- `QwenRouterController` - 调用 QwenModelRouter 进行任务路由

### 常用代码模式

**ChatClient 调用方式**
```java
// 简单调用
chatClient.prompt().user("消息内容").call().content();

// 带系统提示和模型选项
chatClient.prompt()
    .system("系统提示词")
    .user("用户消息")
    .options(DashScopeChatOptions.builder().withModel("qwen-max").build())
    .call().content();

// 结构化输出（自动映射到实体类）
chatClient.prompt().user("文本").call().entity(MyEntity.class);

// 流式输出（返回 Flux<String>）
chatClient.prompt().user("文本").stream().content();
```

**提示词模板**
- 使用 `PromptTemplate` 和 `{占位符}` 语法定义可复用模板
- 渲染时用 `template.render(Map.of("key", value))` 替换占位符

## 配置说明

API Key 配置在 `application.yml`：
```yaml
spring.ai.dashscope.api-key: ${DASHSCOPE_API_KEY}
```