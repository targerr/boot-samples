package com.example.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: wgs
 * @Date 2023/10/17 15:28
 * @Classname UserInfoDTO
 * @Description
 */
@Data
@Schema(description = "用户基础实体对象")
@Accessors(chain = true)
public class UserInfoDTO extends  BaseUserInfoDTO{



    @Schema(description = "token")
    private String token;
}
