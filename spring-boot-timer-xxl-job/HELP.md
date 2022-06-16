### 快速实现xxl-job定时任务

##### 示例

第一步：创建starter工程spring-boot-timer-xxl-job并配置pom.xml文件

~~~xml

<dependencies>
    <!--xxl-job依赖 -->
    <dependency>
        <groupId>com.xuxueli</groupId>
        <artifactId>xxl-job-core</artifactId>
        <version>2.2.0</version>
    </dependency>
</dependencies>
~~~

第二步：配置文件

- 可选

~~~yml
server:
  port: 8082
xxl:
  job:
    admin:
      addresses: http://localhost:8080/xxl-job-admin/
    executor:
      appname: xxl-job-executor-sample
      logpath: /tmp/

~~~

第三步: 配置

```java

package com.example.config;

/**
 * @Author: wgs
 * @Date 2022/5/31 10:38
 * @Classname XxlJobConfig
 * @Description
 */
@Component
@Slf4j
public class XxlJobConfig {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
//        xxlJobSpringExecutor.setIp(ip);
//        xxlJobSpringExecutor.setPort(port);
//        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
//        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }
}

```

第四步: 自定义job

```java
package com.example.job;

/**
 * @Author: wgs
 * @Date 2022/5/31 10:39
 * @Classname MyXxlJob
 * @Description
 */
@Component
@Slf4j
public class MyXxlJob {
    @XxlJob("myXxlJobHandler")
    public ReturnT<String> execute(String param) {
        log.info("myXxlJobHandler execute...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //轮询 流量均摊，推荐
        //故障转移 流量到第一台，查日志方便

        //从数据库里查询所有用户，为每一个用户生成结单
        //10w用户，2台服务器，每台运算5w用户数据，耗时减半
        ShardingUtil.ShardingVO shardingVo = ShardingUtil.getShardingVo();

        //分片，必须掌握
//        List<Integer> list = Arrays.asList(1, 2, 3, 4);
//        for (Integer i : list) {
//            if (i % shardingVo.getTotal() == shardingVo.getIndex()) {
//                log.info("myXxlJobHandler execute...user={}. shardingVo={}",
//                        i, new Gson().toJson(shardingVo));
//            }
//        }

        XxlJobLogger.log("myXxlJobHandler execute...");
        return ReturnT.SUCCESS;
    }
}

```

第五步: 调度器控制台

- git clone https://github.com/xuxueli/xxl-job.git
- 数据库脚本地址：/xxl-job/doc/db/tables_xxl_job.sql
- 日志配置(<property name="log.path" value="logs/xxl-job/xxl-job-admin.log"/>)
- 修改配置文件(数据库信息)

##### 5. 配置定时任务

##### 5.1. 将启动的执行器添加到调度中心

执行器管理 - 新增执行器

![image-20190808105910203](https://static.xkcoding.com/spring-boot-demo/2019-08-08-025910.png)

##### 5.2. 添加定时任务

任务管理 - 新增 - 保存

![image-20190808110127956](https://static.xkcoding.com/spring-boot-demo/2019-08-08-030128.png)

##### 5.3. 启停定时任务

第六步: 使用API添加定时任务

> 实际场景中，如果添加定时任务都需要手动在 xxl-job-admin 去操作，这样可能比较麻烦，用户更希望在自己的页面，添加定时任务参数、定时调度表达式，然后通过 API 的方式添加定时任务

### 6.1. 改造xxl-job-admin

* 去除权限校验

![image.png](https://upload-images.jianshu.io/upload_images/4994935-43bc0b5140e6f9c1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 6.1.1. 修改 JobGroupController.java

```java

@RequestMapping("/list")
@ResponseBody
// 去除权限校验
@PermissionLimit(limit = false)

```

#### 6.1.2. 修改 JobInfoController.java

```java
// 分别在 pageList、add、update、remove、pause、start、triggerJob 方法上添加注解，去除权限校验
@PermissionLimit(limit = false)
```

### 6.2. 改造 执行器项目

#### 6.2.1. 添加手动触发类

```java
/**
 * <p>
 * 手动操作 xxl-job
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019-08-07 14:58
 */
@Slf4j
@RestController
@RequestMapping("/xxl-job")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ManualOperateController {
    private final static String baseUri = "http://127.0.0.1:18080/xxl-job-admin";
    private final static String JOB_INFO_URI = "/jobinfo";
    private final static String JOB_GROUP_URI = "/jobgroup";

    /**
     * 任务组列表，xxl-job叫做触发器列表
     */
    @GetMapping("/group")
    public String xxlJobGroup() {
        HttpResponse execute = HttpUtil.createGet(baseUri + JOB_GROUP_URI + "/list").execute();
        log.info("【execute】= {}", execute);
        return execute.body();
    }

    /**
     * 分页任务列表
     *
     * @param page 当前页，第一页 -> 0
     * @param size 每页条数，默认10
     * @return 分页任务列表
     */
    @GetMapping("/list")
    public String xxlJobList(Integer page, Integer size) {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("start", page != null ? page : 0);
        jobInfo.put("length", size != null ? size : 10);
        jobInfo.put("jobGroup", 2);
        jobInfo.put("triggerStatus", -1);

        HttpResponse execute = HttpUtil.createGet(baseUri + JOB_INFO_URI + "/pageList").form(jobInfo).execute();
        log.info("【execute】= {}", execute);
        return execute.body();
    }

    /**
     * 测试手动保存任务
     */
    @GetMapping("/add")
    public String xxlJobAdd() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("jobGroup", 2);
        jobInfo.put("jobCron", "0 0/1 * * * ? *");
        jobInfo.put("jobDesc", "手动添加的任务");
        jobInfo.put("author", "admin");
        jobInfo.put("executorRouteStrategy", "ROUND");
        jobInfo.put("executorHandler", "demoTask");
        jobInfo.put("executorParam", "手动添加的任务的参数");
        jobInfo.put("executorBlockStrategy", ExecutorBlockStrategyEnum.SERIAL_EXECUTION);
        jobInfo.put("glueType", GlueTypeEnum.BEAN);

        HttpResponse execute = HttpUtil.createGet(baseUri + JOB_INFO_URI + "/add").form(jobInfo).execute();
        log.info("【execute】= {}", execute);
        return execute.body();
    }

    /**
     * 测试手动触发一次任务
     */
    @GetMapping("/trigger")
    public String xxlJobTrigger() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("id", 4);
        jobInfo.put("executorParam", JSONUtil.toJsonStr(jobInfo));

        HttpResponse execute = HttpUtil.createGet(baseUri + JOB_INFO_URI + "/trigger").form(jobInfo).execute();
        log.info("【execute】= {}", execute);
        return execute.body();
    }

    /**
     * 测试手动删除任务
     */
    @GetMapping("/remove")
    public String xxlJobRemove() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("id", 4);

        HttpResponse execute = HttpUtil.createGet(baseUri + JOB_INFO_URI + "/remove").form(jobInfo).execute();
        log.info("【execute】= {}", execute);
        return execute.body();
    }

    /**
     * 测试手动停止任务
     */
    @GetMapping("/stop")
    public String xxlJobStop() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("id", 4);

        HttpResponse execute = HttpUtil.createGet(baseUri + JOB_INFO_URI + "/stop").form(jobInfo).execute();
        log.info("【execute】= {}", execute);
        return execute.body();
    }

    /**
     * 测试手动启动任务
     */
    @GetMapping("/start")
    public String xxlJobStart() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("id", 4);

        HttpResponse execute = HttpUtil.createGet(baseUri + JOB_INFO_URI + "/start").form(jobInfo).execute();
        log.info("【execute】= {}", execute);
        return execute.body();
    }

}
```

## 参考

- [《分布式任务调度平台xxl-job》](http://www.xuxueli.com/xxl-job/#/)



