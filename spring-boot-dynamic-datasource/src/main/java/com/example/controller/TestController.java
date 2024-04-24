package com.example.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.dal.DsAno;
import com.example.dal.DsSelectExecutor;
import com.example.dal.MasterSlaveDsEnum;
import com.example.pojo.Document;
import com.example.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wgs
 * @Date 2024/4/23 17:16
 * @Classname TestController
 * @Description
 */
@Slf4j
@DsAno(MasterSlaveDsEnum.SLAVE)
@RestController
@RequestMapping(path = "test")
public class TestController {
    @Autowired
    private DocumentService documentService;

    @GetMapping(path = "/selectOne")
    @ResponseBody
    public JSONObject selectOne(String id) {
        final Document document = documentService.getById(id);
        return JSONObject.parseObject(JSONObject.toJSONString(document));
    }

    @PostMapping(path = "/save")
    @ResponseBody
    public Boolean save(String name) {
        Document document = new Document();
        document.setName(name);
        document.setDetails("描述");
        DsSelectExecutor.execute(MasterSlaveDsEnum.MASTER, () -> documentService.save(document));
        documentService.save(document);
        return true;
    }
}
