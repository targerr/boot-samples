package com.github.echo.interceptor;


import com.github.echo.core.EchoServiceImpl;
import com.github.echo.properties.EchoProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.context.ApplicationContext;

import java.sql.Statement;


/**
 * @Author: wgs
 * @Date 2024/9/5 15:30
 * @Classname EchoDataInterceptor
 * @Description
 */
@Intercepts({@Signature(
        type = ResultSetHandler.class,
        method = "handleResultSets",
        args = {Statement.class}
)})
@Slf4j
public class EchoDataInterceptor implements Interceptor {

    private final ApplicationContext applicationContext;
    private final EchoProperties echoProperties;

    public EchoDataInterceptor(ApplicationContext applicationContext, EchoProperties echoProperties) {
        this.applicationContext = applicationContext;
        this.echoProperties = echoProperties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object proceed = invocation.proceed();
        EchoServiceImpl echoServiceImpl = applicationContext.getBean(EchoServiceImpl.class);
        if (echoProperties.getEnabled()) {
            log.debug("查询数据回显注入开始======>");
            echoServiceImpl.action(proceed, echoProperties.getGuavaCache().getEnabled(), new String[0]);
            log.debug("查询数据回显注入结束======>");
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

}
