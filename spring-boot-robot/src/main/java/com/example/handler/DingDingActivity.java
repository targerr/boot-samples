package com.example.handler;

import com.example.domin.ParamInfo;
import com.example.handler.impl.DingDingRobotHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/8/3 09:32
 * @Classname DingDingActiveity
 * @Description
 */
@Slf4j
@Service
public class DingDingActivity {

    public static final Map<DingDingHandler.DingDingEnum, DingDingHandler> MAP = new HashMap<>(4);

    public DingDingActivity(List<DingDingHandler> handlerList) {
        handlerList.forEach(e -> MAP.put(e.getEnum(), e));
    }

    public DingDingHandler getHeader(DingDingHandler.DingDingEnum dingDingEnum) {
        return MAP.get(dingDingEnum);
    }

    public boolean doHandler(DingDingHandler.DingDingEnum dingDingEnum, ParamInfo paramInfo) {
        return getHeader(dingDingEnum).doHandler(paramInfo);
    }
}
