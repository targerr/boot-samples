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
