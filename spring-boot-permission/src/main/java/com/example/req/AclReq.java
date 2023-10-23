package com.example.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: wgs
 * @Date 2023/10/20
 * @Classname AclReq
 * @since 1.0.0
 */
@Data
@Schema(name = "权限点模块")
public class AclReq {
    @Schema(name = "权限点id")
    private Integer id;

    @Schema(name = "权限点名称")
    @NotBlank(message = "权限点名称不可以为空")
    @Length(min = 2, max = 20, message = "权限点名称长度需要在2-20个字之间")
    private String name;

    @Schema(name = "权限点模块id")
    @NotNull(message = "必须指定权限模块")
    private Integer aclModuleId;

    @Schema(name = "权限点url")
    @Length(min = 6, max = 100, message = "权限点URL长度需要在6-100个字符之间")
    private String url;

    @Schema(name = "权限点类型")
    @NotNull(message = "必须指定权限点的类型")
    @Min(value = 1, message = "权限点类型不合法")
    @Max(value = 3, message = "权限点类型不合法")
    private Integer type;

    @Schema(name = "权限点状态")
    @NotNull(message = "必须指定权限点的状态")
    @Min(value = 0, message = "权限点状态不合法")
    @Max(value = 1, message = "权限点状态不合法")
    private Integer status;

    @Schema(name = "权限点顺序")
    @NotNull(message = "必须指定权限点的展示顺序")
    private Integer seq;

    @Schema(name = "权限点备注")
    @Length(min = 0, max = 200, message = "权限点备注长度需要在200个字符以内")
    private String remark;
}
