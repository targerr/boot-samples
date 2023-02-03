package com.example.chain.event;

import com.alibaba.fastjson.JSONObject;
import com.example.chain.domain.TaskInfo;
import com.example.enums.SmsEnum;
import com.example.holder.SmsScriptHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Author: wgs
 * @Date 2022/7/15 10:42
 * @Classname BusinessListener
 * @Description
 */
@Component
@Slf4j
public class BusinessListener  {
    @Autowired
    private SmsScriptHolder smsScriptHolder;

    @EventListener({BusinessEvent.class})
    public void processEvent(BusinessEvent event) {
        log.error("【事件】获取消息,data: {}", JSONObject.toJSONString(event.getSource(), true));

        smsScriptHolder.route("TencentSmsScript")
                .send("18806513872", "1212", SmsEnum.用户登录);
    }
}
