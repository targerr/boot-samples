### 通常对于后端而言，有几个最简单的优化思路
1. 加机器
2. 加缓存
3. 串行改并行
4.

### 并行改造

将串行调用改成并行改造，最容易想到的方式就是每个调用放在独立的线程中执行，然后等待所有线程执行完毕之后在返回结果

1. 使用线程池调用

```java
FutureTask<PageListVo<ArticleDTO>> futureTask = new FutureTask<>(() -> articleList(category.getCategoryId()));
new Thread(futureTask).start();
vo.setArticles(futureTask.get());

```

2. 或者借助线程池来执行

```java 
Future<PageListVo<ArticleDTO>> ans = AsyncUtil.submit(() -> articleList(category.getCategoryId()));
vo.setArticles(ans.get());
```

#### 这里重点介绍一下技术派中借助CompletableFuture来实现并行访问的使用姿势

以下是 `CompletableFuture` 的一些基础知识点：

### 创建 `CompletableFuture`

你可以通过以下几种方式创建一个 `CompletableFuture`：

1. **使用 `CompletableFuture.supplyAsync()` 创建一个带有结果的 `CompletableFuture`**

   ```
   java
   Copy code
   CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
       // 执行异步任务
       return "Hello, CompletableFuture!";
   });
   ```

2. **使用 `CompletableFuture.runAsync()` 创建一个不带结果的 `CompletableFuture`**

   ```
   java
   Copy code
   CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
       // 执行异步任务
       System.out.println("Async task executed!");
   });
   ```

### 异步任务组合

你可以将多个 `CompletableFuture` 组合在一起以实现复杂的异步流程。

1. **thenApply() 和 thenCompose()**

   `thenApply()` 用于处理 `CompletableFuture` 的结果，`thenCompose()` 用于处理 `CompletableFuture` 的结果并返回新的 `CompletableFuture`。

   ```
   java
   Copy code
   CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
       .thenApply(s -> s + " World");
   ```

   ```
   java
   Copy code
   CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
       .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));
   ```

2. **thenAccept() 和 thenRun()**

   `thenAccept()` 和 `thenRun()` 用于消费 `CompletableFuture` 的结果。

   ```
   java
   Copy code
   CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
       .thenAccept(s -> System.out.println("Result: " + s));
   ```

   ```
   java
   Copy code
   CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> "Hello")
       .thenRun(() -> System.out.println("Async task completed!"));
   ```

### 异常处理

你可以使用 `exceptionally()` 或 `handle()` 方法来处理异常。

```
java
Copy code
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error!");
}).exceptionally(ex -> "Handled exception: " + ex.getMessage());

// 或者使用 handle()
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Error!");
}).handle((result, ex) -> {
    if (ex != null) {
        return "Handled exception: " + ex.getMessage();
    }
    return result;
});
```

### 组合多个 `CompletableFuture`

你可以使用 `thenCombine()`, `thenAcceptBoth()`, `applyToEither()`, `runAfterBoth()` 等方法来组合多个 `CompletableFuture`。

```
java
Copy code
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");

// thenCombine()
CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (s1, s2) -> s1 + " " + s2);
```

### 等待 `CompletableFuture` 完成

你可以使用 `join()` 方法来等待 `CompletableFuture` 完成并获取其结果。

```
java
Copy code
String result = future.join();
```

这只是 `CompletableFuture` 的一些基础知识点，它还提供了许多其他方法和功能，如超时、取消、异步异常处理等。熟悉这些基础知识点后，你可以更进一步探索其更高级的功能和用法。



### 并行调度封装工具类 CompletableFutureBridge

为了更方便的实现并行访问调度，技术派中对CompletableFuture做了易用性的封装，并支持耗时打印


```java 

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

```

### 单核心点主要就两块：

1. 并发执行
```
// 添加任务
CompletableFuture future = CompletableFuture.runAsync(run);

// 等待所有任务执行完毕
CompletableFuture.allOf(new CompletableFuture[]{future, ...}).join();
```
2. 记录每个任务的执行耗时

> 注意上面的实现没有用前面提到的StopWatch，主要是因为它非线程安全；因此我们实现了一个简易版本的耗时统计

```java 
// 记录每个任务，以及总的耗时情况
Map<String, Long> cost;

// 任务执行开始前，记录当前时间
private void startRecord(String name) {
    cost.put(name, System.currentTimeMillis());
}

// 任务执行完毕之后，记录任务执行耗时
private void endRecord(String name) {
    long now = System.currentTimeMillis();
    cost.put(name, now - cost.getOrDefault(name, now));
}
```

3. 并发执行调度
   在AysncUtil中，给上面的封装类提供了一个简单的实例化方式

```java 
public static CompletableFutureBridge concurrentExecutor(String... name) {
    if (name.length > 0) {
        return new CompletableFutureBridge(name[0]);
    }
    return new CompletableFutureBridge();
}
```

#### 实现姿势进行替换

```
public IndexVo buildIndexVo(String activeTab) {
    IndexVo vo = new IndexVo();
    CategoryDTO category = categories(activeTab, vo);
    vo.setCategoryId(category.getCategoryId());
    vo.setCurrentCategory(category.getCategory());
    // 并行调度实例，提高响应性能
    AsyncUtil.concurrentExecutor("首页响应")
            .runAsyncWithTimeRecord(() -> vo.setArticles(articleList(category.getCategoryId())), "文章列表")
            .runAsyncWithTimeRecord(() -> vo.setTopArticles(topArticleList(category)), "置顶文章")
            .runAsyncWithTimeRecord(() -> vo.setHomeCarouselList(homeCarouselList()), "轮播图")
            .runAsyncWithTimeRecord(() -> vo.setSideBarItems(sidebarService.queryHomeSidebarList()), "侧边栏")
            .runAsyncWithTimeRecord(() -> vo.setUser(loginInfo()), "用户信息")
            .allExecuted()
            .prettyPrint();
    return vo;
}
```