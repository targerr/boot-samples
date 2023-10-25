package com.example.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
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
@Schema(name = "权限点模块", description = "权限点实体")
public class AclReq {
    @Schema(description = "权限点id")
    private Integer id;

    @Schema(description = "权限点模块id")
    @NotNull(message = "必须指定权限模块")
    private Integer aclModuleId;

    @Schema(description = "权限点URL",defaultValue = "sys/role", required = false)
    @Length(min = 6, max = 100, message = "权限点URL长度需要在6-100个字符之间")
    private String url;


    @Schema(description = "顺序")
    @NotNull(message = "必须指定点的展示顺序")
    private Integer seq;


    @Schema(description = "名称")
    @NotBlank(message = "名称不可以为空")
    @Length(min = 2, max = 20, message = "名称长度需要在2-20个字之间")
    private String name;

    @Schema(description = "类型")
    @Min(value = 1, message = "类型不合法")
    @Max(value = 2, message = "类型不合法")
    private Integer type = 1;

    @Schema(description = "状态")
    @NotNull(message = "状态不可以为空")
    @Min(value = 0, message = "状态不合法")
    @Max(value = 1, message = "状态不合法")
    private Integer status;

    @Schema(description = "备注")
    @Length(min = 0, max = 200, message = "备注长度需要在200个字符以内")
    private String remark;
}


