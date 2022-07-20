package com.example.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.TPromotionSeckill;
import com.example.service.ITPromotionSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Component
public class SecKillTask {
    @Autowired
    private ITPromotionSeckillService seckillService;
    @Resource
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void startSecKill() {
        LambdaQueryWrapper<TPromotionSeckill> lambda = new LambdaQueryWrapper<TPromotionSeckill>();
        lambda.ge(TPromotionSeckill::getStartTime, new Date());
        lambda.le(TPromotionSeckill::getEndTime, new Date());
        lambda.eq(TPromotionSeckill::getStatus, 0);
        List<TPromotionSeckill> findUnstartSeckill = seckillService.list(lambda);


    }

}
