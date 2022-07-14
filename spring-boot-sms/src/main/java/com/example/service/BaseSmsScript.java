package com.example.service;

import cn.hutool.core.util.ArrayUtil;
import com.example.annotation.SmsScriptHandler;
import com.example.holder.SmsScriptHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;

/**
 * @Author: wgs
 * @Date 2022/7/14 13:57
 * @Classname BaseSmsScript
 * @Description
 */
@Slf4j
public abstract class BaseSmsScript implements SmsScript {

    @Autowired
    private SmsScriptHolder smsScriptHolder;

    @PostConstruct
    public void registerProcessScript() {
        if (ArrayUtil.isEmpty(this.getClass().getAnnotations())) {
            log.error("BaseSmsScript can not find annotation!");
            return;
        }
        Annotation handlerAnnotations = null;
        for (Annotation annotation : this.getClass().getAnnotations()) {
            if (annotation instanceof SmsScriptHandler) {
                handlerAnnotations = annotation;
                break;
            }
        }
        if (handlerAnnotations == null) {
            log.error("handler annotations not declared");
            return;
        }
        //注册handler
        smsScriptHolder.putHandler(((SmsScriptHandler) handlerAnnotations).value(), this);
    }
}
