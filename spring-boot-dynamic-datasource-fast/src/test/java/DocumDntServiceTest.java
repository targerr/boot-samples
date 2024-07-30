import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SpringBootDynamicApplication;
import com.example.datasource.config.DynamicContextHolder;
import com.example.pojo.Document;
import com.example.service.DocumentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import service.DynamicDataSourceTestService;

/**
 * @Author: wgs
 * @Date 2024/4/23 15:25
 * @Classname DocumentServiceTest
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDynamicApplication.class)


public class DocumDntServiceTest {
    @Autowired
    private DocumentService documentService;

    @Test
    public void save() {
        Document document = new Document();
        document.setName("测试"+ RandomUtil.randomNumbers(10));
        document.setDetails("描述");
        documentService.save(document);
        System.out.println("~~~~~");
    }

    @Test
    public void findOne() {
        DynamicContextHolder.push("dev");
        String id = "1770288973374308353";
        System.out.println(JSONObject.toJSONString(documentService.getById(id)));
    }
}
