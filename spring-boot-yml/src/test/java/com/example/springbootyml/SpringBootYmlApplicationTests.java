package com.example.springbootyml;

import com.alibaba.fastjson.JSONObject;
import com.example.springbootyml.config.PersonProperties;
import com.example.springbootyml.config.TaikangProperties;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class SpringBootYmlApplicationTests {
    @Autowired
    private TaikangProperties taiKangConfig;

    @Autowired
    private PersonProperties personProperties;

    @Test
    public void testTaiKangConfig() {
        System.out.println(JSONObject.toJSONString(taiKangConfig, true));

        TaikangProperties.BaseProperties monthConfig = taiKangConfig.getMedical().getMonthConfig();
        System.out.println(JSONObject.toJSONString(monthConfig, true));

    }

    @Test
    public void personConfig() {
        System.out.println(JSONObject.toJSONString(personProperties, true));


    }


}
