package com.example;

import com.example.share.entity.Share;
import com.example.share.service.IShareService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * @Author: wgs
 * @Date 2022/8/19 15:16
 * @Classname ShareTest
 * @Description
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ShareTest {
    @Autowired
    private IShareService service;

    @Test
    public void  save(){
        Share share = new Share();
        share.setAuthor("钱文忠");
        share.setBuyCount(2);
        share.setDownloadUrl("www.baidu.com");
        share.setAuditStatus("审核通过");
        share.setPrice(new BigDecimal("20"));
        share.setTitle("钢铁怎么炼成的");
        service.save(share);
        System.out.println("----");
    }
}
