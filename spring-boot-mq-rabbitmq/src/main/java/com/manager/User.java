package com.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: wgs
 * @Date 2022/11/2 11:08
 * @Classname User
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    private String id;

    private String name;

    private Integer age;
}
