package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * <h1>MDC 的使用和源码解析</h1>
 * */
@Slf4j
public class UseMDC {

    // 使用 MDC 之前, 需要先去配置 %X{REQUEST_ID}
    private static final String FLAG = "REQUEST_ID";

    // -----------------------------------------------------------------------------------------------------------------
    // 第一个例子

    private static void mdc01() {

        MDC.put(FLAG, UUID.randomUUID().toString());
        log.info("log in mdc01");
        mdc02();

        log.info("MDC FLAG is: [{}]", MDC.get(FLAG));
        MDC.remove(FLAG);
        log.info("after remove MDC FLAG");

        // 我们没有使用 clear, 但是也能猜出来, 就是清除所有的 key
    }

    private static void mdc02() {

        log.trace("log in trace");
        log.debug("log in debug");
        log.info("log in info");
        log.warn("log in warn");
        log.error("log in error");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // 第二个例子, 多线程
    static class MyHandler extends Thread {

        private final String name;

        public MyHandler(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            MDC.put(FLAG, UUID.randomUUID().toString());
            log.info("start to process: [{}]", this.name);

            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                log.info(e.getMessage());
            }

            log.info("done to process: [{}]", this.name);
            MDC.remove(FLAG);
        }
    }

    private void MultiThreadUseMdc() {

        new MyHandler("imooc").start();
        new MyHandler("qinyi").start();
    }


    public static void main(String[] args) {

//        mdc01();
        mdc02();
//        new UseMDC().MultiThreadUseMdc();
    }
}
