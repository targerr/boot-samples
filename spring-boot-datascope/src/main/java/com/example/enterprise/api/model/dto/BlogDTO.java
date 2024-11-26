package com.example.enterprise.api.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * DTO对象
 * </p>
 *
 * @author wgs
 * @since 2024-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class BlogDTO implements Serializable {


    private static final long serialVersionUID = 1L;
    private String id;

    private String title;

    private String content;

//    @Query(propName = "id", operator = Query.Operator.in)
    private List<String> ids;

}
