package com.example.enterprise.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wgs
 * @since 2024-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField(value = "create_date_time", fill = FieldFill.INSERT)
    private LocalDateTime createDateTime;

    @TableField(value = "modify_date_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyDateTime;


}
