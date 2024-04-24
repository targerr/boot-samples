import cn.hutool.core.util.RandomUtil;

import com.alibaba.fastjson.JSONObject;
import com.example.SpringBootDynamicApplication;
import com.example.dal.DsSelectExecutor;
import com.example.dal.MasterSlaveDsEnum;
import com.example.pojo.Document;
import com.example.service.DocumentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wgs
 * @Date 2024/4/23 15:25
 * @Classname DocumentServiceTest
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootDynamicApplication.class)
public class DocumentServiceTest {
    @Autowired
    private DocumentService documentService;
    @Test
    public void save(){
        Document document = new Document();;
        document.setId(RandomUtil.randomNumbers(16));
        document.setName("测试11");
        document.setDetails("描述");
        documentService.save(document);
        System.out.println("~~~~~");

    }

    @Test
    public void findOne(){
        final Document submit = DsSelectExecutor.submit(MasterSlaveDsEnum.SLAVE, () -> documentService.getById("1770288973374308353"));
//        final Document byId = documentService.getById("9814299265276352");
        System.out.println(JSONObject.toJSONString(submit,true));
    }
}
