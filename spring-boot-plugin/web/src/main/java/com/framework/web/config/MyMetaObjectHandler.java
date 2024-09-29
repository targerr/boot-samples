package com.framework.web.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: wgs
 * @Date 2022/4/24 10:45
 * @Classname MybatisPlusConfig
 * @Description
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //第一个对应实体属性名, 第二个参数需要填充的值
        setFieldValByName("createDateTime", LocalDateTime.now(), metaObject);
        //第一个对应实体属性名, 第二个参数需要填充的值
        setFieldValByName("modifyDateTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //第一个对应实体属性名, 第二个参数需要填充的值
        setFieldValByName("modifyDateTime", LocalDateTime.now(), metaObject);
    }
}
