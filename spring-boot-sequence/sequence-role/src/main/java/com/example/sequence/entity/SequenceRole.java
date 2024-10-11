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
 * 流水号规则表
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sequence_role")
@ApiModel(value = "SequenceRole对象", description = "流水号规则表")
public class SequenceRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("流水号规则id")
    @TableField("sequence_role_id")
    private Long sequenceRoleId;

    @ApiModelProperty("规则key")
    @TableField("sequence_key")
    private String sequenceKey;

    @ApiModelProperty("名称")
    @TableField("sequence_name")
    private String sequenceName;

    @ApiModelProperty("前缀")
    @TableField("sequence_prefix")
    private String sequencePrefix;

    @ApiModelProperty("时间格式")
    @TableField("time_format")
    private String timeFormat;

    @ApiModelProperty("流水号长度")
    @TableField("sequence_length")
    private Integer sequenceLength;

    @ApiModelProperty("流水号开始")
    @TableField("sequence_begin")
    private Integer sequenceBegin;

    @ApiModelProperty("流水号步长")
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
    private Integer delFlag;

    @ApiModelProperty("系统最后修改时间")
    @TableField("sys_at")
    private LocalDateTime sysAt;


}
