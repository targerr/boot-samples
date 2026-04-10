package com.mamba.system.dto;

import lombok.Data;

@Data
public class PageQueryDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    /** 搜索关键词 */
    private String keyword;

    /** 状态筛选 */
    private Integer status;

    /** 类型筛选 */
    private Integer type;
}
