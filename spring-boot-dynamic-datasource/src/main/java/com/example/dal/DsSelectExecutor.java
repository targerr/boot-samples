package com.example.dal;

import java.util.function.Supplier;

/**
 * @Author: wgs
 * @Date 2024/4/23 16:45
 * @Classname DsSelectExecutor
 * @Description 手动指定数据源的用法
 */
public class DsSelectExecutor {

    /**
     * 有返回结果
     *
     * @param ds
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> T submit(DS ds, Supplier<T> supplier) {
        DsContextHolder.set(ds);
        try {
            return supplier.get();
        } finally {
            DsContextHolder.reset();
        }
    }

    /**
     * 无返回结果
     *
     * @param ds
     * @param call
     */
    public static void execute(DS ds, Runnable call) {
        DsContextHolder.set(ds);
        try {
            call.run();
        } finally {
            DsContextHolder.reset();
        }
    }
}
