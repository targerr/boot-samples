package example.controller;

import com.alibaba.fastjson.JSON;
import example.config.properties.GetuiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2023/5/15 11:34
 * @Classname TestController
 * @Description
 */
@RestController
@RequestMapping("/")
@Slf4j
public class TestController {
    @Autowired
    private GetuiProperties getuiProperties;

    @GetMapping("/getInfo")
    public void getInfo(){
      log.error(JSON.toJSONString(getuiProperties,true));
    }
}
