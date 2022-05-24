### springboot整合 async-task

#### 案例

第一步：spring-boot-async-task

~~~xml

<dependencies>
    <!-- 工具类大全 -->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
</dependencies>
~~~

第二步：创建application.yml文件

~~~yaml
logging:
  level:
    com.example: debug

~~~

第三步： ParallelInvokeCommonService 线程处理类

~~~java
package com.example.service;

@Service
public class ParallelInvokeCommonService {
    public List<BaseRspDTO<Object>> executeTask(List<Callable<BaseRspDTO<Object>>> taskList, ExecutorService executor) {

        List<BaseRspDTO<Object>> resultList = new ArrayList<>();
        //校验参数
        if (taskList == null || taskList.size() == 0) {
            return resultList;
        }

        CompletionService<BaseRspDTO<Object>> baseDTOCompletionService = new ExecutorCompletionService<BaseRspDTO<Object>>(executor);
        //提交任务
        for (Callable<BaseRspDTO<Object>> task : taskList) {
            baseDTOCompletionService.submit(task);
        }

        try {
            //遍历获取结果
            for (int i = 0; i < taskList.size(); i++) {
                Future<BaseRspDTO<Object>> baseRspDTOFuture = baseDTOCompletionService.poll(10, TimeUnit.SECONDS);
                resultList.add(baseRspDTOFuture.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
~~~

第四步：创建HandlerHolder

~~~java
package com.example.service;


/**
 * @Author: wgs
 * @Date 2022/5/24 13:54
 * @Classname HandlerHolder
 * @Description service->taskHandler的映射关系
 */
@Component
public class HandlerHolder {
    private Map<ServiceStrategy, TaskHandler> TASK_HANDLER_MAP = new HashMap<>(128);

    public HandlerHolder(List<TaskHandler> taskHandlerList) {
        taskHandlerList.forEach(taskHandler -> TASK_HANDLER_MAP.put(taskHandler.getTaskType(), taskHandler));
    }

    public TaskHandler route(ServiceStrategy serviceStrategy) {
        return TASK_HANDLER_MAP.get(serviceStrategy);
    }
}

~~~

第五步：创建TaskHandler

~~~java
package com.example.service;

import com.example.dto.BaseRspDTO;
import com.example.enums.ServiceStrategy;
import com.example.param.AppInfoReq;

/**
 * @Author: wgs
 * @Date 2022/5/24 13:52
 * @Classname TaskHandler
 * @Description
 */
public interface TaskHandler {

    /**
     * 1.指定子类
     * 2.策略类的key，如是usetInfoDTO还是bannerDTO，还是labelDTO
     * @return
     */
    ServiceStrategy getTaskType();

    /**
     * 处理器
     * @param req
     * @return
     */
    BaseRspDTO<Object> execute(AppInfoReq req);

}
~~~

第六步：创建启动类BannerStrategyTask

~~~java
package com.example.service;


@Service
@Slf4j
public class BannerStrategyTask implements TaskHandler {

    @Override
    public ServiceStrategy getTaskType() {
        return ServiceStrategy.BANNER;
    }

    @Override
    public BaseRspDTO<Object> execute(AppInfoReq req) {
        log.debug("banner 查询执行 线程Id:{}", Thread.currentThread().getId());
        BaseRspDTO<Object> baseRspDTO = new BaseRspDTO<Object>();
        baseRspDTO.setKey(getTaskType());
        baseRspDTO.setData(BannerDTO.builder().msg("查询banner图!").build());
        ThreadUtil.sleep(3000);
        return baseRspDTO;
    }

}

~~~

第七步:单元测试

```java

@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootAsyncTaskApplication.class)
public class CommonServiceTest {
    @Autowired
    private AppHeadService appHeadService;

    @Test
    public void appTest() {
        AppInfoReq req = new AppInfoReq();
        final AppHeadInfoResponse appHeadInfoResponse = appHeadService.parallelQueryAppHeadPageInfo2(req);
        System.out.println(JSONObject.toJSONString(appHeadInfoResponse));

    }
}

```

简单示例

```java
public class BaseRspDTO<T extends Object> {

    //区分是DTO返回的唯一标记，比如是UserInfoDTO还是BannerDTO
    private String key;
    //返回的data
    private T data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

    //并行查询App首页信息
    public AppHeadInfoResponse parallelQueryAppHeadPageInfo(AppInfoReq req) {

        long beginTime = System.currentTimeMillis();
        System.out.println("开始并行查询app首页信息，开始时间：" + beginTime);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletionService<BaseRspDTO<Object>> baseDTOCompletionService = new ExecutorCompletionService<BaseRspDTO<Object>>(executor);

        //查询用户信息任务
        Callable<BaseRspDTO<Object>> userInfoDTOCallableTask = () -> {
            UserInfoParam userInfoParam = buildUserParam(req);
            UserInfoDTO userInfoDTO = userService.queryUserInfo(userInfoParam);
            BaseRspDTO<Object> userBaseRspDTO = new BaseRspDTO<Object>();
            userBaseRspDTO.setKey("userInfoDTO");
            userBaseRspDTO.setData(userInfoDTO);
            return userBaseRspDTO;
        };

        //banner信息查询任务
        Callable<BaseRspDTO<Object>> bannerDTOCallableTask = () -> {
            BannerParam bannerParam = buildBannerParam(req);
            BannerDTO bannerDTO = bannerService.queryBannerInfo(bannerParam);
            BaseRspDTO<Object> bannerBaseRspDTO = new BaseRspDTO<Object>();
            bannerBaseRspDTO.setKey("bannerDTO");
            bannerBaseRspDTO.setData(bannerDTO);
            return bannerBaseRspDTO;
        };

        //label信息查询任务
        Callable<BaseRspDTO<Object>> labelDTODTOCallableTask = () -> {
            LabelParam labelParam = buildLabelParam(req);
            LabelDTO labelDTO = labelService.queryLabelInfo(labelParam);
            BaseRspDTO<Object> labelBaseRspDTO = new BaseRspDTO<Object>();
            labelBaseRspDTO.setKey("labelDTO");
            labelBaseRspDTO.setData(labelDTO);
            return labelBaseRspDTO;
        };

        //提交用户信息任务
        baseDTOCompletionService.submit(userInfoDTOCallableTask);
        //提交banner信息任务
        baseDTOCompletionService.submit(bannerDTOCallableTask);
        //提交label信息任务
        baseDTOCompletionService.submit(labelDTODTOCallableTask);

        UserInfoDTO userInfoDTO = null;
        BannerDTO bannerDTO = null;
        LabelDTO labelDTO = null;

        try {
            //因为提交了3个任务，所以获取结果次数是3
            for (int i = 0; i < 3; i++) {
                Future<BaseRspDTO<Object>> baseRspDTOFuture = baseDTOCompletionService.poll(1, TimeUnit.SECONDS);
                BaseRspDTO baseRspDTO = baseRspDTOFuture.get();
                if ("userInfoDTO".equals(baseRspDTO.getKey())) {
                    userInfoDTO = (UserInfoDTO) baseRspDTO.getData();
                } else if ("bannerDTO".equals(baseRspDTO.getKey())) {
                    bannerDTO = (BannerDTO) baseRspDTO.getData();
                } else if ("labelDTO".equals(baseRspDTO.getKey())) {
                    labelDTO = (LabelDTO) baseRspDTO.getData();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("结束并行查询app首页信息,总耗时：" + (System.currentTimeMillis() - beginTime));
        return buildResponse(userInfoDTO, bannerDTO, labelDTO);
    }
```

- [参考](https://mp.weixin.qq.com/s/IMRBOOm3j23XJda_jlwG3g)