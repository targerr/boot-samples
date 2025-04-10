package com.example;

import com.example.request.OrginfoRequest;
import com.example.service.OrginfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrginfoServiceImplTest {

    @Autowired
    private OrginfoService orginfoService;

    @Test
    public void testOrginfoService() {
        // 创建 OrginfoEntity 实例并赋值所有字段
        OrginfoRequest request = new OrginfoRequest();
        request.setRowGuid("409b4141a9a848939481cbd0ab7ff5e2");
        request.setDanWeiGuid("409b4141a9a848939481cbd0ab7ff5e2");
        request.setDanWeiName("江西品汉环保科技有限公司");
        request.setDanWeiType("TBR");
        request.setUnitOrgNum("91361181MA399ARGXF");
        request.setAreaCode("330102");
        request.setAreaName("上城区");
        request.setRegistMoney(new BigDecimal("1000000.00"));
        request.setPlatformName("椒江区");
        request.setPlatformCode("331002");

        // 测试 save 操作
        assertTrue(orginfoService.save(request), "Save OrginfoEntity");

        request.setDanWeiName("Updated Org Name");
        assertTrue(orginfoService.update(request), "Update OrginfoEntity");
    }
}