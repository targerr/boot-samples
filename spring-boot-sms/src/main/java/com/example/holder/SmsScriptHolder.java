package com.example.holder;

import com.example.service.SmsScript;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/7/14 13:59
 * @Classname SmsScriptHolder
 * @Description SmsScript的映射关系
 */
@Component
public class SmsScriptHolder {
    private Map<String, SmsScript> handlers = new HashMap<>(8);

    public void putHandler(String scriptName, SmsScript handler) {
        handlers.put(scriptName, handler);
    }
    public SmsScript route(String scriptName) {
        return handlers.get(scriptName);
    }
}
