package com.mamba.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysAclDTO {

    private Integer id;

    @NotBlank(message = "权限码不能为空")
    private String code;

    @NotBlank(message = "权限名称不能为空")
    private String name;

    @NotNull(message = "权限模块不能为空")
    private Integer aclModuleId;

    private String url;

    @NotNull(message = "权限类型不能为空")
    private Integer type;

    private Integer seq;

    private String remark;
}
