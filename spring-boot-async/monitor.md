### 线程池异步任务异常捕获处理器

### 一、 配置类

```java
package com.example.config;

/**
 自定义异步任务线程池, 异步任务异常捕获处理器
 */
@EnableAsync// 开启 Spring 异步任务支持
@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 设置核心线程数
        executor.setCorePoolSize(2);
        // 设置最大线程数
        executor.setMaxPoolSize(2);
        // 线程池所使用的缓冲队列
        executor.setQueueCapacity(2);
        executor.setThreadNamePrefix("Qinyi-Async-");   // 这个非常重要

        // 等待所有任务结果候再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        // 定义拒绝策略
        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        // 初始化线程池, 初始化 core 线程
        executor.initialize();

        return executor;
    }

    /**
     * 知道系统中异步线程出现异常所用到的处理器
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    /**
     * <h2>异步任务异常捕获处理器</h2>
     */
    @SuppressWarnings("all")
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method,
                                            Object... objects) {

            throwable.printStackTrace();
            log.error("线程池错误: [{}], 方法: [{}], 参数: [{}]",
                    throwable.getMessage(), method.getName(),
                    JSON.toJSONString(objects));

            // TODO 发送邮件或者是短信, 做进一步的报警处理
        }
    }
}


```

### 二、 异步任务执行信息

- 线程状态枚举类

```java

@Getter
@AllArgsConstructor
public enum AsyncTaskStatusEnum {

    STARTED(0, "已经启动"),
    RUNNING(1, "正在运行"),
    SUCCESS(2, "执行成功"),
    FAILED(3, "执行失败"),
    ;

    /** 执行状态编码 */
    private final int state;

    /** 执行状态描述 */
    private final String stateInfo;
}


```

- 任务执行信息类

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTaskInfo {

    /** 异步任务 id */
    private String taskId;

    /** 异步任务执行状态 */
    private AsyncTaskStatusEnum status;

    /** 异步任务开始时间 */
    private Date startTime;

    /** 异步任务结束时间 */
    private Date endTime;

    /** 异步任务总耗时 */
    private String totalTime;
}
```

- 异步任务执行管理器

```java


@Slf4j
@Component
public class AsyncTaskManager {
    @Autowired
    private AsyncService asyncService;

    /**
     * 管理线程容器
     */
    Map<String, AsyncTaskInfo> taskContainer = new HashMap<>(16);

    /**
     * <h2>初始化异步任务</h2>
     */
    public AsyncTaskInfo initTask() {

        AsyncTaskInfo taskInfo = new AsyncTaskInfo();
        // 设置一个唯一的异步任务 id, 只要唯一即可
        taskInfo.setTaskId(UUID.randomUUID().toString());
        taskInfo.setStatus(AsyncTaskStatusEnum.STARTED);
        taskInfo.setStartTime(new Date());

        // 初始化的时候就要把异步任务执行信息放入到存储容器中
        taskContainer.put(taskInfo.getTaskId(), taskInfo);
        return taskInfo;
    }

    /**
     * <h2>提交异步任务</h2>
     */
    public AsyncTaskInfo submit() {
        // 初始化一个异步任务的监控信息
        AsyncTaskInfo taskInfo = initTask();
        asyncService.asyncImportGoods(taskInfo.getTaskId());
        return taskInfo;
    }

    /**
     * <h2>设置异步任务执行状态信息</h2>
     */
    public void setTaskInfo(AsyncTaskInfo taskInfo) {
        taskContainer.put(taskInfo.getTaskId(), taskInfo);
    }

    /**
     * <h2>获取异步任务执行状态信息</h2>
     */
    public AsyncTaskInfo getTaskInfo(String taskId) {
        return taskContainer.get(taskId);
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

- 业务方法类

```java

@Service
@Slf4j
public class AsyncService {
    @Async
    public void asyncImportGoods(String taskId) {
        log.info("任务 Id: {}", taskId);
        execute();
    }

    private void execute() {
        final StopWatch stopWatch = DateUtil.createStopWatch();
        stopWatch.start("异步线程进入");
        ThreadUtil.sleep(1000L);
        stopWatch.stop();

        stopWatch.start("异步线程执行");
        log.info("线程名称: {}", Thread.currentThread().getName());
        ThreadUtil.sleep(1000L);
        stopWatch.stop();

        log.info("线程信息: {}", stopWatch.prettyPrint());
    }
}

```

- aop 监控类

```java

@Slf4j
@Aspect
@Component
public class AsyncTaskMonitor {
    /** 注入异步任务管理器 */
    private final AsyncTaskManager asyncTaskManager;

    public AsyncTaskMonitor(AsyncTaskManager asyncTaskManager) {
        this.asyncTaskManager = asyncTaskManager;
    }

    /**
     * <h2>异步任务执行的环绕切面</h2>
     * 环绕切面让我们可以在方法执行之前和执行之后做一些 "额外" 的操作
     * 后期使用注解代替
     */
    @Around("execution(* com.example.service.AsyncService.*(..))")
    public Object taskHandle(ProceedingJoinPoint proceedingJoinPoint) {

        // 获取 taskId, 调用异步任务传入的第二个参数
        String taskId = proceedingJoinPoint.getArgs()[0].toString();

        // 获取任务信息, 在提交任务的时候就已经放入到容器中了
        AsyncTaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);
        log.info("异步任务监控线程 id: [{}]", taskId);

        taskInfo.setStatus(AsyncTaskStatusEnum.RUNNING);
        asyncTaskManager.setTaskInfo(taskInfo); // 设置为运行状态, 并重新放入容器

        AsyncTaskStatusEnum status;
        Object result;

        try {
            // 执行异步任务
            result = proceedingJoinPoint.proceed();
            status = AsyncTaskStatusEnum.SUCCESS;
        } catch (Throwable ex) {
            // 异步任务出现了异常
            result = null;
            status = AsyncTaskStatusEnum.FAILED;
            log.error("AsyncTaskMonitor: async task [{}] is failed, Error Info: [{}]",
                    taskId, ex.getMessage(), ex);
        }

        // 设置异步任务其他的信息, 再次重新放入到容器中
        taskInfo.setEndTime(new Date());
        taskInfo.setStatus(status);
        taskInfo.setTotalTime(String.valueOf(
                taskInfo.getEndTime().getTime() - taskInfo.getStartTime().getTime()
        ));
        asyncTaskManager.setTaskInfo(taskInfo);

        return result;
    }
}

```

### 三、测试

```java

@RestController
@RequestMapping("/async")
@Slf4j
public class AsyncController {
    @Autowired
    private AsyncTaskManager asyncTaskManager;

    @GetMapping("/executeTask")
    public String executeTask() {

        AsyncTaskInfo asyncTaskInfo = asyncTaskManager.submit();

        return asyncTaskInfo.getTaskId();
    }

    @GetMapping("/task-info")
    public String getTaskInfo(@RequestParam String taskId) {
        return asyncTaskManager.getTaskInfo(taskId).getStatus().getStateInfo();
    }
}

```