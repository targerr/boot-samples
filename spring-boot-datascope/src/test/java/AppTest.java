import cn.hutool.json.JSONUtil;
import com.example.enterprise.SpringBootDataScopeApplication;
import com.example.enterprise.dao.mapper.BlogMapper;
import com.example.enterprise.entity.Blog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootDataScopeApplication.class)
@Slf4j
public class AppTest {
    @Autowired
    private BlogMapper blogMapper;

    @Test
    public void selectAllCustomer() {
        final List<Blog> list = blogMapper.selectList(null);
        System.out.println(JSONUtil.toJsonStr(list));
        System.out.println("------");
    }
}

