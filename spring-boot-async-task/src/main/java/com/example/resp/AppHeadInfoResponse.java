package com.example.resp;

import com.example.dto.BannerDTO;
import com.example.dto.LabelDTO;
import com.example.dto.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wgs
 * @Date 2022/5/24 14:33
 * @Classname AppHeadInfoResponse
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppHeadInfoResponse {
    private UserInfoDTO userInfoDTO;
    private BannerDTO bannerDTO;
    private LabelDTO labelDTO;
}
