package com.mamba.system.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysRoleVO {

    private Integer id;

    private String name;

    /** 1:管理员角色 2:其他 */
    private Integer type;

    private String typeDesc;

    private Integer status;

    private String statusDesc;

    private String remark;

    /** 角色关联的权限ID列表 */
    private List<Integer> aclIdList;

    /** 角色关联的用户ID列表 */
    private List<Integer> userIdList;
}
