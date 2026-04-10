package com.mamba.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统日志实体
 */
@Data
@TableName("sys_log")
public class SysLog {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 权限更新类型 1-7 */
    private Integer type;

    /** 对象id */
    @TableField("target_id")
    private Integer targetId;

    /** 旧值(JSON) */
    @TableField("old_value")
    private String oldValue;

    /** 新值(JSON) */
    @TableField("new_value")
    private String newValue;

    /** 操作人 */
    private String operator;

    /** 操作时间 */
    @TableField("operate_time")
    private LocalDateTime operateTime;

    /** 操作IP */
    @TableField("operate_ip")
    private String operateIp;

    /** 状态，0：未复原，1：已复原 */
    private Integer status;
}
