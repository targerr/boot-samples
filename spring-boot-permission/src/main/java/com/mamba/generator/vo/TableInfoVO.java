package com.mamba.generator.vo;

import lombok.Data;

/**
 * 数据库表信息VO
 */
@Data
public class TableInfoVO {
    private String tableName;
    private String tableComment;
    private String engine;
    private String createTime;
}
