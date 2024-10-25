package com.github.common.log.aspect;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.common.log.annotation.LogOperate;
import com.github.common.log.enums.LogType;
import com.github.common.log.enums.OperatorType;
import com.github.common.log.event.SysLogOperateEvent;
import com.github.common.log.holder.SpringContextHolder;
import com.github.common.log.pojo.LogOperateDo;
import com.github.common.log.util.SysLogUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @Author: wgs
 * @Date 2024/9/26 16:17
 * @Classname SysLogAspect
 * @Description 操作日志使用spring event异步入库
 */
@Aspect
@Slf4j
@AllArgsConstructor
public class SysLogAspect {
    @Around("@annotation(logOperate)")
    public Object around(ProceedingJoinPoint point, LogOperate logOperate) {
        return process(point, logOperate.value(), logOperate.operatorType(), logOperate.saveRequestData());
    }


    @SneakyThrows
    public Object process(ProceedingJoinPoint point, String title, OperatorType operatorType, Boolean saveRequestData) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);
        // 方法路径
        String methodName = point.getTarget().getClass().getName() + StrUtil.DOT + signature.getName() + "()";
        StringBuilder params = new StringBuilder("{");
        //参数值
        Object[] argValues = point.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature) point.getSignature()).getParameterNames();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        LogOperateDo logOperateVo = SysLogUtils.getSysLogOperate();
        logOperateVo.setTitle(title);
        logOperateVo.setMethod(methodName);
        logOperateVo.setParams(params + " }");
        logOperateVo.setOperatorType(operatorType.name());
        Long startTime = System.currentTimeMillis();
        Object obj;
        try {
            obj = point.proceed();
            logOperateVo.setLogType(LogType.INFO.name());
        } catch (Exception e) {
            logOperateVo.setException(ExceptionUtil.stacktraceToString(e));
            // 业务报错
//            if (e instanceof BizException) {
//                logOperateVo.setLogType(LogType.WARN.name());
//            } else {
            logOperateVo.setLogType(LogType.ERROR.name());
//            }
            throw e;
        } finally {
            saveLog(startTime, logOperateVo, saveRequestData);
        }
        return obj;
    }


    /**
     * @param startTime
     * @param logOperateVo
     * @param saveRequestData
     */
    public void saveLog(Long startTime, LogOperateDo logOperateVo, Boolean saveRequestData) {
        Long endTime = System.currentTimeMillis();
        logOperateVo.setTime(endTime - startTime);
        if (log.isTraceEnabled()) {
            log.trace("[logOperateVo]:{}", logOperateVo);
        }

        // 是否需要保存request，参数和值
        if (saveRequestData) {
            // 发送异步日志事件
            SpringContextHolder.publishEvent(new SysLogOperateEvent(logOperateVo));
        }
    }
}
