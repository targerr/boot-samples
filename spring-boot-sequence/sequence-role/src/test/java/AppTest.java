import com.example.StartApplication;
import com.example.sequence.enums.SequenceKeyEnum;
import com.example.sequence.service.SequenceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StartApplication.class)
@Slf4j
public class AppTest {
    @Autowired
    private SequenceRecordService sequenceRecordService;

    @Test
    public void contextLoads() {

        for (int i = 0; i < 20; i++) {
            final String sequenceCode = sequenceRecordService.getSequenceCode(SequenceKeyEnum.HTDJ, new Date());
            System.out.println("单号: " + sequenceCode);
        }
    }

}
