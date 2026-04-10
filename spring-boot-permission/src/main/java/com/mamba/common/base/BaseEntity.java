package com.mamba.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类，包含公共审计字段
 */
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT)
    private String operator;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime operateTime;

    /**
     * 操作IP
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operateIp;

}
