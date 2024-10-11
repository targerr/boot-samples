package com.example.sequence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流水号表
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sequence_record")
@ApiModel(value = "SequenceRecord对象", description = "流水号表")
public class SequenceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("流水号id")
    @TableField("sequence_record_id")
    private Long sequenceRecordId;

    @ApiModelProperty("由流水号规则key、前缀、日期组成的名称")
    @TableField("sequence_record_name")
    private String sequenceRecordName;

    @ApiModelProperty("当前值")
    @TableField("current_value")
    private Integer currentValue;

    @ApiModelProperty("步长")
    @TableField("sequence_interval")
    private Integer sequenceInterval;

    @ApiModelProperty("创建者")
    @TableField("created_by")
    private Long createdBy;

    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @ApiModelProperty("更新者")
    @TableField("updated_by")
    private Long updatedBy;

    @ApiModelProperty("更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @ApiModelProperty("删除(0:正常,1已删除)")
    @TableField("del_flag")
    private Byte delFlag;

    @ApiModelProperty("系统最后修改时间")
    @TableField("sys_at")
    private LocalDateTime sysAt;


}
