package com.framework.web.controller;/*
package com.example.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.datasource.annotations.RequestRequired;
import com.example.entity.City;
import com.example.service.CityService;
import com.github.log.MyLog;
import com.github.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

*/
/**
 * @Author: wgs
 * @Date 2024/9/4 15:51
 * @Classname TestController
 * @Description
 *//*

@RequestRequired
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private CityService cityService;

    @Autowired
    private HelloService helloService;


    @MyLog(desc = "getById方法")
    @GetMapping("/getCity")
    public JSONObject getCity(String id) {
        final City byId = cityService.getById(id);
        System.out.println("byId = " + helloService.sayHello());
        return JSONObject.parseObject(JSONObject.toJSONString(byId));

    }


    @GetMapping("/getList")
    public JSONObject getList() {
        final List<City> cityList = cityService.list();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cityList", cityList);
        return jsonObject;

    }
}
*/