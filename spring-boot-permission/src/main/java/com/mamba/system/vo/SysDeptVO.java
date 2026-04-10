package com.mamba.system.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysDeptVO {

    private Integer id;

    private String name;

    private Integer parentId;

    private String level;

    private Integer seq;

    private String remark;

    /** 子部门列表（树形结构） */
    private List<SysDeptVO> children;

    private LocalDateTime operateTime;

    private String operator;
}
