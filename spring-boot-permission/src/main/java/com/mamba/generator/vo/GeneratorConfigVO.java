package com.mamba.generator.vo;

import lombok.Data;

/**
 * 代码生成器配置VO
 */
@Data
public class GeneratorConfigVO {
    /** 作者 */
    private String author = "mamba";
    /** 包名 */
    private String packageName = "com.mamba.system";
    /** 模块名 */
    private String moduleName = "system";
    /** 表前缀 */
    private String tablePrefix = "sys_";
    /** 表名 */
    private String tableName;
    /** 是否去除表前缀 */
    private Boolean removePrefix = true;
}
