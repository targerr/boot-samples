package com.example.springbootyml;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springbootyml.config.PersonProperties;
import com.example.springbootyml.config.TaikangProperties;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class SpringBootYmlApplicationTests {
    @Autowired
    private TaikangProperties taiKangConfig;

    @Autowired
    private PersonProperties personProperties;
    @Autowired
    private JsonConfigLoader jsonConfigLoader;

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


    @Test
    public void mapConfig() {
        final Map<String, Object> configMap = jsonConfigLoader.getConfigMap();
        System.out.println(JSONObject.toJSONString(configMap, true));

        System.out.println("百万医疗基础:");
        System.out.println("景点游老年款的3节点:");

        final List<Item> itemList = getVersionAndDurationJson(configMap, "景点游老年款1", "30");
        System.out.println("------------------------------------------------");
        System.out.println(JSONObject.toJSONString(itemList, true));

    }


    @Data
    static class Item {
        private String amount;
        private String kindCode;
        private String premium;
    }


    private static List<Item> getVersionAndDurationJson(Map<String, Object> configMap, String key, String subdata) {

        JSONObject jsonObject = new JSONObject(configMap);
        if (StringUtils.isBlank(subdata)) {
            return JSONArray.parseArray(jsonObject.getJSONArray(key).toJSONString(), Item.class);
        }

        JSONObject json = jsonObject.getJSONObject(key);
        return JSONArray.parseArray(json.getJSONArray(subdata).toJSONString(), Item.class);
    }


}
