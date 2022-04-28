package com.example.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: wgs
 * @Date 2022/4/28 14:23
 * @Classname User
 * @Description
 */
@Data
@ApiModel(description = "用户实体类")
public class User {
    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "地址")
    private String address;
}
