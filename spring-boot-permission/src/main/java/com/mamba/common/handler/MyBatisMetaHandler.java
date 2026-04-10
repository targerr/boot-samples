package com.mamba.common.handler;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.mamba.common.util.IpUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 */
@Component
public class MyBatisMetaHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "operator", String.class, getOperator());
        this.strictInsertFill(metaObject, "operateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "operateIp", String.class, getOperateIp());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "operateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "operateIp", String.class, getOperateIp());
    }

    /**
     * 获取当前操作人
     */
    private String getOperator() {
        try {
            return StpUtil.getLoginIdAsString();
        } catch (Exception e) {
            return "system";
        }
    }

    /**
     * 获取当前操作IP
     */
    private String getOperateIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ip = IpUtil.getIpAddr(request);
                return StrUtil.blankToDefault(ip, "127.0.0.1");
            }
        } catch (Exception e) {
            // ignore
        }
        return "127.0.0.1";
    }

}
