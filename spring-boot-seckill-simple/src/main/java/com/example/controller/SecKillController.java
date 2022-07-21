package com.example.controller;

import cn.hutool.core.lang.Dict;
import com.example.service.PromotionSecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wgs
 * @since 2022-07-20
 */
@RestController
@RequestMapping("/")
public class SecKillController {

    @Autowired
    private PromotionSecKillService service;

    @PostMapping("/seckill")
    public Dict processSecKill(Long psId, String userId) {
        String orderNo = service.processSecKill(psId, userId, 1);
        service.sendOrderToQueue(userId, orderNo);


        return Dict.create().set("code", 0).set("message", " 恭喜您，抢到商品啦");
    }
}
