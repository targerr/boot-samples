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
 * @Classname AclModuleReq
 * @since 1.0.0
 */
@Data
@Schema(name = "权限模块")
public class AclModuleReq {
    @Schema(description = "权限模块id")
    private Integer id;

    @Schema(description = "权限模块名称")
    @NotBlank(message = "权限模块名称不可以为空")
    @Length(min = 2, max = 20, message = "权限模块名称长度需要在2~20个字之间")
    private String name;

    @Schema(description = "权限模块父id")
    private Integer parentId = 0;

    @NotNull(message = "权限模块展示顺序不可以为空")
    private Integer seq;

    @Schema(description = "权限模块状态")
    @NotNull(message = "权限模块状态不可以为空")
    @Min(value = 0, message = "权限模块状态不合法")
    @Max(value = 1, message = "权限模块状态不合法")
    private Integer status;

    @Schema(description = "权限模块备注")
    @Length(max = 200, message = "权限模块备注需要在200个字之间")
    private String remark;
}
