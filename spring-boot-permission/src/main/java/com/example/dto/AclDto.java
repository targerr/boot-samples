package com.example.dto;

import com.example.entity.SysAcl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AclDto  {

    @Schema(name = "是否要默认选中")
    private boolean checked = false;

    @Schema(name = "是否有权限操作")
    private boolean hasAcl = false;

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
    public static AclDto adapt(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }
}
