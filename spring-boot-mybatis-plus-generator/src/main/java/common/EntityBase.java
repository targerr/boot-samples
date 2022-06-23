package common;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import common.EntityBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Author: wgs
 * @Date 2022/6/14 14:48
 * @Classname EntityBase
 * @Description
 */
public class EntityBase implements Serializable {
    @ApiModelProperty("序列")
    @TableId(
            value = "id",
            type = IdType.ASSIGN_ID
    )
    private String id;
    @ApiModelProperty("说明")
    private String description;
    @ApiModelProperty("创建时间")
    @TableField(
            fill = FieldFill.INSERT
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private LocalDateTime createDateTime;
    @ApiModelProperty("创建人")
    private String createName;
    @ApiModelProperty("修改时间")
    @TableField(
            fill = FieldFill.INSERT_UPDATE
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private LocalDateTime modifyDateTime;
    @ApiModelProperty("修改人")
    private String modifyName;
    @ApiModelProperty("删除状态（0：未删除，1：删除）")
    @TableLogic
    private Boolean isDelete;
    @ApiModelProperty("排序")
    private Integer sorting;
    @Version
    @ApiModelProperty(
            hidden = true,
            value = "版本号"
    )
    private Integer version;
}
