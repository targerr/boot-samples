package com.mamba.system.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysAclModuleVO {

    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private Integer status;

    private String remark;

    /** 模块下的权限点列表 */
    private List<SysAclVO> aclList;

    /** 子模块列表（树形结构） */
    private List<SysAclModuleVO> children;

    private LocalDateTime operateTime;

    private String operator;
}
