package com.example.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.TestDemo;
import com.example.bo.TestDemoImportVo;
import com.example.excel.ExcelResult;
import com.example.utils.ExcelUtil;
import com.example.vo.TestDemoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Author: wgs
 * @Date 2022/7/4 10:55
 * @Classname EasyExcelController
 * @Description
 */
@RestController
@RequestMapping("/excel")
public class EasyExcelController {
    /**
     * 单列表多数据
     */
    @GetMapping("/exportTemplateOne")
    public void exportTemplateOne(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("title", "单列表多数据");
        map.put("test1", "数据测试1");
        map.put("test2", "数据测试2");
        map.put("test3", "数据测试3");
        map.put("test4", "数据测试4");
        map.put("testTest", "666");
        List<TestObj> list = new ArrayList<>();
        list.add(new TestObj("单列表测试1", "列表测试1", "列表测试2", "列表测试3", "列表测试4"));
        list.add(new TestObj("单列表测试2", "列表测试5", "列表测试6", "列表测试7", "列表测试8"));
        list.add(new TestObj("单列表测试3", "列表测试9", "列表测试10", "列表测试11", "列表测试12"));
        ExcelUtil.exportTemplate(CollUtil.newArrayList(map, list), "单列表.xlsx", "excel/单列表.xlsx", response);
    }

    /**
     * 多列表多数据
     */
    @GetMapping("/exportTemplateMuliti")
    public void exportTemplateMuliti(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("title1", "标题1");
        map.put("title2", "标题2");
        map.put("title3", "标题3");
        map.put("title4", "标题4");
        map.put("author", "Lion Li");
        List<TestObj1> list1 = new ArrayList<>();
        list1.add(new TestObj1("list1测试1", "list1测试2", "list1测试3"));
        list1.add(new TestObj1("list1测试4", "list1测试5", "list1测试6"));
        list1.add(new TestObj1("list1测试7", "list1测试8", "list1测试9"));
        List<TestObj1> list2 = new ArrayList<>();
        list2.add(new TestObj1("list2测试1", "list2测试2", "list2测试3"));
        list2.add(new TestObj1("list2测试4", "list2测试5", "list2测试6"));
        List<TestObj1> list3 = new ArrayList<>();
        list3.add(new TestObj1("list3测试1", "list3测试2", "list3测试3"));
        List<TestObj1> list4 = new ArrayList<>();
        list4.add(new TestObj1("list4测试1", "list4测试2", "list4测试3"));
        list4.add(new TestObj1("list4测试4", "list4测试5", "list4测试6"));
        list4.add(new TestObj1("list4测试7", "list4测试8", "list4测试9"));
        list4.add(new TestObj1("list4测试10", "list4测试11", "list4测试12"));
        Map<String, Object> multiListMap = new HashMap<>();
        multiListMap.put("map", map);
        multiListMap.put("data1", list1);
        multiListMap.put("data2", list2);
        multiListMap.put("data3", list3);
        multiListMap.put("data4", list4);
        ExcelUtil.exportTemplateMultiList(multiListMap, "多列表.xlsx", "excel/多列表.xlsx", response);
    }


    /**
     * 导出测试单表列表
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        TestDemoVo vo = new TestDemoVo();
        vo.setId(1L);
        vo.setUserId(1L);
        vo.setCreateBy("id");
        vo.setDeptId(2L);
        vo.setOrderNum(2);
        vo.setValue("测试");
        vo.setOrderNum(RandomUtil.randomInt());
        vo.setCreateTime(new Date());
        // List<TestDemoVo> list = iTestDemoService.queryList(bo);
        List<TestDemoVo> list = Arrays.asList(vo);
        ExcelUtil.exportExcel(list, "测试单表", TestDemoVo.class, response);
    }

    @PostMapping("/importData")
    public String importData(@RequestPart("file") MultipartFile file) throws IOException {
        ExcelResult<TestDemoImportVo> excelResult = ExcelUtil.importExcel(file.getInputStream(), TestDemoImportVo.class, true);
        List<TestDemoImportVo> volist = excelResult.getList();
        List<TestDemo> list = BeanUtil.copyToList(volist, TestDemo.class);
        System.err.println(JSONObject.toJSONString(list));
        return "ok";
    }

    @Data
    @AllArgsConstructor
    static class TestObj1 {
        private String test1;
        private String test2;
        private String test3;
    }

    @Data
    @AllArgsConstructor
    static class TestObj {
        private String name;
        private String list1;
        private String list2;
        private String list3;
        private String list4;
    }

}
