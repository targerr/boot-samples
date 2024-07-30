package service;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.datasource.annotation.DataSource;
import com.example.pojo.Document;
import com.example.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: wgs
 * @Date 2024/4/24 11:20
 * @Classname service.DynamicDataSourceTestService
 * @Description
 */
@Service
//@DataSource("master")
public class DynamicDataSourceTestService {
    @Autowired
    private DocumentService documentService;


    @Transactional
    public void getDocument(String id) {
        System.out.println(JSONObject.toJSONString(documentService.getById(id)));
    }

    @Transactional
    @DataSource("dev")
    public void update(Long id) {
        Document document = new Document();
        document.setName("测试11");
        document.setDetails("描述");
        documentService.save(document);
        System.out.println("~~~~~");
    }

    @DataSource("master")
    @Transactional
    public void save() {
        Document document = new Document();
        document.setName("测试"+RandomUtil.randomNumbers(10));
        document.setDetails("描述");
        documentService.save(document);
        System.out.println("~~~~~");

        //测试事物
        int i = 1 / 0;
    }
}
