package com.mamba.system.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysUserVO {

    private Integer id;

    private String username;

    private String telephone;

    private String mail;

    private Integer deptId;

    /** 关联查询 - 部门名称 */
    private String deptName;

    private Integer status;

    /** 状态描述 */
    private String statusDesc;

    private String remark;

    /** 用户关联的角色ID列表 */
    private List<Integer> roleIds;

    /** 用户关联的角色列表 */
    private List<SysRoleVO> roleList;

    private LocalDateTime operateTime;

    private String operator;
}
