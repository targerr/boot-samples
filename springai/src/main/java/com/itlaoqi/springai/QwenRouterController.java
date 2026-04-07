package com.itlaoqi.springai;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qwen/router")
public class QwenRouterController {

    private final QwenModelRouter router;

    public QwenRouterController(QwenModelRouter router) {
        this.router = router;
    }

    // 简短摘要：默认使用 qwen-plus（可通过 model 覆盖）
    @GetMapping("/summary")
    public String summary(@RequestParam("text") String text,
                          @RequestParam(name = "model", required = false) String model) {
        return router.analyze(text, QwenModelRouter.TaskType.SUMMARY, model);
    }

    // 深度 SWOT 分析：默认使用 qwen-max（可通过 model 覆盖）
    @GetMapping("/swot")
    public String swot(@RequestParam("text") String text,
                       @RequestParam(name = "model", required = false) String model) {
        return router.analyze(text, QwenModelRouter.TaskType.SWOT, model);
    }

    // 自动路由：根据长度与关键词选择模型与结构（可通过 mode 指定 summary|swot）
    @GetMapping("/analyze")
    public String analyze(@RequestParam("text") String text,
                          @RequestParam(name = "mode", defaultValue = "auto") String mode,
                          @RequestParam(name = "model", required = false) String model) {
        QwenModelRouter.TaskType task = parseMode(mode);
        return router.analyze(text, task, model);
    }

    private QwenModelRouter.TaskType parseMode(String mode) {
        if (!StringUtils.hasText(mode)) return QwenModelRouter.TaskType.AUTO;
        String m = mode.trim().toLowerCase();
        return switch (m) {
            case "summary" -> QwenModelRouter.TaskType.SUMMARY;
            case "swot" -> QwenModelRouter.TaskType.SWOT;
            default -> QwenModelRouter.TaskType.AUTO;
        };
    }
}