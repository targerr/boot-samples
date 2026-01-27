package org.example.param;

import lombok.Data;

/**
 * @author
 * @description TODO
 * @date 2024-10-23 21:20
 */
@Data
public class BlogParam {

    /**
     * 类别
     */
    private String type;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;
}
