package com.framework.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.framework.web.entity.City;
import com.framework.web.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2024/9/4 15:51
 * @Classname TestController
 * @Description
 */

@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityService cityService;





    @GetMapping("/getCity")
    public JSONObject getCity(String id) {
//        DynamicDataSourceContextHolder.setDataSourceType("SLAVE");
        final City byId = cityService.selcetId(id);

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
