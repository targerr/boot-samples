package com.example.datasource.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.datasource.constant.DataSourceConstant;
import lombok.extern.slf4j.Slf4j;


/**
 * @author: ranyang
 * @Date: 2021/07/30 09:09
 * @descript: 动态数据源管理
 */
@Slf4j
public class DynamicSourceTtl {

    public static TransmittableThreadLocal<String> dataSourceContext = new TransmittableThreadLocal<>();

    public static String push(String dataSource) {
        String ds = StringUtils.isBlank(dataSource) ? DataSourceConstant.MASTER_DATASOURCE : dataSource;
        dataSourceContext.set(ds);
        DynamicDataSourceContextHolder.push(ds);
        return ds;
    }

    public static void clear() {
        dataSourceContext.remove();
        DynamicDataSourceContextHolder.clear();
    }

    public static String get() {
        //判断如果ttl有参数，未找到参数。重新赋值
        if (StringUtils.isNotBlank(dataSourceContext.get()) && StringUtils.isBlank(DynamicDataSourceContextHolder.peek())) {
            DynamicDataSourceContextHolder.push(dataSourceContext.get());
        }
        //判断ttl有参数，并有参数，但互相参数不一致，以ttl为准重新赋值
        if (StringUtils.isNotBlank(dataSourceContext.get()) && StringUtils.isNotBlank(DynamicDataSourceContextHolder.peek())) {
            if (!dataSourceContext.get().equals(DynamicDataSourceContextHolder.peek())) {
                DynamicDataSourceContextHolder.push(dataSourceContext.get());
            }
        }
        return dataSourceContext.get();
    }

}
