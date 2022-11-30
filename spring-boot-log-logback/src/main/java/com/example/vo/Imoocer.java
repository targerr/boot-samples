package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/11/25 21:25
 * @Classname Imoocer
 * @Description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Imoocer {

    private String name;
    private Integer age;
    private Double salary;

    public Imoocer(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}


