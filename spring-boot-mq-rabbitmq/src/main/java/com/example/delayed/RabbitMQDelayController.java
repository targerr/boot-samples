package com.example.delayed;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2024/6/11 15:27
 * @Classname RabbitMQDelayMessageController
 * @Description
 * 参考： https://zhuanlan.zhihu.com/p/641458427*
 */

@Slf4j
@RestController
@RequestMapping("/rabbitmq_order_delay_message")
public class RabbitMQDelayController {

    @Autowired
    private MessageSender sender;
    /**
     * 发送消息
     * @return
     */
    @RequestMapping(value = "/sendMsg", method = RequestMethod.GET)
    @ResponseBody
    public void sendMsg() {
        OrderMessage orderMessage = OrderMessage.builder().orderId(RandomUtil.randomNumbers(20)).tradeNo(RandomUtil.randomNumbers(10)).build();
        sender.sendOrderMessage(orderMessage);
    }
}
