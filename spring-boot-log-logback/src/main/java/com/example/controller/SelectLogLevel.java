package com.example.controller;

/**
 * @Author: wgs
 * @Date 2022/11/25 21:24
 * @Classname SelectLogLevel
 * @Description
 */

import com.example.vo.Imoocer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

/**
 * <h1>选择合适的日志打印级别</h1>
 * */
@Slf4j
@RestController
@RequestMapping("/select")
public class SelectLogLevel {

    /**
     * <h2>选择日志级别</h2>
     * */
    @PostMapping("/log/level")
    public Imoocer selectLogLevel(Imoocer imoocer) {

        // 1. 更加具体的调试信息, 例如调用了什么方法, 参数是什么, 可以使用 trace
        log.trace("call printSomething, with args: [{}], [{}]", null, null);
        printSomething(null, null);

        // 2. 在项目开发阶段, 调试程序的正确性, 可以使用 debug
        log.debug("coming in selectLogLevel with args: [{}]", imoocer);

        // 3. 正常的业务执行流程、系统的启动/关闭、需要做的审计等等都可以使用 info
        if (imoocer.getAge() > 19) {
            // 将来可以用于统计 age 大于 19 的请求
            log.info("imoocer age > 19: [{}]", imoocer.getAge());
        }

        // 4. 不是错误, 不会影响程序的正常执行, 但是并不建议这样做, 可以使用 warn
        try {
            callRemoteSystem();
        } catch (Exception ex) {
            log.warn("call remote system error: [{}]", System.currentTimeMillis());
        }

        // 5. 程序出现了某种错误, 需要介入处理
        try {
            // 读取 classpath 下的 error.txt 文件
            org.springframework.util.ResourceUtils.getFile("classpath:error.txt");
        } catch (FileNotFoundException ex) {
            log.error("error.txt is not exist");
            return null;
        }

        return imoocer;
    }

    private void printSomething(String x, String y) {

        // ...
    }

    private void callRemoteSystem() {
        // ...
    }
}
