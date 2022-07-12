package com.example.config;

public interface RabbitConsts {
    //中奖队列名称
    public final static String QUEUE_HIT = "prize_queue_hit";
    //参与活动队列
    public static final String QUEUE_PLAY = "prize_queue_play";
    //中奖路由名称
    public final static String EXCHANGE_DIRECT = "prize_exchange_direct";

    /**
     * 延迟队列
     */
    String DELAY_QUEUE = "delay.queue";

    /**
     * 延迟队列交换器
     */
    String DELAY_MODE_QUEUE = "delay.mode";
}
