## spring-boot-async
> 演示异步编程
### 配置类
- @EnableAsync注解可以直接放在SpringBoot启动类上，也可以单独放在其他配置类上

```java

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //设置核心线程数
        threadPool.setCorePoolSize(10);
        //设置最大线程数
        threadPool.setMaxPoolSize(100);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(10);
        // 拒绝策略， 由当前执行线程执行
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(60);
        //  线程名称前缀
        threadPool.setThreadNamePrefix("async-");
        // 初始化线程
        threadPool.initialize();
        return threadPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}

```

### 方法标记 @Async注解

```java

@Component
@Slf4j
public class AsyncJob {

    @Async
    public void job() throws InterruptedException {
        doTask("job", 1);
    }

    /**
     * 模拟5秒的异步任务
     */
    @Async
    public Future<Boolean> job1() throws InterruptedException {
        doTask("job1", 5);
        return new AsyncResult<>(Boolean.TRUE);
    }

    /**
     * 模拟2秒的异步任务
     */
    @Async
    public Future<Boolean> job2() throws InterruptedException {
        doTask("job2", 2);
        return new AsyncResult<>(Boolean.TRUE);
    }

    /**
     * 模拟3秒的异步任务
     */
    @Async
    public Future<Boolean> job3() throws InterruptedException {
        doTask("job3", 3);
        return new AsyncResult<>(Boolean.TRUE);
    }

    /**
     * 模拟5秒的同步任务
     */
    public void task1() throws InterruptedException {
        doTask("task1", 5);
    }

    /**
     * 模拟2秒的同步任务
     */
    public void task2() throws InterruptedException {
        doTask("task2", 2);
    }

    /**
     * 模拟3秒的同步任务
     */
    public void task3() throws InterruptedException {
        doTask("task3", 3);
    }

    private void doTask(String taskName, Integer time) throws InterruptedException {
        Long startTime = System.currentTimeMillis();
        log.info("{}开始执行，当前线程名称【{}】", taskName, Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(time);
        log.info("{}执行成功，当前线程名称【{}】耗时 {} ms", taskName, Thread.currentThread().getName(), System.currentTimeMillis() - startTime);
    }
}

```

### 单元测试
```java

    @Autowired
    private AsyncJob asyncJob;

    /**
     * 测试异步任务
     */
    @Test
    public void asyncTaskTest() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        Future<Boolean> asyncTask1 = asyncJob.job1();
        Future<Boolean> asyncTask2 = asyncJob.job2();
        Future<Boolean> asyncTask3 = asyncJob.job3();

        // 获取异步任务的处理结果，异步任务没有处理完成，会一直阻塞，可以设置超时时间，使用 get 的重载方法
        asyncTask1.get();
        asyncTask2.get();
        asyncTask3.get();
        long endTime = System.currentTimeMillis();

        log.info("异步任务，总耗时：{} 毫秒", (endTime - startTime));

    }


    /**
     * 测试同步任务
     */
    @Test
    public void taskTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        asyncJob.task1();
        asyncJob.task2();
        asyncJob.task3();
        long endTime = System.currentTimeMillis();

        log.info("同步任务，总耗时：{} 毫秒", (endTime - startTime));
    }
```

### 运行结果    
异步结果
```java
2022-04-18 16:57:36.729  INFO 62093 --- [        async-2] com.example.job.AsyncJob                 : job2开始执行，当前线程名称【async-2】
2022-04-18 16:57:36.729  INFO 62093 --- [        async-3] com.example.job.AsyncJob                 : job3开始执行，当前线程名称【async-3】
2022-04-18 16:57:36.729  INFO 62093 --- [        async-1] com.example.job.AsyncJob                 : job1开始执行，当前线程名称【async-1】
2022-04-18 16:57:38.736  INFO 62093 --- [        async-2] com.example.job.AsyncJob                 : job2执行成功，当前线程名称【async-2】耗时 2007 ms
2022-04-18 16:57:39.733  INFO 62093 --- [        async-3] com.example.job.AsyncJob                 : job3执行成功，当前线程名称【async-3】耗时 3004 ms
2022-04-18 16:57:41.736  INFO 62093 --- [        async-1] com.example.job.AsyncJob                 : job1执行成功，当前线程名称【async-1】耗时 5007 ms
2022-04-18 16:57:41.736  INFO 62093 --- [           main] c.e.SpringBootAsyncApplicationTests      : 异步任务，总耗时：5015 毫秒

```
同步结果
```java
2022-04-18 17:14:44.911  INFO 62613 --- [           main] com.example.job.AsyncJob                 : task1开始执行，当前线程名称【main】
2022-04-18 17:14:49.916  INFO 62613 --- [           main] com.example.job.AsyncJob                 : task1执行成功，当前线程名称【main】耗时 5005 ms
2022-04-18 17:14:49.917  INFO 62613 --- [           main] com.example.job.AsyncJob                 : task2开始执行，当前线程名称【main】
2022-04-18 17:14:51.922  INFO 62613 --- [           main] com.example.job.AsyncJob                 : task2执行成功，当前线程名称【main】耗时 2005 ms
2022-04-18 17:14:51.923  INFO 62613 --- [           main] com.example.job.AsyncJob                 : task3开始执行，当前线程名称【main】
2022-04-18 17:14:54.928  INFO 62613 --- [           main] com.example.job.AsyncJob                 : task3执行成功，当前线程名称【main】耗时 3005 ms
2022-04-18 17:14:54.928  INFO 62613 --- [           main] c.e.SpringBootAsyncApplicationTests      : 同步任务，总耗时：10022 毫秒
```