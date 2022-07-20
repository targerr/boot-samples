package com.example.service;

import com.example.entity.TPromotionSeckill;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wgs
 * @since 2022-07-20
 */
public interface ITPromotionSeckillService extends IService<TPromotionSeckill> {

    List<TPromotionSeckill> findUnstartSeckill();
}
