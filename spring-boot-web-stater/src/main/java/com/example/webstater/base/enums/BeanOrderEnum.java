package com.example.webstater.base.enums;

/**
 * description: The Order Of Bean

 */
public enum BeanOrderEnum {

    REGISTRY_FEIGN_FILTER(50),
    DATASOURCE_EXCEPTION_HANDLER_ORDER(Integer.MIN_VALUE),
    CACHE_EXCEPTION_ORDER(Integer.MIN_VALUE + 1),
    IDEMPOTENT_EXCEPTION_ORDER(Integer.MIN_VALUE + 2),
    SENTINEL_EXCEPTION_ORDER(Integer.MIN_VALUE + 3),
    ZOOKEEPER_EXCEPTION_ORDER(Integer.MIN_VALUE + 4),
    FEIGN_EXCEPTION_ORDER(Integer.MIN_VALUE + 5),
    ALARM_EXCEPTION_ORDER(Integer.MIN_VALUE + 6),
    APPLICATION_LOG_ORDER(Integer.MIN_VALUE + 7),
    BASE_EXCEPTION_ORDER(Integer.MIN_VALUE + 8),
    ;

    private final int order;

    BeanOrderEnum(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

}
