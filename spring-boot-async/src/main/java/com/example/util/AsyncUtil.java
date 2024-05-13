package com.example.util;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import lombok.extern.slf4j.Slf4j;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 异步工具类
 *
 * @author YiHui
 * @date 2023/6/12
 */
@Slf4j
public class AsyncUtil {
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = this.defaultFactory.newThread(r);
            if (!thread.isDaemon()) {
                thread.setDaemon(true);
            }

            thread.setName("paicoding-" + this.threadNumber.getAndIncrement());
            return thread;
        }
    };
    private static ExecutorService executorService;
    private static SimpleTimeLimiter simpleTimeLimiter;

    static {
        initExecutorService(Runtime.getRuntime().availableProcessors() * 2, 50);
    }

    public static void initExecutorService(int core, int max) {
        // 异步工具类的默认线程池构建, 参数选择原则:
        //  1. 技术派不存在cpu密集型任务，大部分操作都设计到 redis/mysql 等io操作
        //  2. 统一的异步封装工具，这里的线程池是一个公共的执行仓库，不希望被其他的线程执行影响，因此队列长度为0, 核心线程数满就创建线程执行，超过最大线程，就直接当前线程执行
        //  3. 同样因为属于通用工具类，再加上技术派的异步使用的情况实际上并不是非常饱和的，因此空闲线程直接回收掉即可；大部分场景下，cpu * 2的线程数即可满足要求了
        max = Math.max(core, max);
        executorService = new ExecutorBuilder()
                .setCorePoolSize(core)
                .setMaxPoolSize(max)
                .setKeepAliveTime(0)
                .setKeepAliveTime(0, TimeUnit.SECONDS)
                .setWorkQueue(new SynchronousQueue<Runnable>())
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setThreadFactory(THREAD_FACTORY)
                .buildFinalizable();
        simpleTimeLimiter = SimpleTimeLimiter.create(executorService);
    }


    /**
     * 带超时时间的方法调用执行，当执行时间超过给定的时间，则返回一个超时异常，内部的任务还是正常执行
     * 若超时时间内执行完毕，则直接返回
     *
     * @param time
     * @param unit
     * @param call
     * @param <T>
     * @return
     */
    public static <T> T callWithTimeLimit(long time, TimeUnit unit, Callable<T> call) throws ExecutionException, InterruptedException, TimeoutException {
        return simpleTimeLimiter.callWithTimeout(call, time, unit);
    }


    public static void execute(Runnable call) {
        executorService.execute(call);
    }

    public static <T> Future<T> submit(Callable<T> t) {
        return executorService.submit(t);
    }


    public static boolean sleep(Number timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout.longValue());
            return true;
        } catch (InterruptedException var3) {
            return false;
        }
    }

    public static boolean sleep(Number millis) {
        return millis == null ? true : sleep(millis.longValue());
    }

    public static boolean sleep(long millis) {
        if (millis > 0L) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException var3) {
                return false;
            }
        }

        return true;
    }


    public static class CompletableFutureBridge {
        private List<CompletableFuture> list;
        private Map<String, Long> cost;
        private String taskName;

        public CompletableFutureBridge() {
            this("CompletableFutureExecute");
        }

        public CompletableFutureBridge(String task) {
            this.taskName = task;
            list = new ArrayList<>();
            cost = new ConcurrentHashMap<>();
            cost.put(task, System.currentTimeMillis());
        }

        /**
         * 异步执行，带返回结果
         *
         * @param supplier
         * @return
         */
        public CompletableFutureBridge supplyAsync(Supplier supplier) {
            return supplyAsync(supplier, executorService);
        }

        public CompletableFutureBridge supplyAsync(Supplier supplier, ExecutorService executor) {
            return supplyAsyncWithTimeRecord(supplier, supplier.toString(), executor);
        }

        public CompletableFutureBridge supplyAsyncWithTimeRecord(Supplier supplier, String name) {
            return supplyAsyncWithTimeRecord(supplier, name, executorService);
        }

        public CompletableFutureBridge supplyAsyncWithTimeRecord(Supplier supplier, String name, ExecutorService executor) {
            list.add(CompletableFuture.supplyAsync(supplyWithTime(supplier, name), executor));
            return this;
        }


        /**
         * 异步并发执行，无返回结果
         *
         * @param run
         * @return
         */
        public CompletableFutureBridge runAsync(Runnable run) {
            list.add(CompletableFuture.runAsync(runWithTime(run, run.toString()), executorService));
            return this;
        }

        public CompletableFutureBridge runAsync(Runnable run, ExecutorService executor) {
            return runAsyncWithTimeRecord(run, run.toString(), executor);
        }


        /**
         * 异步并发执行，并记录耗时
         *
         * @param run
         * @param name
         * @return
         */
        public CompletableFutureBridge runAsyncWithTimeRecord(Runnable run, String name) {
            return runAsyncWithTimeRecord(run, name, executorService);
        }

        public CompletableFutureBridge runAsyncWithTimeRecord(Runnable run, String name, ExecutorService executor) {
            list.add(CompletableFuture.runAsync(runWithTime(run, name), executor));
            return this;
        }

        private Runnable runWithTime(Runnable run, String name) {
            return () -> {
                startRecord(name);
                try {
                    run.run();
                } finally {
                    endRecord(name);
                }
            };
        }

        private Supplier supplyWithTime(Supplier call, String name) {
            return () -> {
                startRecord(name);
                try {
                    return call.get();
                } finally {
                    endRecord(name);
                }
            };
        }

        public CompletableFutureBridge allExecuted() {
            CompletableFuture.allOf(ArrayUtil.toArray(list, CompletableFuture.class)).join();
            endRecord(this.taskName);
            return this;
        }

        private void startRecord(String name) {
            cost.put(name, System.currentTimeMillis());
        }

        private void endRecord(String name) {
            long now = System.currentTimeMillis();
            cost.put(name, now - cost.getOrDefault(name, now));
        }

        public void prettyPrint() {
            StringBuilder sb = new StringBuilder();
            sb.append('\n');
            long totalCost = cost.remove(taskName);
            sb.append("StopWatch '").append(taskName).append("': running time = ").append(totalCost).append(" ms");
            sb.append('\n');
            if (cost.size() <= 1) {
                sb.append("No task info kept");
            } else {
                sb.append("---------------------------------------------\n");
                sb.append("ms         %     Task name\n");
                sb.append("---------------------------------------------\n");
                NumberFormat pf = NumberFormat.getPercentInstance();
                pf.setMinimumIntegerDigits(2);
                pf.setMinimumFractionDigits(2);
                pf.setGroupingUsed(false);
                for (Map.Entry<String, Long> entry : cost.entrySet()) {
                    sb.append(entry.getValue()).append("\t\t");
                    sb.append(pf.format(entry.getValue() / (double) totalCost)).append("\t\t");
                    sb.append(entry.getKey()).append("\n");
                }
            }
            log.info("\n---------------------\n{}\n--------------------\n", sb);
        }
    }

    public static CompletableFutureBridge concurrentExecutor(String... name) {
        if (name.length > 0) {
            return new CompletableFutureBridge(name[0]);
        }
        return new CompletableFutureBridge();
    }


    /**
     * public IndexVo buildIndexVo(String activeTab) {
     * IndexVo vo = new IndexVo();
     * CategoryDTO category = categories(activeTab, vo);
     * vo.setCategoryId(category.getCategoryId());
     * vo.setCurrentCategory(category.getCategory());
     * // 并行调度实例，提高响应性能
     * AsyncUtil.concurrentExecutor("首页响应")
     * .runAsyncWithTimeRecord(() -> vo.setArticles(articleList(category.getCategoryId())), "文章列表")
     * .runAsyncWithTimeRecord(() -> vo.setTopArticles(topArticleList(category)), "置顶文章")
     * .runAsyncWithTimeRecord(() -> vo.setHomeCarouselList(homeCarouselList()), "轮播图")
     * .runAsyncWithTimeRecord(() -> vo.setSideBarItems(sidebarService.queryHomeSidebarList()), "侧边栏")
     * .runAsyncWithTimeRecord(() -> vo.setUser(loginInfo()), "用户信息")
     * .allExecuted()
     * .prettyPrint();
     * return vo;
     * }* @param args
     */

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AsyncUtil.concurrentExecutor("测试无返回").runAsync(()->{
            System.out.println("Async task executed!");
        });


        JSONObject json = new JSONObject();

        AsyncUtil.concurrentExecutor("测试")
                .runAsyncWithTimeRecord(() -> json.put("1", method1()), "方法1")
                .runAsyncWithTimeRecord(() -> json.put("2", method2()), "方法2")

                .allExecuted().prettyPrint();

        System.out.println(json);
    }

    public static String method1() {
        ThreadUtil.sleep(2000);
        return "Hello";
    }

    public static String method2() {
        ThreadUtil.sleep(3000);
        return "world";
    }
}
