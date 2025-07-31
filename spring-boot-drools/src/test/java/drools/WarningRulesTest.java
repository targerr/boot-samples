package drools;

import com.example.springbootdrools.SpringbootDroolsApplication;
import com.example.springbootdrools.drools.Calculation;
import com.example.springbootdrools.drools.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * @Author: wgs
 * @Date 2025/5/26 10:26
 * @Classname WarningRulesTest
 * @Description
 */
@SpringBootTest(classes = SpringbootDroolsApplication.class)
@Slf4j
public class WarningRulesTest {
    @Autowired
    private KieBase kieBase;
    @Test
    public void droolsTest() {
        Calculation calculation = new Calculation();
        calculation.setThreshold(new BigDecimal(5000));
        calculation.setWage(new BigDecimal(18000));
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        kieSession.insert(calculation);
        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();
    }

}
