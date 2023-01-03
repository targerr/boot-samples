package com.example;

import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/11/9 11:53
 * @Classname DefinitionTest
 * @Description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class DefinitionTest {

    @Test
    public void buildDefinition() throws URISyntaxException {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setOrder(0);
        routeDefinition.setId("commerce-nacos-client");
        routeDefinition.setUri(new URI("lb://commerce-nacos-client"));

        routeDefinition.setFilters(createDefinitions());

        List<PredicateDefinition> predicates = createPredicateDefinitions();

        routeDefinition.setPredicates(predicates);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(routeDefinition);

        String configInfo = JSONObject.toJSONString(jsonArray);
        log.info("动态路由配置文件: {}", configInfo);

    }

    private List<PredicateDefinition> createPredicateDefinitions() {
        List<PredicateDefinition> predicates = new ArrayList<>();

        PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.setName("Path");

        Map<String, String> pattern = Maps.newHashMap();
        pattern.put("pattern", "/nacos-s/**");
        predicateDefinition.setArgs(pattern);

        predicates.add(predicateDefinition);
        return predicates;
    }

    private List<FilterDefinition> createDefinitions() {
        List<FilterDefinition> filterDefinitions = new ArrayList<>();
        FilterDefinition definition = new FilterDefinition();
        definition.setName("StripPrefix");

        Map<String, String> args = Maps.newHashMap();
        args.put("parts", "1");
        definition.setArgs(args);

        FilterDefinition timeDefinition = new FilterDefinition();
        timeDefinition.setName("time");

        Map<String, String> timeArgs = Maps.newHashMap();
        timeArgs.put("parts", "true");
        timeDefinition.setArgs(args);

        filterDefinitions.add(definition);
        filterDefinitions.add(timeDefinition);
        return filterDefinitions;
    }
}
