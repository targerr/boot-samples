package com.example.dto;

import com.example.enums.ServiceStrategy;
import com.example.param.AppInfoReq;
import com.example.param.UserInfoParam;
import lombok.Data;

import java.util.concurrent.*;

/**
 * @Author: wgs
 * @Date 2022/5/24 10:50
 * @Classname BaseRspDTO
 * @Description
 */
@Data
public class BaseRspDTO<T extends Object> {
    /**
     * 区分是DTO返回的唯一标记，比如是UserInfoDTO还是BannerDTO
     */
    private ServiceStrategy key;
    /**
     * 返回的数据
     */
    private T data;

}
