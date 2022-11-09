package com.example.config;

public interface RabbitConsts {
    //中奖队列名称
    public final static String QUEUE_HIT = "prize_queue_hit";
    //参与活动队列
    public static final String QUEUE_PLAY = "prize_queue_play";
    //中奖路由名称
    public final static String EXCHANGE_DIRECT = "prize_exchange_direct";

    /**
     * 定义交换机
     */
     String EXCHANGE_SPRINGBOOT_NAME = "mt_ex";


    /**
     * 短信队列
     */
     String FANOUT_SMS_QUEUE = "fanout_sms_queue";
    /**
     * 邮件队列
     */
     String FANOUT_EMAIL_QUEUE = "fanout_email_queue";


    /**
     * 延迟队列
     */
    String DELAY_QUEUE = "delay.queue";

    /**
     * 延迟队列交换器
     */
    String DELAY_MODE_QUEUE = "delay.mode";
}
