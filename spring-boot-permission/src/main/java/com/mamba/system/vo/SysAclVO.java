package com.mamba.system.vo;

import lombok.Data;

@Data
public class SysAclVO {

    private Integer id;

    private String code;

    private String name;

    private Integer aclModuleId;

    /** 关联查询 - 模块名称 */
    private String aclModuleName;

    private String url;

    /** 1:菜单 2:按钮 3:其他 */
    private Integer type;

    private String typeDesc;

    private Integer status;

    private String statusDesc;

    private Integer seq;

    private String remark;

    /** 用于角色分配权限时标记是否已选中 */
    private Boolean checked;
}
