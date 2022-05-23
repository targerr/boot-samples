package com.example.enums;

import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/5/22 14:38
 * @Classname CommonEnum
 * @Description
 */
public interface CommonEnum {
    @Getter
    enum ResultEnum implements Ienum<Integer> {

        /**
         * 成功
         */
        SUCCESS(0, "成功"),
        /**
         * 参数不正确
         */
        PARAM_ERROR(1, "参数不正确"),

        RATE_ERROR(10, "限流中"),
        ;
        private Integer code;
        private String message;

        ResultEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
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
        boolean is(T t);

        T get();
    }
}
