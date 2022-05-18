### springboot整合 record

#### 案例

第一步：创建starter工程spring-boot-log-record

~~~xml

<dependencies>
    <dependency>
        <groupId>cn.monitor4all</groupId>
        <artifactId>log-record-starter</artifactId>
        <version>1.1.4</version>
    </dependency>
</dependencies>
~~~

第二步：创建application.yml文件

~~~yaml


~~~

第三步： DbLogRecordServiceImpl类

~~~java
package com.example.service;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.IOperationLogGetService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/5/16 17:11
 * @Classname UserParseFunction
 * @Description
 */
@Service
@Slf4j
public class DbLogRecordServiceImpl implements IOperationLogGetService {


    @Override
    public void createLog(LogDTO logDTO) {
        log.info("logDTO: [{}]", JSON.toJSONString(logDTO));
    }
}
~~~

第四步：创建OrderController

~~~java
package com.example.order;

import cn.monitor4all.logRecord.annotation.OperationLog;
import cn.monitor4all.logRecord.context.LogRecordContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/5/16 16:51
 * @Classname OrderController
 * @Description
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @OperationLog(bizType = "addressChange", bizId = "#order.orderNo", msg = "'用户' + #userName + '修改了订单的配送地址：从' + #oldAddress + '修改到' + #order.newAddress")

    @PostMapping("/create")
    public JSONObject createOrder(Order order) {
        log.info("【创建订单】orderNo={}", order.getOrderNo());

        // 手动传递日志上下文：用户信息 地址旧值
        LogRecordContext.putVariables("userName", "王亚茹");
        LogRecordContext.putVariables("oldAddress", "滨江区");
        // db insert order
        Order order1 = new Order();
        order1.setProductName("内部变量测试");
        return JSON.parseObject(JSON.toJSONString(order));
    }
}


~~~

第五步：创建启动类SpringBootLogRecordApplication

~~~java

@SpringBootApplication
public class SpringBootLogRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLogRecordApplication.class, args);
    }

}

~~~

- 执行启动类main方法启动项目，访问地址：127.0.0.1:8080/order/create

* [参考](https://github.com/qqxx6661/logRecord)

