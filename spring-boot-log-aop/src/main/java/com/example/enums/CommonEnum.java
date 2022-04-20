package com.example.enums;

import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/4/20 10:00
 * @Classname CommonEnum
 * @Description
 */
public interface CommonEnum {

    @Getter
    enum LoggerTypeEnum implements Ienum<Integer> {

        /**
         * 查询
         */
        SELECT(1, "查询"),
        ;
        private final Integer code;
        private final String logDesc;

        LoggerTypeEnum(int code, String logDesc) {
            this.code = code;
            this.logDesc = logDesc;
        }

        @Override
        public boolean is(Integer code) {
            return get().equals(code);
        }

        @Override
        public Integer get() {
            return code;
        }
    }

    interface Ienum<T> {
        /**
         * 校验枚举值
         *
         * @param t
         * @return
         */
        boolean is(T t);

        /**
         * 获取枚举值
         *
         * @return
         */
        T get();
    }
}
