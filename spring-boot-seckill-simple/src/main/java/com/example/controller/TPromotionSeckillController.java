package com.example.controller;

import com.example.service.ITPromotionSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wgs
 * @since 2022-07-20
 */
@Controller
@RequestMapping("/tPromotionSeckill")
public class TPromotionSeckillController {

    @Autowired
    private ITPromotionSeckillService service;

}
