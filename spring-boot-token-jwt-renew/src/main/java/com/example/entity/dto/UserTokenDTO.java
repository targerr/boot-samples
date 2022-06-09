package com.example.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/6/9 09:00
 * @Classname UserTokenDTO
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTokenDTO {
    private String id;
    private Long gmtCreate;
    private String userName;
}
