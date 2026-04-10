package com.mamba.system.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysAclModuleDTO {

    private Integer id;

    @NotBlank(message = "模块名称不能为空")
    private String name;

    /**
     * 默认0表示顶级模块
     */
    private Integer parentId;

    private Integer seq;

    private Integer status;

    private String remark;
}
