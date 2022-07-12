package com.example.listener;


import com.example.config.RabbitConsts;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = RabbitConsts.QUEUE_PLAY)
@Slf4j
public class PrizeHitReceiver {
    private final static Logger logger = LoggerFactory.getLogger(PrizeHitReceiver.class);

    @RabbitHandler
    public void processMessage2(byte[] message) {
        logger.info("Receiver1 ===========> : " + new String(message));
    }

    @RabbitHandler
    public void processMessage3(Map message) {
        logger.info("user hit : " + message);


    }

//    @RabbitHandler
//    public void processMessage2(CardUserHit message) {
//
//    }
}