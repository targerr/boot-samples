package com.example;

import cn.hutool.core.io.FileUtil;
import com.example.utils.PdfUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/5/20 11:21
 * @Classname PdfTest
 * @Description
 */
public class PdfTest {

    public static void main(String[] args) throws Exception {
        byte[] bytes = FileUtil.readBytes("/Users/Downloads/1.pdf");
        Map<String, String> map = new HashMap<>();
        map.put("Name", "tom");
        map.put("Age", "23");
        String address = "/Users/Downloads/2.pdf";
        PdfUtils.generate(bytes, address, map, null);
        System.out.println("-------");
//        png("/Users/Downloads/", "/Users//Downloads/2.pdf", "192847040_0brokerQR");
//        pdf2Pic("/Users/Downloads/", "/Users/Downloads/22f3bB1E6af.pdf", "22f3bBE6af");
    }

}
