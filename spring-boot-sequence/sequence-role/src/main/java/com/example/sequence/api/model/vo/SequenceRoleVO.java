package com.example.sequence.api.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流水号规则表VO对象
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SequenceRoleVO", description = "流水号规则表VO对象")
public class SequenceRoleVO implements Serializable {


    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "流水号规则id", example = "1")
    private Long sequenceRoleId;

    @ApiModelProperty(value = "规则key")
    private String sequenceKey;

    @ApiModelProperty(value = "名称")
    private String sequenceName;

    @ApiModelProperty(value = "前缀")
    private String sequencePrefix;

    @ApiModelProperty(value = "时间格式")
    private String timeFormat;

    @ApiModelProperty(value = "流水号长度")
    private Byte sequenceLength;

    @ApiModelProperty(value = "流水号开始", example = "1")
    private Integer sequenceBegin;

    @ApiModelProperty(value = "流水号步长")
    private Byte sequenceInterval;

    @ApiModelProperty(value = "创建者", example = "1")
    private Long createdBy;

    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新者", example = "1")
    private Long updatedBy;

    @ApiModelProperty(value = "更新时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "删除(0:正常,1已删除)")
    private Byte delFlag;

    @ApiModelProperty(value = "系统最后修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sysAt;



}
