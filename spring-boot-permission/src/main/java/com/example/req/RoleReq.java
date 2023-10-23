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
 * @Classname RoleReq
 * @since 1.0.0
 */
@Data
@Schema(name = "角色model",description = "角色实体")
public class RoleReq {
    @Schema(description = "角色id")
    private Integer id;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不可以为空")
    @Length(min = 2, max = 20, message = "角色名称长度需要在2-20个字之间")
    private String name;

    @Schema(description = "角色类型")
    @Min(value = 1, message = "角色类型不合法")
    @Max(value = 2, message = "角色类型不合法")
    private Integer type = 1;

    @Schema(description = "角色状态")
    @NotNull(message = "角色状态不可以为空")
    @Min(value = 0, message = "角色状态不合法")
    @Max(value = 1, message = "角色状态不合法")
    private Integer status;

    @Schema(description = "角色备注")
    @Length(min = 0, max = 200, message = "角色备注长度需要在200个字符以内")
    private String remark;
}
