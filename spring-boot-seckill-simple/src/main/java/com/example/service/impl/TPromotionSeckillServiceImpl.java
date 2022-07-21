package com.example.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.RabbitConstant;
import com.example.constants.SecKillConstant;
import com.example.entity.TPromotionSeckill;
import com.example.exception.SecKillException;
import com.example.mapper.TPromotionSeckillMapper;
import com.example.service.PromotionSecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wgs
 * @since 2022-07-20
 */
@Service
@Slf4j
public class TPromotionSeckillServiceImpl extends ServiceImpl<TPromotionSeckillMapper, TPromotionSeckill> implements PromotionSecKillService {

    @Autowired
    private TPromotionSeckillMapper seckillMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;


    @Override
    public String processSecKill(Long psId, String userId, Integer number) {
        final TPromotionSeckill ps = seckillMapper.selectById((Serializable) psId);

        checkStatus(psId, ps);

        Integer goodsId = (Integer) redisTemplate.opsForList().leftPop(SecKillConstant.SECKILL_COUNT + ps.getPsId());
        if (ObjectUtil.isNull(goodsId)) {
            throw new SecKillException("抱歉，该商品已被抢光，下次再来吧！！");
        }

        // 判断是否抢购
        boolean isExisted = redisTemplate.opsForSet().isMember(SecKillConstant.USER_COUNT + ps.getPsId(), userId);
        if (isExisted) {
            // 退回商品
            redisTemplate.opsForList().rightPush(SecKillConstant.SECKILL_COUNT + ps.getPsId(), ps.getGoodsId());
            log.error("【秒杀活动】 您已经参加过此活动,id: {}; userId:{}", psId, userId);
            throw new SecKillException("抱歉，您已经参加过此活动，请勿重复抢购！");
        }

        String orderNo = IdUtil.getSnowflakeNextIdStr();
        log.info("【秒杀活动】 恭喜您，抢到商品啦。快去下单吧,id: {}; userId:{}; orderNo:{}", psId, userId, orderNo);
        redisTemplate.opsForSet().add(SecKillConstant.USER_COUNT + ps.getPsId(), userId);


        return orderNo;
    }

    public String sendOrderToQueue(String userId, String orderNo) {
        log.info("【秒杀活动】 准备向队列发送信息, userId:{}; orderNo:{}", userId, orderNo);
        System.out.println("准备向队列发送信息");
        //订单基本信息
        Map<String, String> data = new HashMap<String, String>();
        data.put("userid", userId);
        data.put("orderNo", orderNo);
        //附加额外的订单信息
        rabbitTemplate.convertAndSend(RabbitConstant.QUEUE_ORDER, data);
        return orderNo;
    }

    private void checkStatus(Long psId, TPromotionSeckill seckill) {
        if (ObjectUtil.isNull(seckill)) {
            log.error("【秒杀活动】 不存在,id: {}", psId);
            throw new SecKillException("活动不存在!");
        }
        if (seckill.getStatus() == 0) {
            log.error("【秒杀活动】 未开始,id: {}", psId);
            throw new SecKillException("秒杀活动未开始!");
        }
        if (seckill.getStatus() == 2) {
            log.error("【秒杀活动】 已结束,id: {}", psId);
            throw new SecKillException("秒杀活动已结束!");
        }
    }
}
