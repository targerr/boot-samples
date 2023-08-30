import com.example.SpringBootSmsYmlApplication;
import com.example.config.AppProperties;
import com.example.service.SmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wgs
 * @Date 2022/7/14 09:58
 * @Classname SmsTest
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= SpringBootSmsYmlApplication.class)
public class SmsTest {
    @Autowired
    private SmsService smsService;
@Autowired
private AppProperties appProperties;

    @Test
    public void send() {
        System.out.println(appProperties);
        smsService.send("18806513872", "1212");
    }

}
