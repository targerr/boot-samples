package com.example;

import com.alibaba.fastjson.JSONObject;
import com.example.param.AppInfoReq;
import com.example.resp.AppHeadInfoResponse;
import com.example.service.AppHeadService;
import com.example.service.ParallelInvokeCommonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wgs
 * @Date 2022/5/24 15:14
 * @Classname CommonServiceTest
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootAsyncTaskApplication.class)
public class CommonServiceTest {
    @Autowired
    private AppHeadService appHeadService;
    @Test
    public void appTest(){
        AppInfoReq req = new AppInfoReq();
        final AppHeadInfoResponse appHeadInfoResponse = appHeadService.parallelQueryAppHeadPageInfo2(req);
        System.out.println(JSONObject.toJSONString(appHeadInfoResponse));

    }
}
