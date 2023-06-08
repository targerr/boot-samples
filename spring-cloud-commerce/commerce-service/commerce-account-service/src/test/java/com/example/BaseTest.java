package com.example;

import com.example.filter.AccessContext;
import com.example.vo.LoginUserInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: wgs
 * @Date 2022/11/16 15:41
 * @Classname BaseTest
 * @Description 测试类基类 填充用户信息
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseTest {

    protected LoginUserInfo loginUserInfo = new LoginUserInfo(10L, "tom");


    public void init() {
        AccessContext.setUserInfoThreadLocal(loginUserInfo);
    }

    @Before
    public void before() {
        init();
    }

    @After
    public void destroy() {
        AccessContext.clearLoginUserInfo();
    }
}
