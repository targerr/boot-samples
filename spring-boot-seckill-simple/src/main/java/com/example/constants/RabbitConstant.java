package com.example.constants;

/**
 * @Author: wgs
 * @Date 2022/7/20 15:57
 * @Classname RabbitConstant
 * @Description
 */
public interface RabbitConstant {
    public final static String QUEUE_ORDER = "queue_order";
    //中奖队列名称
    public final static String QUEUE_HIT = "prize_queue_hit";
    //参与活动队列
    public static final String QUEUE_PLAY = "prize_queue_play";
    //中奖路由名称
    public final static String EXCHANGE_DIRECT = "prize_exchange_direct";

}
