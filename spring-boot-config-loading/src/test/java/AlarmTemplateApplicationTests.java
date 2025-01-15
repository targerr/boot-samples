import com.alibaba.fastjson.JSONObject;
import com.example.AlarmTemplateApplication;
import com.example.entity.AlarmTemplate;
import com.example.provider.AlarmTemplateProvider;
import com.example.provider.FileAlarmTemplateProvider;
import com.example.provider.JdbcAlarmTemplateProvider;
import com.example.provider.YamlAlarmTemplateProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes= AlarmTemplateApplication.class)
class AlarmTemplateApplicationTests {
    @Resource
    private FileAlarmTemplateProvider fileAlarmTemplateProvider;
    @Resource
    private YamlAlarmTemplateProvider yamlAlarmTemplateProvider;
    @Resource
    private JdbcAlarmTemplateProvider jdbcAlarmTemplateProvider;
    @Resource
    private AlarmTemplateProvider alarmTemplateProvider;
    @Test
    public void contextLoads() {
         AlarmTemplate alarmTemplate = fileAlarmTemplateProvider.getAlarmTemplate("send-message-notice");

        System.out.println(JSONObject.toJSONString(alarmTemplate, true));
    }

    @Test
    public void contextYmlLoads() {
        AlarmTemplate alarmTemplate = yamlAlarmTemplateProvider.getAlarmTemplate("send-message-notice");

        System.out.println(JSONObject.toJSONString(alarmTemplate, true));
    }

    @Test
    public void contextJdbcLoads() {
        AlarmTemplate alarmTemplate = jdbcAlarmTemplateProvider.getAlarmTemplate("send-message-notice");

        System.out.println(JSONObject.toJSONString(alarmTemplate, true));
    }

    @Test
    public void contextTemplateLoads() {
        AlarmTemplate alarmTemplate = alarmTemplateProvider.loadingAlarmTemplate("send-message-notice");

        System.out.println(JSONObject.toJSONString(alarmTemplate, true));
    }


}
