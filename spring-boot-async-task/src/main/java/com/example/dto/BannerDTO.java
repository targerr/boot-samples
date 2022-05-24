package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * @Author: wgs
 * @Date 2022/5/24 14:34
 * @Classname BannerDTO
 * @Description
 */
@AllArgsConstructor
@Data
@Builder
public class BannerDTO {
    String msg;
}
