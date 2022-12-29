package com.example.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSONObject;
import com.example.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wgs
 * @Date 2022/11/30 14:30
 * @Classname SentinelHandler
 * @Description
 */
@Component
@Slf4j
public class SentinelHandler implements BlockExceptionHandler {
    /**
     * 流控与异常信息处理器
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws Exception
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {

        String msg = null;

        if (e instanceof FlowException) {//限流异常
            msg = "接口已被限流";
        } else if (e instanceof DegradeException) {//熔断异常
            msg = "接口已被熔断,请稍后再试";
        } else if (e instanceof ParamFlowException) { //热点参数限流
            msg = "热点参数限流"; //例如:id参数=5不开启限流,id=10开启限流,针对不同的参数进行不同的限流策略
        } else if (e instanceof SystemBlockException) { //系统规则异常
            msg = "系统规则(负载/....不满足要求)";//例如:ＣＰＵ负载超过８０％则不允许访问
        } else if (e instanceof AuthorityException) { //授权规则异常
            msg = "授权规则不通过"; //例如:服务A不允许服务B进行访问,服务B当发起调用后就会触发授权异常
        }

        CommonResponse<String> response = new CommonResponse<String>(429, msg, "");

        httpServletResponse.setStatus(429);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");

        httpServletResponse.getWriter().write(JSONObject.toJSONString(response));
    }

}
