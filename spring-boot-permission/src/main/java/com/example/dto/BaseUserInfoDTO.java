package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: wgs
 * @Date 2023/10/24
 * @Classname BaseUserInfoDTO
 * @since 1.0.0
 */
@Data
@Schema(description = "用户基础实体对象")
@Accessors(chain = true)
public class BaseUserInfoDTO {
    /**
     * 用户id
     */
    @Schema(description = "用户id", required = true)
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名", required = true)
    private String userName;


    /**
     * 手机号
     */
    @Schema(description = "手机号", required = true)
    private String telephone;


    /**
     * 用户图像
     */
    @Schema(description = "用户头像")
    private String photo;
    /**
     * 个人简介
     */
    @Schema(description = "用户简介")
    private String profile;

    /**
     * 扩展字段
     */
    @Schema(hidden = true)
    private String extend;

    /**
     * 是否删除
     */
    @Schema(hidden = true, description = "用户是否被删除")
    private Integer deleted;

    /**
     * 用户最后登录区域
     */
    @Schema(description = "用户最后登录的地理位置", example = "湖北·武汉")
    private String region;

}
