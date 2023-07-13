package com.example.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/11/30 11:46
 * @Classname CommonResponse
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse<T> {
    private Integer code;
    private String msg;
    private T data;

    public static void main(String[] args) {
        St
    }

}