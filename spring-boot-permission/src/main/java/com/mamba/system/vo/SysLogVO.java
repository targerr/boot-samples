package com.mamba.system.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysLogVO {

    private Integer id;

    private Integer type;

    /** 类型描述：1部门 2用户 3权限模块 4权限 5角色 6角色用户关系 7角色权限关系 */
    private String typeDesc;

    private Integer targetId;

    private String oldValue;

    private String newValue;

    private String operator;

    private LocalDateTime operateTime;

    private String operateIp;

    /** 0:未复原 1:已复原 */
    private Integer status;

    private String statusDesc;
}
