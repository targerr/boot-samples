package com.example.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wanggs
 * @since 2022-04-22
 */
@Data
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Blog {

    /**
     * 主键
     */
    @Id
    private String id;

    private String title;

    private String content;

    private LocalDateTime createDateTime;

    private LocalDateTime modifyDateTime;

}
