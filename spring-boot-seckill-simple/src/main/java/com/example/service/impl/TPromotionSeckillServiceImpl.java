package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.service.ITPromotionSeckillService;
import com.example.entity.TPromotionSeckill;
import com.example.mapper.TPromotionSeckillMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wgs
 * @since 2022-07-20
 */
@Service
public class TPromotionSeckillServiceImpl extends ServiceImpl<TPromotionSeckillMapper, TPromotionSeckill> implements ITPromotionSeckillService {

    @Autowired
    private ServiceImpl service;

    @Override
    public List<TPromotionSeckill> findUnstartSeckill() {
        LambdaQueryWrapper<TPromotionSeckill> lambda = new LambdaQueryWrapper<TPromotionSeckill>();
        lambda.ge(TPromotionSeckill::getStartTime, new Date());
        lambda.le(TPromotionSeckill::getEndTime, new Date());
        lambda.eq(TPromotionSeckill::getStatus, 0);
        return service.list(lambda);

    }
}
