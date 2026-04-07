# Spring AI Alibaba Demo

基于 Spring AI + 阿里云通义千问（DashScope）的 AI 应用示例项目，展示如何使用 Spring AI 框架集成大语言模型。

## 功能特点

- **基础对话** - 简单的 AI 对话接口，支持默认消息处理
- **知乎风格回答** - 使用 Prompt 模板生成知乎高赞风格的回答
- **结构化数据抽取** - 从自然语言文本中抽取物流信息等结构化数据
- **流式响应 (SSE)** - 打字机效果的实时流式输出
- **智能模型路由** - 根据任务类型自动选择合适的模型（qwen-plus / qwen-max）

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 17+ |
| Spring Boot | 3.4.10 |
| Spring AI Alibaba | 1.0.0.2 |
| Lombok | - |

## 快速开始

### 前置条件

1. JDK 17 或更高版本
2. Maven 3.6+
3. 阿里云 DashScope API Key（[获取地址](https://dashscope.console.aliyun.com/)）

### 安装步骤

```bash
# 克隆项目
git clone <repository-url>
cd springai

# 配置 API Key（修改 application.yml）
spring:
  ai:
    dashscope:
      api-key: YOUR_API_KEY_HERE

# 构建项目
mvn clean package -DskipTests

# 运行项目
java -jar target/springai-0.0.1-SNAPSHOT.jar
```

### 配置说明

```yaml
# application.yml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}  # 建议使用环境变量
      chat:
        options:
          model: qwen-plus  # 默认模型
```

## API 文档

### 1. 基础对话

```http
GET /api/qwen/chat/base?message=你好
```

**参数**
| 参数 | 必填 | 说明 |
|------|------|------|
| message | 否 | 用户消息，为空时默认"你好" |

**响应**
```text
你好！我是AI助手，有什么可以帮助您的吗？
```

### 2. 知乎风格回答

```http
GET /api/qwen/chat/answer?question=如何评价微服务架构？&model=qwen-max
```

**参数**
| 参数 | 必填 | 说明 |
|------|------|------|
| question | 否 | 问题，为空时使用默认问题 |
| model | 否 | 模型名称，默认 qwen-plus |

**响应特点**
- 先给出 1-2 句结论
- 分点阐述，每点包含：观点 → 逻辑链 → 例证
- 可执行建议或检查清单

### 3. 物流信息抽取

```http
GET /api/qwen/chat/extract?text=订单号20250326-001，快件已到达上海浦东转运中心，预计明天10:00送达；派送员李四，联系电话13900139000。
```

**参数**
| 参数 | 必填 | 说明 |
|------|------|------|
| text | 否 | 物流文本，为空时使用默认示例 |
| model | 否 | 模型名称，默认 qwen-plus |

**响应**
```json
{
  "orderId": "20250326-001",
  "currentLocation": "上海浦东转运中心",
  "estimatedArrival": "明天10:00",
  "courierName": "李四",
  "courierPhone": "13900139000"
}
```

### 4. 流式响应 (SSE)

```http
GET /api/qwen/stream/sse?prompt=介绍一下Spring框架
Accept: text/event-stream
```

**参数**
| 参数 | 必填 | 说明 |
|------|------|------|
| prompt | 是 | 用户提示词 |

**响应** - Server-Sent Events 流式输出，实现打字机效果

### 5. 智能模型路由

#### 文本摘要
```http
GET /api/qwen/router/summary?text=长文本内容...
```

#### SWOT 分析
```http
GET /api/qwen/router/swot?text=分析对象描述...
```

#### 自动路由
```http
GET /api/qwen/router/analyze?text=内容&mode=auto
```

**mode 参数**
| 值 | 说明 | 默认模型 |
|------|------|------|
| summary | 文本摘要 | qwen-plus |
| swot | SWOT 分析 | qwen-max |
| auto | 智能选择 | 根据文本长度和关键词自动选择 |

**自动路由规则**
- 文本长度 > 600 字符 → qwen-max
- 包含关键词（swot、深入、深度、全面分析、系统性）→ qwen-max
- 其他情况 → qwen-plus

## 项目结构

```
src/main/java/com/itlaoqi/springai/
├── SpringaiApplication.java      # 启动类
├── QwenChatController.java       # 对话控制器（基础对话、知乎风格、结构化抽取）
├── QwenStreamController.java     # 流式响应控制器
├── QwenRouterController.java     # 模型路由控制器
├── QwenModelRouter.java          # 智能模型路由服务
├── QwenClientsConfig.java        # ChatClient 配置类
└── LogisticsInfo.java            # 物流信息实体
```

## 模型说明

| 模型 | 适用场景 | 特点 |
|------|---------|------|
| qwen-plus | 日常对话、简单摘要 | 响应快、成本低 |
| qwen-max | 复杂分析、深度推理 | 能力强、适合复杂任务 |
| qwen3-max | 最新模型 | 更强的推理能力 |

## 开发指南

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=QwenChatControllerTest
```

### 添加新的 API

1. 创建 Controller 类，添加 `@RestController` 注解
2. 注入 `ChatClient.Builder` 或已配置的 `ChatClient`
3. 使用 `ChatClient` 的链式 API 构建请求

**示例代码**
```java
@RestController
@RequestMapping("/api/qwen/custom")
public class CustomController {

    private final ChatClient chatClient;

    public CustomController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/hello")
    public String hello(@RequestParam("name") String name) {
        return chatClient.prompt()
                .user("请用一句话问候：" + name)
                .call()
                .content();
    }
}
```

## 常见问题

**Q: 如何切换模型？**

A: 可以通过以下方式：
1. 请求参数 `model` 指定模型
2. 使用 `DashScopeChatOptions.builder().withModel("qwen-max").build()` 配置

**Q: 如何处理 API 调用失败？**

A: Spring AI 会抛出异常，建议在 Controller 添加 `@ExceptionHandler` 处理：

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<String> handleException(Exception e) {
    return ResponseEntity.status(500).body("AI 服务异常: " + e.getMessage());
}
```

**Q: 如何配置多个模型？**

A: 参考 `QwenClientsConfig.java`，使用 `@Bean` 配置多个 ChatClient。

## 许可证

MIT License

## 参考

- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)
- [阿里云 DashScope 文档](https://help.aliyun.com/zh/dashscope/)
- [Spring AI Alibaba](https://github.com/alibaba/spring-ai-alibaba)