package com.itlaoqi.springai;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import org.springframework.ai.chat.client.ChatClient;
import java.io.IOException;


@RestController
@RequestMapping("/api/qwen/stream")
public class QwenStreamController {

    private final ChatClient chatClient;

    public QwenStreamController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    
    //打字机效果：流式响应，每个 token 发送到前端
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sse(@RequestParam("prompt") String prompt) {
        // 创建 SseEmitter，设置超时时间为 0（无限期）
        SseEmitter emitter = new SseEmitter(0L);

        String system = "你是一个生活百科助手，请用简洁明了的中文回答。";
        
        //TIPS:Token是AI处理文本的基本单位。 它由“分词器”将文本拆分成单词、字符或词块等更小单元而生成。 AI通过处理这些标准化的数据块来理解和生成语言，其处理上限和费用也基于Token数量计算。
        //TIPS:AI处理语言的基本单位是Token，而非传统意义上的单词。一个单词可能由数个Token组成，单个字符或标点符号也能成为一个Token。 AI模型通过这种方式更高效地理解和生成文本，其处理限制和费用也基于Token数量计算。
        
        //Flux<String>的实现原理：Flux是Reactive Streams规范的一部分，用于处理异步、非阻塞的流数据。在Spring AI中，Flux<String>用于表示流式响应，每个元素是一个token。当调用stream()方法时，AI模型会返回一个Flux<String>，其中每个元素都是模型生成的一个token。
        Flux<String> tokenFlux = chatClient
                .prompt()
                .system(system)
                .user(prompt)
                .stream()
                .content();

        tokenFlux
                //doOnNext：每个元素（token）发送到前端
                .doOnNext(chunk -> {
                    try {
                        // 每个 token 发送到前端
                        emitter.send(chunk);
                    } catch (IOException e) {
                        // 发生异常时，发送错误到前端
                        emitter.completeWithError(e);
                    }
                })
                //doOnError：如果发生错误，发送错误到前端
                .doOnError(emitter::completeWithError)
                //doOnComplete：如果完成，发送完成信号到前端
                .doOnComplete(emitter::complete)
                //subscribe：开始订阅，触发响应
                .subscribe();

        return emitter;
    }

}