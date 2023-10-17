package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: wgs
 * @Date 2023/10/17 15:28
 * @Classname UserInfoDTO
 * @Description
 */
@Data
@ApiModel("用户基础实体对象")
@Accessors(chain = true)
public class UserInfoDTO {
    @ApiModelProperty(value = "业务主键")
    private Long id;
    @ApiModelProperty(value = "token")
    private String token;
}
