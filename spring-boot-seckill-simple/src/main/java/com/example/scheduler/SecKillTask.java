package com.example.scheduler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.constants.SecKillConstant;
import com.example.entity.TPromotionSeckill;
import com.example.service.PromotionSecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
@Slf4j
public class SecKillTask {
    @Autowired
    private PromotionSecKillService seckillService;
    @Resource
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void startSecKill() {
        log.info("【秒杀活动】启动任务, date: {}", DateUtil.now());

        // 获取未开始的活动
        List<TPromotionSeckill> unstartSeckills = getUnstartSeckills();

        for (TPromotionSeckill ps : unstartSeckills) {
            log.info("【秒杀活动】启动, id:{}", ps.getPsId());
            //删掉以前重复的活动任务缓存
            redisTemplate.delete(SecKillConstant.SECKILL_COUNT + ps.getPsId());
            //有几个库存商品，则初始化几个list对象
            for (int i = 0; i < ps.getPsCount(); i++) {
                redisTemplate.opsForList().rightPush(SecKillConstant.SECKILL_COUNT + ps.getPsId(), ps.getGoodsId());
            }
            ps.setStatus(1);
            seckillService.updateById(ps);
        }

    }


    @Scheduled(cron = "0/15 * * * * ?")
    public void endSecKill() {
        log.info("【秒杀活动】已结束任务, date:{}", DateUtil.now());
        List<TPromotionSeckill> expireTimeList = getExpireSecKills();

        for (TPromotionSeckill ps : expireTimeList) {
            log.info("【秒杀活动】已结束 id:{}", ps.getPsId());
            ps.setStatus(2);
            seckillService.updateById(ps);
            redisTemplate.delete(SecKillConstant.SECKILL_COUNT + ps.getPsId());
        }
    }


    private List<TPromotionSeckill> getExpireSecKills() {
        LambdaQueryWrapper<TPromotionSeckill> lambda = new LambdaQueryWrapper<TPromotionSeckill>();
        lambda.le(TPromotionSeckill::getEndTime, DateUtil.now());
        lambda.eq(TPromotionSeckill::getStatus, 1);
        List<TPromotionSeckill> unstartSeckills = seckillService.list(lambda);
        return unstartSeckills;
    }

    private List<TPromotionSeckill> getUnstartSeckills() {
        LambdaQueryWrapper<TPromotionSeckill> lambda = new LambdaQueryWrapper<TPromotionSeckill>();
        lambda.le(TPromotionSeckill::getStartTime, DateUtil.now());
        lambda.ge(TPromotionSeckill::getEndTime, DateUtil.now());
        lambda.eq(TPromotionSeckill::getStatus, 0);
        List<TPromotionSeckill> unstartSeckills = seckillService.list(lambda);
        return unstartSeckills;
    }

}
