package com.example.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/7/4 11:01
 * @Classname IndexController
 * @Description
 */
@RestController
@RequestMapping("/hutool")
public class HutoolExcelController {
    @GetMapping("/export")
    public String index(HttpServletResponse response) {

        List<User> list = new ArrayList<>();
        User obj = new User();
        obj.setName("卡卡罗特");
        obj.setAge("25");
        obj.setBirthDay("0903");
        list.add(obj);
        list.add(new User());
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        // 写入excel
        addExcelWriter(list, writer);
        // TODO 写入OSS
        // ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // writer.flush(outputStream, true);
        // OssUtils.upload(outputStream, title);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String name = "test";
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xls");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        IoUtil.close(out);

        return "ok";
    }


    @PostMapping("/importData")
    public String importData(@RequestPart("file") MultipartFile file) throws IOException {
        final ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        final List<List<Object>> read = reader.read();
        System.out.println(JSONObject.toJSONString(read));

        return "ok";
    }

    private void addExcelWriter(List<User> list, ExcelWriter writer) {
        //自定义标题别名
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("age", "年龄");
        writer.addHeaderAlias("birthDay", "生日");
        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(2, "申请人员信息");
        writer.write(list, true);
    }


    @Data
    static class User {
        private String name;
        private String birthDay;
        private String age;
    }
}
